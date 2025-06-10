package com.utp.webdevelopment.service;

import com.utp.webdevelopment.model.User;
import com.utp.webdevelopment.model.enums.UserRole;
import com.utp.webdevelopment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> findAllUsersPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public User createUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default values if not provided
        if (user.getRole() == null) {
            user.setRole(UserRole.CUSTOMER);
        }
        if (user.getStatus() == null) {
            user.setStatus("ACTIVE");
        }
        
        User savedUser = userRepository.save(user);
        log.info("Created user with ID: {}", savedUser.getId());
        return savedUser;
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userDetails.getName());
                    existingUser.setEmail(userDetails.getEmail());
                    existingUser.setAddress(userDetails.getAddress());
                    existingUser.setPhone(userDetails.getPhone());
                    existingUser.setRole(userDetails.getRole());
                    existingUser.setStatus(userDetails.getStatus());
                    existingUser.setEmailVerified(userDetails.getEmailVerified());
                    existingUser.setPhoneVerified(userDetails.getPhoneVerified());
                    
                    // Only update password if provided
                    if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    }
                    
                    User updatedUser = userRepository.save(existingUser);
                    log.info("Updated user with ID: {}", updatedUser.getId());
                    return updatedUser;
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("Deleted user with ID: {}", id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public long countUsers() {
        return userRepository.count();
    }

    public long countUsersByRole(UserRole role) {
        return userRepository.countByRole(role);
    }

    public long countUsersByStatus(String status) {
        return userRepository.countByStatus(status);
    }
} 