package com.service_desk.controllers;

import com.service_desk.exceptions.TicketNotFoundException;
import com.service_desk.model.AddTicketRequest;
import com.service_desk.model.TicketPriority;
import com.service_desk.model.TicketResponse;
import com.service_desk.model.UpdateTicketRequest;
import com.service_desk.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/api/tickets")
public class TicketController {
    @Autowired
    TicketService ticketService;
    
    @GetMapping(path = "/{id}")
    public TicketResponse getTicket(@PathVariable(value = "id") Long id){
        return ticketService.getTicketById(id);
    }

    @GetMapping
    public List<TicketResponse> getAllTickets(){
        return ticketService.getAllOpenTickets();
    }

    @PostMapping(path = "/add")
    public TicketResponse addTicket(@Valid @RequestBody AddTicketRequest request){
        return ticketService.addTicket(request);
    }

    @PostMapping(path = "/update")
    public TicketResponse updateTicket(@Valid @RequestBody UpdateTicketRequest request){
        return ticketService.updateTicket(request);
    }

    @GetMapping(path = "/priorities")
    public TicketPriority[] getPriorities(){
        return TicketPriority.values();
    }

    @GetMapping(path = "/close/{id}")
    public TicketResponse closeTicket(@PathVariable(value = "id") Long id){
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TicketNotFoundException.class)
    public Map<String, List<String>> handleObjectNotFoundExceptions(TicketNotFoundException ex){
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("errors", List.of(ex.getMessage()));
        return errors;
    }


}
