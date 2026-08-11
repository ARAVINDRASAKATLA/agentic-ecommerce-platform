package com.Products.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class orderItems {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderItemId;
	
	@ManyToOne
	@JoinColumn(name="order_id")
	private userOrder order;
	
	private int productId;
	private int quantity;
	private double price;
	
	
	
	

}
