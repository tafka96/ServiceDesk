package com.service_desk.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service_desk.model.Ticket;
import com.service_desk.services.TicketService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TicketController.class)
@AutoConfigureMockMvc
class TicketControllerTest {
    @MockBean
    TicketService ticketService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getTicketByIdTest() throws Exception {
        when(ticketService.getTicketById(1)).thenReturn(mockTicket1);
        when(ticketService.getTicketById(2)).thenReturn(mockTicket2);
        mockMvc.perform(get("/api/tickets/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(mockTicket1.getId())))
                .andExpect(jsonPath("$.email", is(mockTicket1.getEmail())));

        mockMvc.perform(get("/api/tickets/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(mockTicket2.getId())))
                .andExpect(jsonPath("$.email", is(mockTicket2.getEmail())));

        verify(ticketService, times(2)).getTicketById(anyInt());
    }

    @Test
    public void getAllTicketsTest() throws Exception {
        when(ticketService.getAllOpenTickets()).thenReturn(Arrays.asList(mockTicket1, mockTicket2));
        mockMvc.perform(get("/api/tickets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(mockTicket1.getId())))
                .andExpect(jsonPath("$[0].title", is(mockTicket1.getTitle())))
                .andExpect(jsonPath("$[1].id", is(mockTicket2.getId())))
                .andExpect(jsonPath("$[1].title", is(mockTicket2.getTitle())));
        verify(ticketService, times(1)).getAllOpenTickets();
    }

    @Test
    public void addTicketTest() throws Exception{
        when(ticketService.addTicket(any(Ticket.class))).thenAnswer(i -> {
            Ticket returnTicket = getMockAddTicket();
            returnTicket.setId(13);
            returnTicket.setClosed(false);
            return returnTicket;
        });

        Ticket ticket = getMockAddTicket();
        mockMvc.perform(post("/api/tickets/add").content(objectMapper.writeValueAsString(ticket)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(13)))
                .andExpect(jsonPath("$.title", is(getMockAddTicket().getTitle())))
                .andExpect(jsonPath("$.closed", is(false)));

        verify(ticketService, times(1)).addTicket(any(Ticket.class));
    }

    @Test
    public void updateTicketTest() throws Exception{
        when(ticketService.updateTicket(any(Ticket.class))).thenReturn(mockTicket1);
        mockMvc.perform(post("/api/tickets/update").content(objectMapper.writeValueAsString(mockTicket1)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(mockTicket1.getId())))
                .andExpect(jsonPath("$.problem", is(mockTicket1.getProblem())));
        verify(ticketService, times(1)).updateTicket(any(Ticket.class));
    }

    @Test
    public void addTicketValidationTest() throws Exception {
        mockMvc.perform(post("/api/tickets/add").content(objectMapper.writeValueAsString(failValidationTicket)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.errors", Matchers.containsInAnyOrder("Title must be between 3 and 50 letters","Please enter a correct email")));

        verify(ticketService, times(0)).addTicket(any(Ticket.class));
    }

    @Test
    public void updateTicketValidationTest() throws Exception {
        mockMvc.perform(post("/api/tickets/update").content(objectMapper.writeValueAsString(failValidationTicket)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.errors", Matchers.containsInAnyOrder("Title must be between 3 and 50 letters","Please enter a correct email")));


        verify(ticketService, times(0)).updateTicket(any(Ticket.class));
    }

    private final Ticket mockTicket1 = new Ticket(1, "Test title 1", "Testemail1@test.com", "Test problem 1", Ticket.Priority.AVERAGE,false, LocalDate.now(), null);
    private final Ticket mockTicket2 = new Ticket(2, "Test title 2", "Testemail2@test.com", "Test problem 2", Ticket.Priority.HIGH,false, LocalDate.now(), null);
    private final Ticket failValidationTicket = new Ticket(1, "Test title 1, Test title 1, Test titddddddddddle 1, Test title 1", "Testemail@testcom", "Test problem 1", Ticket.Priority.AVERAGE,false , LocalDate.now(), null);
    private Ticket getMockAddTicket(){
        Ticket ticket = new Ticket();
        ticket.setTitle("Test title 1");
        ticket.setEmail("Testemail1@test.com");
        ticket.setProblem("Test problem");
        ticket.setPriority(Ticket.Priority.LOWEST);
        return ticket;
    }
}