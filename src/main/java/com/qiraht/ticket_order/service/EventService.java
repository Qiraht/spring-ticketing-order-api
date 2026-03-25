package com.qiraht.ticket_order.service;

import com.qiraht.ticket_order.dto.request.EventReqDTO;
import com.qiraht.ticket_order.entity.Event;
import com.qiraht.ticket_order.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public String createEvent(EventReqDTO request) {
        Event event = Event.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .eventDate(request.eventDate())
                .capacity(request.capacity())
                .available(request.capacity())
                .build();

        log.info("Event created: {}", event.getId());

        eventRepository.save(event);

        return event.getId().toString();
    }
}
