package com.qiraht.ticket_order.service;

import com.qiraht.ticket_order.dto.response.EventReportResponse;
import com.qiraht.ticket_order.dto.response.ReportSummaryResponse;
import com.qiraht.ticket_order.entity.Event;
import com.qiraht.ticket_order.repository.EventRepository;
import com.qiraht.ticket_order.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ReportService {
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public ReportService(EventRepository eventRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
    }

    public ReportSummaryResponse getReportSummary() {
        List<Event> events = eventRepository.findAllByDeletedAtNull();
        List<EventReportResponse> eventReports = new ArrayList<>();
        long totalTicketsSold = 0;
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Event event : events) {
            int ticketsSold = event.getCapacity() - event.getAvailableSlot();
            BigDecimal revenue = ticketRepository.calculateRevenueByEventId(event.getId());
            if (revenue == null) {
                revenue = BigDecimal.ZERO;
            }

            EventReportResponse report = new EventReportResponse(
                    event.getId().toString(),
                    event.getName(),
                    event.getCapacity(),
                    event.getAvailableSlot(),
                    ticketsSold,
                    revenue
            );

            eventReports.add(report);
            totalTicketsSold += ticketsSold;
            totalRevenue = totalRevenue.add(revenue);
        }

        return new ReportSummaryResponse(
                (long) events.size(),
                totalTicketsSold,
                totalRevenue,
                eventReports
        );
    }

    public EventReportResponse getReportEvent(String id) {
        Event event = eventRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        int ticketsSold = event.getCapacity() - event.getAvailableSlot();
        BigDecimal revenue = ticketRepository.calculateRevenueByEventId(event.getId());
        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }

        return new EventReportResponse(
                event.getId().toString(),
                event.getName(),
                event.getCapacity(),
                event.getAvailableSlot(),
                ticketsSold,
                revenue
        );
    }
}
