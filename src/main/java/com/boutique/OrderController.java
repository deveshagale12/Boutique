package com.boutique;
import java.util.List;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/api/boutique/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*") // Allows your frontend to connect
public class OrderController {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo; // Assuming you have a UserRepository

    // USER: Book a product using IDs
    @PostMapping("/book")
    public ResponseEntity<?> bookProduct(@RequestParam Long userId, 
                                        @RequestParam Long productId, 
                                        @RequestParam Integer qty) {
        
        // 1. Verify User and Product exist in NeonDB
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepo.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. Create the Order
        ProductOrder order = new ProductOrder();
        order.setUser(user);
        order.setProduct(product);
        order.setQuantity(qty);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());

        orderRepo.save(order);
        return ResponseEntity.ok("Order requested successfully. Waiting for Admin approval.");
    }

    // ADMIN: Update Order (Accept/Edit)
   @PutMapping("/admin/update/{orderId}")
public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody Map<String, String> payload) {
    ProductOrder existingOrder = orderRepo.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found"));
    
    String newStatus = payload.get("status");

    if ("ACCEPTED".equalsIgnoreCase(newStatus) && !"ACCEPTED".equalsIgnoreCase(existingOrder.getStatus())) {
        Product p = existingOrder.getProduct();
        if (p.getStockQuantity() < existingOrder.getQuantity()) {
            return ResponseEntity.badRequest().body("Insufficient stock.");
        }
        p.setStockQuantity(p.getStockQuantity() - existingOrder.getQuantity());
        productRepo.save(p);
    }

    existingOrder.setStatus(newStatus);
    return ResponseEntity.ok(orderRepo.save(existingOrder));
}

    // ADMIN: Delete Order
    @DeleteMapping("/admin/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        orderRepo.deleteById(orderId);
        return ResponseEntity.ok("Order removed from system.");
    }

    // --- ADD THIS METHOD TO YOUR CONTROLLER ---
    @GetMapping("/admin/all")
public ResponseEntity<List<ProductOrder>> getAllOrders() {
    // This will return the list exactly as your frontend expects
    List<ProductOrder> allOrders = orderRepo.findAll();
    return ResponseEntity.ok(allOrders);
}
}