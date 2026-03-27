package com.qiraht.ticket_order.service;

import com.qiraht.ticket_order.constant.TicketStatus;
import com.qiraht.ticket_order.dto.request.TicketRequest;
import com.qiraht.ticket_order.dto.response.EventResponse;
import com.qiraht.ticket_order.dto.response.TicketResponse;
import com.qiraht.ticket_order.entity.Event;
import com.qiraht.ticket_order.entity.Ticket;
import com.qiraht.ticket_order.exception.NotFoundException;
import com.qiraht.ticket_order.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventService eventService;

    public TicketService(TicketRepository ticketRepository, EventService eventService) {
        this.ticketRepository = ticketRepository;
        this.eventService = eventService;
    }

    @Transactional
    public String bookTicket(TicketRequest request) {
       Event event = eventService.eventTicketSales(request.eventId(), request.quantity());

       Ticket ticket = Ticket.builder()
               .event(event)
               .quantity(request.quantity())
               .priceAtSales(event.getPrice())
               .status(TicketStatus.BOOKED)
               .build();

       ticketRepository.save(ticket);

       log.info("Ticket for event {} booked", event.getId());

       return ticket.getId().toString();
    }

    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        // Get all unique event IDs
        Set<UUID> eventIds = tickets.stream()
                .map(t -> t.getEvent().getId())
                .collect(Collectors.toSet());

        // Batch fetch events
        Map<UUID, EventResponse> eventMap = new HashMap<>();

        for (UUID eventId : eventIds) {
            EventResponse eventResponse = eventService.getEventById(eventId.toString());

            eventMap.put(eventId, eventResponse);
        }

        return tickets.stream()
                .map(t -> new TicketResponse(
                        t.getId().toString(),
                        eventMap.get(t.getEvent().getId()),
                        t.getPriceAtSales(),
                        t.getQuantity(),
                        t.getStatus()
                )).toList();
    }

    public TicketResponse getTicketById(String id) {
        UUID uuid = UUID.fromString(id);

        // get Ticket or throw error
        Ticket ticket = ticketRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Ticket " + id +" not found"));

        String eventId = ticket.getEvent().getId().toString();

        EventResponse formattedEvent = eventService.getEventById(eventId);

        return new TicketResponse(
                ticket.getId().toString(),
                formattedEvent,
                ticket.getPriceAtSales(),
                ticket.getQuantity(),
                ticket.getStatus()
        );
    }
}
