package com.qiraht.ticket_order.controller;

import com.qiraht.ticket_order.dto.ApiResponse;
import com.qiraht.ticket_order.dto.response.EventReportResponse;
import com.qiraht.ticket_order.dto.response.ReportSummaryResponse;
import com.qiraht.ticket_order.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports", description = "Endpoint for Event Sales Reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "GET Report Summary", description = "Get all event sales Summary.  API needs Authentication and Only user role 'ADMIN'")
    public ResponseEntity<ApiResponse<ReportSummaryResponse>> getReportSummary() {
        return ResponseEntity.ok(ApiResponse.success(200, "Successfully fetched report summary", reportService.getReportSummary()));
    }

    @GetMapping("/event/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "GET Report Event", description = "Get vent sales Summary. API needs Authentication and Only user role 'ADMIN'")
    public ResponseEntity<ApiResponse<EventReportResponse>> getReportEvent(@Valid @PathVariable @NotBlank String id) {
        return ResponseEntity.ok(ApiResponse.success(200, "Successfully fetched event report", reportService.getReportEvent(id)));
    }
}
