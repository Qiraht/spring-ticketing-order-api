package com.qiraht.ticket_order.service;

import com.qiraht.ticket_order.config.CustomUserDetails;
import com.qiraht.ticket_order.dto.request.AuthRequest;
import com.qiraht.ticket_order.repository.UserRepository;
import com.qiraht.ticket_order.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String loginUser(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        CustomUserDetails userDetails = (CustomUserDetails) Objects.requireNonNull(
                authentication.getPrincipal(), "Principal should not be null");

        return jwtUtil.generateToken(userDetails);
    }
}
