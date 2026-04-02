
package com.boutique;

import java.time.LocalDateTime;

import jakarta.persistence.*;
@Entity
@Table(name = "product_orders")
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Link to the User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer quantity;
    private String status; // PENDING, ACCEPTED, REJECTED
    private LocalDateTime orderDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

    // Getters and Setters...
    
    
}