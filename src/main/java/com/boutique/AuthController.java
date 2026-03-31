package com.boutique;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}