package com.qiraht.ticket_order.handler;

import com.qiraht.ticket_order.dto.ApiResponse;
import com.qiraht.ticket_order.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Not Found Error
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        log.error("error while processing request", ex);

        ApiResponse<?> body = ApiResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(body);
    }

    // Jakarta Validation Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("error while processing request", ex);

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation Failed");

        ApiResponse<?> body = ApiResponse.error(HttpStatus.BAD_REQUEST.value(), message, null);

        return ResponseEntity.badRequest().body(body);
    }

    // Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(Exception ex, HttpServletRequest request) {
        log.error("error while processing request", ex);

        ApiResponse<?> body = ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);

        return ResponseEntity.internalServerError().body(body);
    }
}
