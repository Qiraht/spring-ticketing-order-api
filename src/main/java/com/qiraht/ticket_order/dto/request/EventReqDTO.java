package com.qiraht.ticket_order.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventReqDTO(
        @NotBlank String name,

        String description,

        @NotNull
        @Future
        LocalDateTime eventDate,

        @NotBlank @DecimalMin(value = "0.01")
        BigDecimal price,

        @NotNull
        @Min(1)
        Integer capacity
) {
}
