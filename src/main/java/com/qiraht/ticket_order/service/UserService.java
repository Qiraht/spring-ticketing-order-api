package com.qiraht.ticket_order.service;

import com.qiraht.ticket_order.config.CustomUserDetails;
import com.qiraht.ticket_order.dto.request.UserRequest;
import com.qiraht.ticket_order.dto.response.UserResponse;
import com.qiraht.ticket_order.entity.User;
import com.qiraht.ticket_order.exception.NotFoundException;
import com.qiraht.ticket_order.exception.ValidationException;
import com.qiraht.ticket_order.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(UserRequest request) {
        // email checks
        if (userRepository.existsByEmail(request.email())) {
            throw new ValidationException("Email already exists");
        }


        // password hashing
        String hashedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
                .email(request.email())
                .firstName(request.first_name())
                .lastName(request.last_name())
                .password(hashedPassword)
                .build();

        userRepository.save(user);

        return user.getId().toString();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return getUserByEmail(userDetails.getUsername());
    }

    public UserResponse formatUser(User user) {
        return new UserResponse(user.getId().toString(),user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
