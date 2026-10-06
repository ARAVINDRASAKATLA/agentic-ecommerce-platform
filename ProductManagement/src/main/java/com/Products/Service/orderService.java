package com.Products.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Products.Builder.orderBuilder;
import com.Products.Dao.orderRepository;
import com.Products.Dao.productRepository;
import com.Products.Model.orderItems;
import com.Products.Model.product;
import com.Products.Model.userOrder;
import com.Products.client.DeliveryClient;
import com.Products.client.InventoryClient;
import com.Products.client.UserClient;
import com.Products.dto.Response.DeliveryResponse;
import com.Products.dto.Response.userOrderResponse;
import com.Products.dto.Response.userResponse;
import com.Products.dto.feign.InventoryCheckRequestDto;
import com.Products.dto.feign.InventoryReserveRequestDto;
import com.Products.dto.feign.InventoryResponseDto;
import com.Products.dto.request.DeliveryCreateRequest;
import com.Products.dto.request.orderCreateRequest;
import com.Products.serviceImpl.orderServiceImpl;

@Service
public class orderService implements orderServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(orderService.class);
	
	@Autowired
	private orderRepository orderRepository;
	
	@Autowired
	private productRepository productRepository;
	
	@Autowired
	private InventoryClient inventoryClient;
	
	@Autowired
	private DeliveryClient deliveryClient;
	
	@Autowired
	private UserClient userClient;

	@Autowired
	private com.Products.kafka.OrderEventProducer orderEventProducer;

	@Override
	@Transactional
	public userOrderResponse createOrder(orderCreateRequest orderRequest) {
		userOrder order = orderBuilder.buildOrderCreateRequest(orderRequest);
		double totalPrice = 0;
		
		// 1. Validate products and calculate prices
		if (order.getOrderItems() != null) {
			for (orderItems items : order.getOrderItems()) {
				product product = productRepository.findById(items.getProductId())
						.orElseThrow(() -> new RuntimeException("Product not found with id: " + items.getProductId()));
				items.setPrice(product.getPrice());
				totalPrice += product.getPrice() * items.getQuantity();
				items.setOrder(order);

				// 2. Check stock with Inventory Service
				try {
					InventoryResponseDto check = inventoryClient.checkStock(InventoryCheckRequestDto.builder()
							.productId(items.getProductId())
							.quantity(items.getQuantity())
							.build());
					if (check == null || !check.isSuccess()) {
						throw new RuntimeException("Product is currently out of stock.");
					}
				} catch (Exception ex) {
					if (ex.getMessage() != null && ex.getMessage().contains("out of stock")) {
						throw new RuntimeException("Product is currently out of stock.");
					}
					log.warn("Inventory check warning: {}", ex.getMessage());
				}
			}
		}

		// 3. Reserve stock in Inventory Service
		if (order.getOrderItems() != null) {
			for (orderItems items : order.getOrderItems()) {
				try {
					inventoryClient.reserveStock(InventoryReserveRequestDto.builder()
							.productId(items.getProductId())
							.quantity(items.getQuantity())
							.build());
				} catch (Exception ex) {
					log.warn("Inventory reservation warning for product {}: {}", items.getProductId(), ex.getMessage());
					throw new RuntimeException("Product is currently out of stock.");
				}
			}
		}

		// 4. Confirm and persist order
		order.setTotalprice(totalPrice);
		order.setOrderTime(LocalDateTime.now());
		order.setStatus("CONFIRMED");
		userOrder savedOrder = orderRepository.save(order);
		
		// 5. Create delivery
		String trackingNumber = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		DeliveryCreateRequest deliveryCreateRequest = DeliveryCreateRequest.builder()
				.orderId(savedOrder.getOrderId())
				.trackingNumber(trackingNumber)
				.ShippingAddress("Customer Delivery Address")
				.estimatedDeliveryDate(LocalDate.now().plusDays(5))
				.status("CREATED")
				.build();
		   
		try {
			DeliveryResponse deliveryResponse = deliveryClient.createDelivery(deliveryCreateRequest);
			if (deliveryResponse != null && deliveryResponse.getDeliveryId() > 0) {
				savedOrder.setDeliveryId(deliveryResponse.getDeliveryId());
				savedOrder = orderRepository.save(savedOrder);
			}
		} catch (Exception ex) {
			log.warn("Could not dispatch delivery creation to DeliveryManagement: {}", ex.getMessage());
		}
		
		// 6. Enrich with User profile details
		userResponse userResponse = null;
		try {
			userResponse = userClient.getUser(order.getUserId());
		} catch (Exception ex) {
			log.warn("Could not fetch user details from UserManagement: {}", ex.getMessage());
			userResponse = userResponse.builder()
					.userId(order.getUserId())
					.userName("Customer #" + order.getUserId())
					.build();
		}

		userOrderResponse response = orderBuilder.builduserOrderResponse(savedOrder, userResponse);
		response.setMessage("Order created and confirmed successfully. Delivery scheduled.");

		// Publish ORDER_CONFIRMED event to Kafka
		try {
			orderEventProducer.publishOrderEvent(com.Products.dto.event.OrderEvent.builder()
					.orderId(savedOrder.getOrderId())
					.userId(savedOrder.getUserId())
					.totalPrice(savedOrder.getTotalprice())
					.status(savedOrder.getStatus())
					.eventType("ORDER_CONFIRMED")
					.deliveryId(savedOrder.getDeliveryId())
					.timestamp(LocalDateTime.now())
					.build());
		} catch (Exception ex) {
			log.warn("Could not publish order confirmation to Kafka: {}", ex.getMessage());
		}

		return response;
	}

	@Override
	public userOrderResponse getUserOrders(int id) {
		userOrder userOrder = orderRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Order Id not found: " + id));

		userResponse userResponse = null;
		try {
			userResponse = userClient.getUser(userOrder.getUserId());
		} catch (Exception ex) {
			log.warn("Could not fetch user details for order {}: {}", id, ex.getMessage());
			userResponse = userResponse.builder()
					.userId(userOrder.getUserId())
					.userName("Customer #" + userOrder.getUserId())
					.build();
		}
		
		return orderBuilder.builduserOrderResponse(userOrder, userResponse);
	}

	@Override
	@Transactional
	public userOrderResponse cancelOrder(int id) {
		userOrder userOrder = orderRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Order Id not found: " + id));

		if ("CANCELLED".equalsIgnoreCase(userOrder.getStatus())) {
			throw new RuntimeException("Order #" + id + " is already cancelled.");
		}
		if ("OUT_FOR_DELIVERY".equalsIgnoreCase(userOrder.getStatus()) || "DELIVERED".equalsIgnoreCase(userOrder.getStatus())) {
			throw new RuntimeException("Order #" + id + " cannot be cancelled as it is already " + userOrder.getStatus() + ". Please request a return upon delivery.");
		}

		userOrder.setStatus("CANCELLED");
		userOrder savedOrder = orderRepository.save(userOrder);

		// Release reserved inventory
		if (savedOrder.getOrderItems() != null) {
			for (orderItems item : savedOrder.getOrderItems()) {
				try {
					inventoryClient.releaseStock(InventoryReserveRequestDto.builder()
							.productId(item.getProductId())
							.quantity(item.getQuantity())
							.build());
				} catch (Exception ex) {
					log.warn("Could not release stock for product {}: {}", item.getProductId(), ex.getMessage());
				}
			}
		}

		userResponse userResponse = null;
		try {
			userResponse = userClient.getUser(savedOrder.getUserId());
		} catch (Exception ex) {
			userResponse = userResponse.builder()
					.userId(savedOrder.getUserId())
					.userName("Customer #" + savedOrder.getUserId())
					.build();
		}

		userOrderResponse response = orderBuilder.builduserOrderResponse(savedOrder, userResponse);
		response.setMessage("Order #" + id + " has been successfully cancelled. Reserved inventory was released.");

		// Publish ORDER_CANCELLED event to Kafka
		try {
			orderEventProducer.publishOrderEvent(com.Products.dto.event.OrderEvent.builder()
					.orderId(savedOrder.getOrderId())
					.userId(savedOrder.getUserId())
					.totalPrice(savedOrder.getTotalprice())
					.status("CANCELLED")
					.eventType("ORDER_CANCELLED")
					.deliveryId(savedOrder.getDeliveryId())
					.timestamp(LocalDateTime.now())
					.build());
		} catch (Exception ex) {
			log.warn("Could not publish order cancellation to Kafka: {}", ex.getMessage());
		}

		return response;
	}

}
