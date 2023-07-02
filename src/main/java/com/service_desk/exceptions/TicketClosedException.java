package com.service_desk.exceptions;

public class TicketClosedException extends RuntimeException{
    public TicketClosedException(String errorMessage) {
        super(errorMessage);
    }
}
