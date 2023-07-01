package com.service_desk.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service_desk.model.AddTicketRequest;
import com.service_desk.model.TicketPriority;
import com.service_desk.model.TicketResponse;
import com.service_desk.model.UpdateTicketRequest;
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
class TicketEntityControllerTest {
    @MockBean
    TicketService ticketService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getTicketByIdTest() throws Exception {
        when(ticketService.getTicketById(1L)).thenReturn(mockTicket1);
        when(ticketService.getTicketById(2L)).thenReturn(mockTicket2);
        mockMvc.perform(get("/api/tickets/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is(mockTicket1.getEmail())));

        mockMvc.perform(get("/api/tickets/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.email", is(mockTicket2.getEmail())));

        verify(ticketService, times(2)).getTicketById(anyLong());
    }
    private final TicketResponse mockTicket1 = TicketResponse
            .builder()
            .id(1L)
            .title("Test title 1")
            .problem("Test problem 1")
            .priority(TicketPriority.AVERAGE.toString())
            .closed(false)
            .createdDate(LocalDate.now())
            .closedDate(null).build();
    private final TicketResponse mockTicket2 = TicketResponse
            .builder()
            .id(2L)
            .title("Test title 2")
            .problem("Test problem 2")
            .priority(TicketPriority.HIGH.toString())
            .closed(false)
            .createdDate(LocalDate.now())
            .closedDate(null).build();



    @Test
    public void getAllTicketsTest() throws Exception {
        when(ticketService.getAllOpenTickets()).thenReturn(Arrays.asList(mockTicket1, mockTicket2));
        mockMvc.perform(get("/api/tickets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is(mockTicket1.getTitle())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is(mockTicket2.getTitle())));
        verify(ticketService, times(1)).getAllOpenTickets();
    }

    @Test
    public void updateTicketTest() throws Exception{
        var request = new UpdateTicketRequest();
        request.setId(1L);
        request.setProblem("Test problem");
        request.setPriority(TicketPriority.HIGH);
        request.setTitle("Test title");
        request.setEmail("test@test.com");

        when(ticketService.updateTicket(request)).thenReturn(mockTicket1);
        mockMvc.perform(post("/api/tickets/update").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.problem", is(mockTicket1.getProblem())));
        verify(ticketService, times(1)).updateTicket(any(UpdateTicketRequest.class));
    }

    @Test
    public void addTicketValidationTest() throws Exception {
        var request = new AddTicketRequest();
        request.setTitle("Test title 1, Test title 1, Test titddddddddddle 1, Test title 1");
        request.setEmail("Testemail@testcom");
        request.setProblem("Test problem 1");
        request.setPriority(TicketPriority.AVERAGE);
        mockMvc.perform(post("/api/tickets/add").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.errors", Matchers.containsInAnyOrder("Title must be between 3 and 50 letters","Please enter a correct email")));

        verify(ticketService, times(0)).addTicket(any(AddTicketRequest.class));
    }



    @Test
    public void updateTicketValidationTest() throws Exception {
        var request = new UpdateTicketRequest();
        request.setId(1L);
        request.setProblem("Test problem");
        request.setPriority(TicketPriority.HIGH);
        request.setTitle("Te");
        request.setEmail("testtest.com");

        mockMvc.perform(post("/api/tickets/update").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.errors", Matchers.containsInAnyOrder("Title must be between 3 and 50 letters","Please enter a correct email")));
        verify(ticketService, times(0)).updateTicket(any(UpdateTicketRequest.class));
    }

    @Test
    public void closeTicketTest() throws Exception{
        when(ticketService.closeTicket(1L)).thenReturn(mockTicket1);
        mockMvc.perform(get("/api/tickets/close/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.problem", is(mockTicket1.getProblem())));
        verify(ticketService, times(1)).closeTicket(any());
    }

    @Test
    public void getPrioritiesTest() throws Exception{
        mockMvc.perform(get("/api/tickets/priorities").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(TicketPriority.values().length)));

    }
}