package com.service_desk.services;

import com.service_desk.model.Ticket;
import com.service_desk.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TicketServiceTest {
    @Mock
    TicketRepository ticketRepository;

    @InjectMocks
    TicketService ticketService;

    @Test
    public void findByIdTest(){
        when(ticketRepository.findById(1)).thenReturn(mockTicket1);
        when(ticketRepository.findById(2)).thenReturn(mockTicket2);
        assertSame(mockTicket1, ticketService.getTicketById(1));
        assertSame(mockTicket2, ticketService.getTicketById(2));
    }

    @Test
    public void addTicketTest(){
        when(ticketRepository.save(any())).thenReturn(mockTicket1);
        assertSame(mockTicket1, ticketService.addTicket(mockTicket1));
    }

    @Test
    public void getAllOpenTicketsTest(){
        List<Ticket> testList = Arrays.asList(mockTicket1, mockTicket2);
        when(ticketRepository.findAllOpenTickets()).thenReturn(testList);
        assertEquals(testList, ticketService.getAllOpenTickets());
    }

    @Test
    public void updateTicketTest(){
        when(ticketRepository.update(mockTicket1)).thenReturn(mockTicket1);
        assertSame(mockTicket1, ticketService.updateTicket(mockTicket1));
    }

    @Test
    public void closeTicketTest(){
        when(ticketRepository.close(any())).thenReturn(mockClosedTicket);
        assertSame(mockClosedTicket, ticketService.closeTicket(3));
    }

    private final Ticket mockTicket1 = new Ticket(1, "Test title 1", "Testemail1@test.com", "Test problem 1", Ticket.Priority.AVERAGE,false, LocalDate.now(), null);
    private final Ticket mockTicket2 = new Ticket(2, "Test title 2", "Testemail2@test.com", "Test problem 2", Ticket.Priority.HIGH,false, LocalDate.now(), null);
    private final Ticket mockClosedTicket = new Ticket(3, "Test title 3", "Testemail3@test.com", "Test problem 3", Ticket.Priority.HIGHEST,true, LocalDate.now(), null);
}

