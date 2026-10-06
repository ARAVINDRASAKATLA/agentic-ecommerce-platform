package com.CustomerCare.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CustomerCare.client.InventoryClient;
import com.CustomerCare.client.ProductClient;
import com.CustomerCare.client.TrackingClient;
import com.CustomerCare.dto.gemini.FunctionDeclaration;
import com.CustomerCare.dto.gemini.Tool;
import com.CustomerCare.dto.tool.InventoryStockDto;
import com.CustomerCare.dto.tool.OrderDetailsDto;
import com.CustomerCare.dto.tool.ProductDetailsDto;
import com.CustomerCare.dto.tool.TrackingDetailsDto;

@Service
public class AgentToolService {

	private static final Logger log = LoggerFactory.getLogger(AgentToolService.class);

	@Autowired
	private ProductClient productClient;

	@Autowired
	private InventoryClient inventoryClient;

	@Autowired
	private TrackingClient trackingClient;

	public List<Tool> getDeclaredTools() {
		List<FunctionDeclaration> declarations = new ArrayList<>();

		// 1. lookupOrder
		Map<String, Object> lookupOrderProps = new HashMap<>();
		lookupOrderProps.put("orderId", Map.of(
				"type", "INTEGER",
				"description", "The unique integer ID of the customer order"
		));
		declarations.add(FunctionDeclaration.builder()
				.name("lookupOrder")
				.description("Look up an e-commerce order by order ID to check its items, status, total price, and deliveryId")
				.parameters(Map.of(
						"type", "OBJECT",
						"properties", lookupOrderProps,
						"required", List.of("orderId")
				))
				.build());

		// 2. trackOrder
		Map<String, Object> trackOrderProps = new HashMap<>();
		trackOrderProps.put("orderId", Map.of(
				"type", "INTEGER",
				"description", "The unique order ID to track"
		));
		declarations.add(FunctionDeclaration.builder()
				.name("trackOrder")
				.description("Look up real-time live GPS tracking location and status for an order")
				.parameters(Map.of(
						"type", "OBJECT",
						"properties", trackOrderProps,
						"required", List.of("orderId")
				))
				.build());

		// 3. searchProducts
		Map<String, Object> searchProductProps = new HashMap<>();
		searchProductProps.put("keyword", Map.of(
				"type", "STRING",
				"description", "Product name, title, or keyword to search in the catalog"
		));
		declarations.add(FunctionDeclaration.builder()
				.name("searchProducts")
				.description("Search products in the catalog by keyword to find prices, descriptions, and IDs")
				.parameters(Map.of(
						"type", "OBJECT",
						"properties", searchProductProps,
						"required", List.of("keyword")
				))
				.build());

		// 4. checkProductStock
		Map<String, Object> stockProps = new HashMap<>();
		stockProps.put("productId", Map.of(
				"type", "INTEGER",
				"description", "The product ID to check stock for"
		));
		declarations.add(FunctionDeclaration.builder()
				.name("checkProductStock")
				.description("Check inventory quantity, availability, and warehouse status for a specific product ID")
				.parameters(Map.of(
						"type", "OBJECT",
						"properties", stockProps,
						"required", List.of("productId")
				))
				.build());

		// 5. cancelOrder (with destructive operation confirmation safeguard)
		Map<String, Object> cancelProps = new HashMap<>();
		cancelProps.put("orderId", Map.of(
				"type", "INTEGER",
				"description", "The order ID to cancel"
		));
		cancelProps.put("confirmed", Map.of(
				"type", "BOOLEAN",
				"description", "Must be true only if the user has explicitly confirmed cancellation in the conversation"
		));
		declarations.add(FunctionDeclaration.builder()
				.name("cancelOrder")
				.description("Cancel an order and release reserved stock. Requires explicit confirmation from the customer before execution.")
				.parameters(Map.of(
						"type", "OBJECT",
						"properties", cancelProps,
						"required", List.of("orderId", "confirmed")
				))
				.build());

		return List.of(Tool.builder().functionDeclarations(declarations).build());
	}

	public Map<String, Object> executeTool(String functionName, Map<String, Object> args) {
		log.info("Executing Agent Tool: name={}, args={}", functionName, args);
		Map<String, Object> result = new HashMap<>();

		try {
			switch (functionName) {
				case "lookupOrder": {
					int orderId = ((Number) args.get("orderId")).intValue();
					try {
						OrderDetailsDto order = productClient.getOrder(orderId);
						result.put("order", order);
						result.put("found", true);
					} catch (Exception ex) {
						result.put("found", false);
						result.put("error", "Order #" + orderId + " was not found in the system.");
					}
					break;
				}

				case "trackOrder": {
					long orderId = ((Number) args.get("orderId")).longValue();
					try {
						TrackingDetailsDto tracking = trackingClient.getTracking(orderId);
						result.put("tracking", tracking);
						result.put("found", true);
					} catch (Exception ex) {
						result.put("found", false);
						result.put("error", "Live tracking is not yet active for Order #" + orderId + ".");
					}
					break;
				}

				case "searchProducts": {
					String keyword = (String) args.get("keyword");
					try {
						List<ProductDetailsDto> products = productClient.searchProducts(keyword);
						result.put("products", products);
						result.put("count", products != null ? products.size() : 0);
					} catch (Exception ex) {
						result.put("error", "Could not complete product search: " + ex.getMessage());
					}
					break;
				}

				case "checkProductStock": {
					int productId = ((Number) args.get("productId")).intValue();
					try {
						InventoryStockDto stock = inventoryClient.getInventory(productId);
						result.put("stock", stock);
						result.put("inStock", stock.getAvailableQuantity() > 0);
					} catch (Exception ex) {
						result.put("error", "Stock info unavailable for Product #" + productId);
					}
					break;
				}

				case "cancelOrder": {
					int orderId = ((Number) args.get("orderId")).intValue();
					Boolean confirmed = args.get("confirmed") != null && Boolean.parseBoolean(args.get("confirmed").toString());

					if (!Boolean.TRUE.equals(confirmed)) {
						result.put("status", "CONFIRMATION_REQUIRED");
						result.put("message", "Cancellation requires explicit user confirmation. Ask the user: 'Are you sure you want to cancel Order #" + orderId + "? Please reply YES to confirm.'");
						return result;
					}

					try {
						OrderDetailsDto cancelled = productClient.cancelOrder(orderId);
						result.put("status", "SUCCESS");
						result.put("message", "Order #" + orderId + " has been successfully cancelled and stock released.");
						result.put("order", cancelled);
					} catch (Exception ex) {
						result.put("status", "FAILED");
						result.put("error", ex.getMessage());
					}
					break;
				}

				default:
					result.put("error", "Unknown tool function: " + functionName);
			}
		} catch (Exception ex) {
			log.error("Tool execution failed for {}: {}", functionName, ex.getMessage(), ex);
			result.put("error", "Error executing " + functionName + ": " + ex.getMessage());
		}

		return result;
	}
}
