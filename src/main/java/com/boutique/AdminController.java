package com.boutique;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/boutique/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<Admin> register(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.registerAdmin(admin));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Admin admin = adminService.loginAdmin(credentials.get("email"), credentials.get("password"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Admin Login Successful");
        response.put("adminName", admin.getFullName());
        response.put("role", admin.getRole());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String message = adminService.forgotPassword(request.get("email"), request.get("newPassword"));
        
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
}