package com.boutique;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/boutique/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserService userService;

    // Standard Constructor Injection
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegistrationRequest request) {
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "Boutique user registered successfully!",
            "status", "SUCCESS"
        ));
    }
    
    @PostMapping("/login")
public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request) {
    User user = userService.loginUser(request);
    
    // Create the response map
    Map<String, Object> response = new HashMap<>();
    
    // CRITICAL: Add the ID so the frontend can use it for orders!
    response.put("id", user.getId()); 
    
    response.put("message", "Login successful!");
    response.put("fullName", user.getFullName());
    response.put("email", user.getEmail());
    response.put("status", "SUCCESS");
    
    return ResponseEntity.ok(response);
}
}