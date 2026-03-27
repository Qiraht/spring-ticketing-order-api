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
    public ResponseEntity<ApiResponse<String>> postTicket(@RequestBody TicketRequest request) {
        String data = ticketService.bookTicket(request);

        ApiResponse<String> body = ApiResponse.success(HttpStatus.OK.value(), "Ticket successfully booked", data);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketResponse>>>  getTickets() {
        List<TicketResponse> data = ticketService.getAllTickets();

        ApiResponse<List<TicketResponse>>  body = ApiResponse.success(HttpStatus.OK.value(), "success", data);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<TicketResponse>>  getTicketById(@Valid @NotBlank String id) {
        TicketResponse data = ticketService.getTicketById(id);

        ApiResponse<TicketResponse>  body = ApiResponse.success(HttpStatus.OK.value(), "success", data);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
