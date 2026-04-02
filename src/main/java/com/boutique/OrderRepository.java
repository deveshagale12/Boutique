package com.boutique;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Long> {
    // Find all orders for a specific user
    List<ProductOrder> findByUserId(Long userId);
    
    // Find all pending orders for the admin
    List<ProductOrder> findByStatus(String status);
}