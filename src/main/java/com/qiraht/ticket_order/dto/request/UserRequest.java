package com.qiraht.ticket_order.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String first_name,

        @NotBlank
        String last_name,

        @NotBlank
        @Size(min = 6)
        String password
) {
}
