package com.service_desk.controllers;

import com.service_desk.model.Ticket;
import com.service_desk.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/api")
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

    @GetMapping(path = "/priorities")
    public Ticket.Priority[] getPriorities(){
        return Ticket.Priority.values();
    }

    @GetMapping(path = "tickets/close/{id}")
    public Ticket closeTicket(@PathVariable(value = "id") Integer id){
        return ticketService.closeTicket(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("errors", new ArrayList<>());
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            errors.get("errors").add(error.getDefaultMessage());
        }
        return errors;
    }

}
