package com.qiraht.ticket_order.service;

import com.qiraht.ticket_order.constant.TicketStatus;
import com.qiraht.ticket_order.constant.UserRole;
import com.qiraht.ticket_order.dto.request.TicketRequest;
import com.qiraht.ticket_order.dto.response.EventResponse;
import com.qiraht.ticket_order.dto.response.TicketResponse;
import com.qiraht.ticket_order.dto.response.UserResponse;
import com.qiraht.ticket_order.entity.Event;
import com.qiraht.ticket_order.entity.Ticket;
import com.qiraht.ticket_order.entity.User;
import com.qiraht.ticket_order.exception.AccessDeniedException;
import com.qiraht.ticket_order.exception.NotFoundException;
import com.qiraht.ticket_order.exception.ValidationException;
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
    private final UserService userService;

    public TicketService(TicketRepository ticketRepository, EventService eventService, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    @Transactional
    public String bookTicket(TicketRequest request) {
       Event event = eventService.eventTicketSales(request.eventId(), request.quantity());

       User user = userService.getCurrentUser();

       Ticket ticket = Ticket.builder()
               .event(event)
               .quantity(request.quantity())
               .priceAtSales(event.getPrice())
               .user(user)
               .status(TicketStatus.BOOKED)
               .build();

       ticketRepository.save(ticket);

       log.info("Ticket for event {} booked", event.getId());

       return ticket.getId().toString();
    }

    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();

        User user = userService.getCurrentUser();

        if(user.getRole() == UserRole.ADMIN) {
           tickets = ticketRepository.findAll();
        } else {
            tickets = ticketRepository.findAllByUserId(user.getId());
        }

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
                        t.getStatus(),
                        userService.formatUser(t.getUser())
                )).toList();
    }

    public TicketResponse getTicketById(String id) {
        UUID uuid = UUID.fromString(id);

        User user = userService.getCurrentUser();

        // get Ticket or throw error
        Ticket ticket = ticketRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Ticket " + id +" not found"));

        // check owner
        if (user.getRole() == UserRole.USER && !user.getId().equals(ticket.getUser().getId())) {
            throw new AccessDeniedException("You are not allowed to access this ticket");
        }

        String eventId = ticket.getEvent().getId().toString();

        EventResponse formattedEvent = eventService.getEventById(eventId);
        UserResponse formattedUser = userService.formatUser(user);

        return new TicketResponse(
                ticket.getId().toString(),
                formattedEvent,
                ticket.getPriceAtSales(),
                ticket.getQuantity(),
                ticket.getStatus(),
                formattedUser
        );
    }

    public String cancelBookedTicket(String id) {
        UUID ticketId = UUID.fromString(id);

        User user = userService.getCurrentUser();

        // get Ticket or throw error
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new NotFoundException("Ticket " + id +" not found"));

        // check owner
        if (user.getRole() == UserRole.USER && !user.getId().equals(ticket.getUser().getId())) {
            throw new AccessDeniedException("You are not allowed to access this ticket");
        }

        // check ticket status
        if (ticket.getStatus().equals(TicketStatus.CANCELLED)) {
            throw new ValidationException("Ticket " + id + " is already cancelled");
        }

        // returning ticket slot
        eventService.returnCancelledTicketSlot(ticket.getEvent().getId(), ticket.getQuantity());

        ticket.setStatus(TicketStatus.CANCELLED);

        ticketRepository.save(ticket);

        log.info("Ticket {} for event {} cancelled", ticket.getId(), ticket.getEvent().getId());
        return ticket.getId().toString();
    }
}
