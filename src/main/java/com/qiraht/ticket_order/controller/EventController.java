package com.qiraht.ticket_order.controller;

import com.qiraht.ticket_order.dto.ApiResponse;
import com.qiraht.ticket_order.dto.request.EventPostRequest;
import com.qiraht.ticket_order.dto.request.EventPutRequest;
import com.qiraht.ticket_order.dto.response.EventResponse;
import com.qiraht.ticket_order.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "POST Event", description = "Create new Event. Authentication Needed and only user with role 'ADMIN'")
    public ResponseEntity<ApiResponse<String>> postEvent(@Valid @RequestBody EventPostRequest request) {
        String data = eventService.createEvent(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                "Event created successfully",
                        data));
    }

    @GetMapping
    @Operation(summary = "GET All Events", description = """
            Get All Available Events for 'USER'. API needs Authentication
            User role with 'ADMIN' can view All data and User with role 'USER' will be limited.
            """)
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEvents() {
        List<EventResponse> data = eventService.getAllEvents();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                "success",
                        data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "GET Event by ID", description = "Get Event by ID. API needs Authentication")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable("id") String eventId) {
        EventResponse data = eventService.getEventById(eventId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "success",
                        data));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "PUT Event", description = "Edit Event By Id. API needs Authentication and Only User Role 'ADMIN'")
    public ResponseEntity<ApiResponse<String>> updateEvent(@PathVariable("id") String eventId,@Valid @RequestBody EventPutRequest request) {
        String data = eventService.editEventById(eventId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Event edited successfully",
                        data));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "DELETE Event", description = "Delete Event by Id. Soft Delete. API needs Authentication and Only User Role 'ADMIN'")
    public ResponseEntity<ApiResponse<String>> deleteEvent(@PathVariable("id") String eventId) {
        String data = eventService.deleteEventById(eventId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                "Event deleted successfully",
                        data));
    }
}
