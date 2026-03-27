package com.qiraht.ticket_order.service;

import com.qiraht.ticket_order.dto.request.TicketRequest;
import com.qiraht.ticket_order.entity.Event;
import com.qiraht.ticket_order.entity.Ticket;
import com.qiraht.ticket_order.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventService eventService;

    public TicketService(TicketRepository ticketRepository, EventService eventService) {
        this.ticketRepository = ticketRepository;
        this.eventService = eventService;
    }

    public String createTicket(TicketRequest request) {
       Event event = eventService.eventTicketSales(request.eventId(), request.quantity());

       Ticket ticket = Ticket.builder()
               .event(event)
               .quantity(request.quantity())
               .priceAtSales(event.getPrice())
               .build();

       ticketRepository.save(ticket);

       log.info("Ticket for event {} booked", event.getId());

       return ticket.getId().toString();
    }
}
