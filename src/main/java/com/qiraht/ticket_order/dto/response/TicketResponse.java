package com.qiraht.ticket_order.dto.response;

import com.qiraht.ticket_order.constant.TicketStatus;

import java.math.BigDecimal;

public record TicketResponse(
        String id,
        EventResponse event,
        BigDecimal priceAtSales,
        Integer quantity,
        TicketStatus status,
        UserResponse user
) {
}
