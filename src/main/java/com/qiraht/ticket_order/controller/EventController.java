package com.qiraht.ticket_order.controller;

import com.qiraht.ticket_order.dto.ApiResponse;
import com.qiraht.ticket_order.dto.request.EventPostRequest;
import com.qiraht.ticket_order.dto.request.EventPutRequest;
import com.qiraht.ticket_order.dto.response.EventResponse;
import com.qiraht.ticket_order.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@Tag(name = "Events", description = "Events related endpoint")
@Validated
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @Operation(summary = "Create Event", description = "Create new Event. Authentication Needed and role 'ADMIN'")
    public ResponseEntity<ApiResponse<String>> postEvent(@RequestBody EventPostRequest request) {
        String data = eventService.createEvent(request);

        ApiResponse<String> body = ApiResponse.success(
                HttpStatus.CREATED.value(),
                "Event created successfully",
                data
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEvents() {
        List<EventResponse> data = eventService.getAllEvents();

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "success", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable("id") String eventId) {
        EventResponse data = eventService.getEventById(eventId);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "success", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateEvent(@PathVariable("id") String eventId, @RequestBody EventPutRequest request) {
        String data = eventService.editEventById(eventId, request);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "success", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEvent(@PathVariable("id") String eventId) {
        String data = eventService.deleteEventById(eventId);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "success", data));
    }
}
