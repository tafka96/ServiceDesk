package com.service_desk.controllers;

import com.service_desk.model.Ticket;
import com.service_desk.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TicketController {
    @Autowired
    TicketService ticketService;

    @GetMapping(path = "/tickets/{id}")
    public Ticket getTicket(@PathVariable(value = "id") Integer id){
        return ticketService.getTicketById(id);
    }

    @GetMapping(path = "/tickets")
    public List<Ticket> getAllTickets(){
        return ticketService.getAllOpenTickets();
    }

    @PostMapping(path = "/tickets/add")
    public Ticket addTicket(@Valid @RequestBody Ticket ticket){
        return ticketService.addTicket(ticket);
    }

    @PostMapping(path = "/tickets/update")
    public Ticket updateTicket(@Valid @RequestBody Ticket ticket){
        return ticketService.updateTicket(ticket);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        System.out.println("Gere");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
