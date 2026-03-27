package com.qiraht.ticket_order.controller;

import com.qiraht.ticket_order.dto.ApiResponse;
import com.qiraht.ticket_order.dto.request.AuthRequest;
import com.qiraht.ticket_order.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> postLogin(@Valid @RequestBody AuthRequest request) {
        String data = authService.loginUser(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "login successful",
                        data));
    }
}
