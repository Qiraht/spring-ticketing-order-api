package com.qiraht.ticket_order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "status", "message", "data" })
public record ApiResponse<T>(Integer status, String message, T data) {
    public static <T> ApiResponse<T> success(Integer status, String message,T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> error(Integer status, String message,T data) {
        return new ApiResponse<>(status, message, data);
    }
}
