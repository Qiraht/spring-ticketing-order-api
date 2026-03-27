package com.qiraht.ticket_order.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ReportSummaryResponse(
        Long totalEvents,
        Long overallTicketsSold,
        BigDecimal overallRevenue,
        List<EventReportResponse> events
) {
}
