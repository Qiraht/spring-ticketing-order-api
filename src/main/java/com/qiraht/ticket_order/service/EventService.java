package com.qiraht.ticket_order.service;

import com.qiraht.ticket_order.constant.EventStatus;
import com.qiraht.ticket_order.constant.UserRole;
import com.qiraht.ticket_order.dto.request.EventPostRequest;
import com.qiraht.ticket_order.dto.request.EventPutRequest;
import com.qiraht.ticket_order.dto.response.EventResponse;
import com.qiraht.ticket_order.entity.Event;
import com.qiraht.ticket_order.entity.User;
import com.qiraht.ticket_order.exception.NotFoundException;
import com.qiraht.ticket_order.exception.ValidationException;
import com.qiraht.ticket_order.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;

    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    public String createEvent(EventPostRequest request) {
        User user = userService.getCurrentUser();

        Event event = Event.builder()
                .name(request.name())
                .description(request.description())
                .location(request.location())
                .price(request.price())
                .eventDate(request.eventDate())
                .capacity(request.capacity())
                .availableSlot(request.capacity())
                .status(EventStatus.ACTIVE)
                .user(user)
                .build();

        log.info("Event created: {}", event.getId());

        eventRepository.save(event);

        return event.getId().toString();
    }

    public List<EventResponse> getAllEvents() {
        User user = userService.getCurrentUser();

        List<Event> events = new ArrayList<>();

        // admin only allowed to get non deleted
        if (user.getRole() == UserRole.ADMIN) {
            events = eventRepository.findAll();
        } else {
            events = eventRepository.findAllByDeletedAtNull();
        }

        log.info("Found {} events", events.size());

        return events.stream()
                .map(event -> new EventResponse(
                        event.getId().toString(),
                        event.getName(),
                        event.getDescription(),
                        event.getLocation(),
                        event.getEventDate(),
                        event.getPrice(),
                        event.getCapacity(),
                        event.getAvailableSlot(),
                        event.getStatus(),
                        userService.formatUser(event.getUser())
                )).toList();
    }

    public EventResponse getEventById(String id) {
        UUID uuid = UUID.fromString(id);

        User user = userService.getCurrentUser();

        Event event = eventRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));

        if (user.getRole() != UserRole.ADMIN && event.getDeletedAt() != null) {
            throw new ValidationException("Event with id " + id + " has been deleted");
        }

        log.info("Event found: {}", event.getId());

        return new EventResponse(
                event.getId().toString(),
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                event.getEventDate(),
                event.getPrice(),
                event.getCapacity(),
                event.getAvailableSlot(),
                event.getStatus(),
                userService.formatUser(event.getUser())
        );
    }

    public String editEventById(String id, EventPutRequest request) {
        UUID uuid = UUID.fromString(id);

        Event event = eventRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));

        // check event status
        if (event.getStatus() == EventStatus.FINISHED) {
            throw new ValidationException("Event with id " + id + " has been finished, cannot be edited");
        }

        event.setName(request.name());
        event.setDescription(request.description());
        event.setLocation(request.location());
        event.setEventDate(request.eventDate());
        event.setPrice(request.price());

        eventRepository.save(event);

        return event.getId().toString();
    }

    public String deleteEventById(String id) {
        UUID uuid = UUID.fromString(id);

        Event event = eventRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));

        event.setDeletedAt(LocalDateTime.now());

        return event.getId().toString();
    }

    @Transactional
    public Event eventTicketSales(String id, Integer quantity) {
        UUID uuid = UUID.fromString(id);

        // Get event or throw error
        Event event = eventRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));

        // check available slot
        if (event.getAvailableSlot() < quantity) {
            throw new ValidationException("Not enough tickets available. Available: " + event.getAvailableSlot());
        }

        // check event active from is DeletedAt
        if (event.getDeletedAt() != null) {
            throw new ValidationException("Event is not available " + event.getId());
        }

        // check event active from eventDate
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Event is expired " + event.getId());
        }

        // check event status
        if (event.getStatus() == EventStatus.FINISHED) {
            throw new ValidationException("Event is finished " + event.getId());
        }

        int remainingSlot = event.getAvailableSlot() - quantity;

        event.setAvailableSlot(remainingSlot);

        eventRepository.save(event);

        log.info("Event sold for {} of {} remaining", event.getId(), remainingSlot);

        return event;
    }

    @Transactional
    public void returnCancelledTicketSlot(UUID id, Integer quantity) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));

        // check event status
        if (event.getStatus() == EventStatus.FINISHED) {
            throw new ValidationException("Event is ongoing/finished " + event.getId());
        }

        event.setAvailableSlot(event.getAvailableSlot() + quantity);

        eventRepository.save(event);
    }
}
