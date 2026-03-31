package com.boutique;

import org.springframework.stereotype.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Used to check if an email is already taken during registration
    boolean existsByEmail(String email);

    // Useful later for the login feature
    Optional<User> findByEmail(String email);
    
    // You could also find users by mobile if needed
    Optional<User> findByMobileNumber(String mobileNumber);
}