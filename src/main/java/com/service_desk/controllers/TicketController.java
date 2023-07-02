package com.service_desk.controllers;

import com.service_desk.exceptions.TicketClosedException;
import com.service_desk.exceptions.TicketNotFoundException;
import com.service_desk.model.AddTicketRequest;
import com.service_desk.model.TicketPriority;
import com.service_desk.model.TicketResponse;
import com.service_desk.model.UpdateTicketRequest;
import com.service_desk.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    
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
    @ExceptionHandler(MethodArgumentNotValidException.class)  //Could be replaced with ControllerAdvice, if me have many controllers
    public Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        var errorMessageList = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return Collections.singletonMap("errors", errorMessageList);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({TicketNotFoundException.class})
    public Map<String, List<String>> handleTicketNotFoundExceptions(TicketNotFoundException ex){
        return Collections.singletonMap("errors", List.of(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TicketClosedException.class)
    public Map<String, List<String>> handleTicketClosedExceptions(TicketClosedException ex){
        return Collections.singletonMap("errors", List.of(ex.getMessage()));
    }


}
