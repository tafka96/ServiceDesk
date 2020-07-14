package com.service_desk.repository;

import com.service_desk.TestConfig;
import com.service_desk.model.Ticket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {TicketRepository.class, TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TicketRepositoryTest {
    @Autowired
    TicketRepository ticketRepository;


    @Test
    @Transactional
    public void aupdateTest(){
        Ticket ticket = ticketRepository.findById(1);
        assertEquals("Test Problem 1", ticket.getProblem());
        ticket.setProblem("New Problem");
        ticketRepository.update(ticket);

        ticket = ticketRepository.findById(1);
        assertEquals("New Problem", ticket.getProblem());
    }


    @Test
    public void repositoryReturnsCorrectTicketTest(){
        Ticket ticket = ticketRepository.findById(1);
        assertEquals(1, ticket.getId());
        assertEquals("Test Title 1", ticket.getTitle());
        assertEquals("testemail1@test.ee", ticket.getEmail());
        assertEquals("Test Problem 1", ticket.getProblem());
        assertEquals(Ticket.Priority.AVERAGE, ticket.getPriority());
    }

    @Test
    public void getOpenTicketsReturnsOnlyOpenTickets(){
        Ticket closedTicket = ticketRepository.findById(3);
        assertTrue(closedTicket.getClosed());
        List<Ticket> tickets = ticketRepository.findAllOpenTickets();
        tickets.forEach(ticket -> assertFalse(ticket.getClosed()));
    }

    @Test
    @Transactional
    public void saveTest(){
     Ticket ticket = getMockAddTicket();
     assertNull(ticket.getId());
     ticketRepository.save(ticket);
     assertFalse(ticket.getClosed());
     assertNotNull(ticket.getId());
    }


    private Ticket getMockAddTicket(){
        Ticket ticket = new Ticket();
        ticket.setTitle("Test title 1");
        ticket.setEmail("Testemail1@test.com");
        ticket.setProblem("Test problem");
        ticket.setPriority(Ticket.Priority.LOWEST);
        return ticket;
    }

}