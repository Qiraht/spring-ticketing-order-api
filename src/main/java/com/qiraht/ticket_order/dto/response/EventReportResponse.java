package com.qiraht.ticket_order.dto.response;

import java.math.BigDecimal;

public record EventReportResponse(
        String eventId,
        String eventName,
        Integer capacity,
        Integer availableSlot,
        Integer ticketsSold,
        BigDecimal revenue
) {
}
