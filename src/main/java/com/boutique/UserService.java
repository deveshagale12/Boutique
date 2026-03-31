package com.boutique;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public void registerUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("User already exists with this email!");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setMobileNumber(request.mobileNumber());
        user.setDob(request.dob());
        
        // Storing password exactly as it comes in
        user.setPassword(request.password()); 
        
        userRepository.save(user);
    }
	public User loginUser(LoginRequest request) {
        // Find user or throw error if not found
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found with this email"));

        // Compare plain-text passwords
        if (!user.getPassword().equals(request.password())) {
            throw new RuntimeException("Invalid password credentials");
        }

        return user;
    }
}