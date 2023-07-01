package com.service_desk.exceptions;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
