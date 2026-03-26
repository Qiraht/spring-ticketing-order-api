package com.qiraht.ticket_order.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventPutRequest(
        @NotBlank String name,

        String description,

        @NotNull
        @FutureOrPresent
        LocalDateTime eventDate,

        @NotBlank @DecimalMin(value = "0.01")
        BigDecimal price
) {
}
