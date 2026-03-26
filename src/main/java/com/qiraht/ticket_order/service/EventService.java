package com.qiraht.ticket_order.service;

import com.qiraht.ticket_order.dto.request.EventPostRequest;
import com.qiraht.ticket_order.dto.request.EventPutRequest;
import com.qiraht.ticket_order.dto.response.EventResponse;
import com.qiraht.ticket_order.entity.Event;
import com.qiraht.ticket_order.exception.NotFoundException;
import com.qiraht.ticket_order.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public String createEvent(EventPostRequest request) {
        Event event = Event.builder()
                .name(request.name())
                .description(request.description())
                .location(request.location())
                .price(request.price())
                .eventDate(request.eventDate())
                .capacity(request.capacity())
                .availableSlot(request.capacity())
                .build();

        log.info("Event created: {}", event.getId());

        eventRepository.save(event);

        return event.getId().toString();
    }

    public List<EventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();

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
                        event.getAvailableSlot()
                )).toList();
    }

    public EventResponse getEventById(String id) {
        UUID uuid = UUID.fromString(id);

        Event event = eventRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));

        log.info("Event found: {}", event.getId());

        return new EventResponse(
                event.getId().toString(),
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                event.getEventDate(),
                event.getPrice(),
                event.getCapacity(),
                event.getAvailableSlot()
        );
    }

    public String editEventById(String id, EventPutRequest request) {
        UUID uuid = UUID.fromString(id);

        Event event = eventRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));

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
}
