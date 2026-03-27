package com.qiraht.ticket_order.dto.response;

public record UserResponse(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
