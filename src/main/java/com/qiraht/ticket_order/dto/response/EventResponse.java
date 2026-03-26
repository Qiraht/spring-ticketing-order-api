package com.qiraht.ticket_order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventResponse(
        String id,
        String name,
        String description,
        String location,
        LocalDateTime eventDate,
        BigDecimal price,
        Integer capacity,
        Integer availableSlot
) {
}
