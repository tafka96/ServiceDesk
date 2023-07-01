package com.service_desk.repository;

import com.service_desk.TestConfig;
import com.service_desk.entity.TicketEntity;
import com.service_desk.model.TicketPriority;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TicketEntityRepositoryTest {
    @Autowired
    TicketEntityRepository ticketEntityRepository;


    @Test
    public void repositoryReturnsCorrectTicketTest(){
        TicketEntity ticketEntity = ticketEntityRepository.findById(1L).get();
        assertEquals(1, ticketEntity.getId());
        assertEquals("Test Title 1", ticketEntity.getTitle());
        assertEquals("testemail1@test.ee", ticketEntity.getEmail());
        assertEquals("Test Problem 1", ticketEntity.getProblem());
        assertEquals(TicketPriority.AVERAGE, ticketEntity.getPriority());
    }

    @Test
    public void getOpenTicketsReturnsOnlyOpenTickets(){
        TicketEntity closedTicketEntity = ticketEntityRepository.findById(5L).get();
        assertTrue(closedTicketEntity.getClosed());
        List<TicketEntity> ticketEntities = ticketEntityRepository.findAllByClosedFalseOrderByPriorityDesc();
        ticketEntities.forEach(ticket -> assertFalse(ticket.getClosed()));
        assertEquals(TicketPriority.HIGHEST, ticketEntities.get(0).getPriority());
        assertEquals(TicketPriority.AVERAGE, ticketEntities.get(1).getPriority());
        assertEquals(TicketPriority.LOW, ticketEntities.get(2).getPriority());
        assertEquals(TicketPriority.LOWEST, ticketEntities.get(3).getPriority());
    }



}