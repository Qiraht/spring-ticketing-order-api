package com.qiraht.ticket_order.controller;

import com.qiraht.ticket_order.dto.ApiResponse;
import com.qiraht.ticket_order.dto.request.EventReqDTO;
import com.qiraht.ticket_order.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@Validated
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @Tag(name = "Events")
    @Operation(summary = "Create Event", description = "Create new Event. Authentication Needed and role 'ADMIN'")
    public ResponseEntity<ApiResponse<String>> postEvent(@RequestBody EventReqDTO eventReqDTO) {
        String data = eventService.createEvent(eventReqDTO);

        ApiResponse<String> body = ApiResponse.success(
                HttpStatus.CREATED.value(),
                "Event created successfully",
                data
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    };
}
