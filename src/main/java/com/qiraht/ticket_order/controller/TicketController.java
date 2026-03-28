package com.qiraht.ticket_order.controller;

import com.qiraht.ticket_order.dto.ApiResponse;
import com.qiraht.ticket_order.dto.request.TicketRequest;
import com.qiraht.ticket_order.dto.response.TicketResponse;
import com.qiraht.ticket_order.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Tickets", description = "Ticket related endpoint")
@Validated
public class TicketController {
    final private TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<String>> postTicket(@Valid @RequestBody TicketRequest request) {
        String data = ticketService.bookTicket(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "ticket successfully booked",
                        data));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<TicketResponse>>>  getTickets() {
        List<TicketResponse> data = ticketService.getAllTickets();

        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "success",
                        data));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<TicketResponse>>  getTicketById(@NotBlank @PathVariable("id") String ticketId) {
        TicketResponse data = ticketService.getTicketById(ticketId);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        "success",
                        data));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<String>> patchTicketByID(@NotBlank @PathVariable("id") String ticketId) {
        String data = ticketService.cancelBookedTicket(ticketId);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "ticket cancelled successfully",
                        data));
    }
}
