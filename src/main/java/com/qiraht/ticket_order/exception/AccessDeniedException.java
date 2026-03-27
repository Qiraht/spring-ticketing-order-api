package com.qiraht.ticket_order.exception;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
