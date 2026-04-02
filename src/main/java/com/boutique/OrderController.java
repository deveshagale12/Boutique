package com.boutique;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/boutique/orders")
@CrossOrigin(origins = "*")
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
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody ProductOrder updateDetails) {
        ProductOrder existingOrder = orderRepo.findById(orderId).orElseThrow();
        
        // Logic: If Admin Accepts, reduce stock
        if ("ACCEPTED".equalsIgnoreCase(updateDetails.getStatus()) && !"ACCEPTED".equalsIgnoreCase(existingOrder.getStatus())) {
            Product p = existingOrder.getProduct();
            if (p.getStockQuantity() < existingOrder.getQuantity()) {
                return ResponseEntity.badRequest().body("Insufficient stock to fulfill this order.");
            }
            p.setStockQuantity(p.getStockQuantity() - existingOrder.getQuantity());
            productRepo.save(p);
        }

        existingOrder.setStatus(updateDetails.getStatus());
        return ResponseEntity.ok(orderRepo.save(existingOrder));
    }

    // ADMIN: Delete Order
    @DeleteMapping("/admin/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        orderRepo.deleteById(orderId);
        return ResponseEntity.ok("Order removed from system.");
    }
}