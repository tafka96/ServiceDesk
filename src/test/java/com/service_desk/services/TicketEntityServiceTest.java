package com.service_desk.services;

import com.service_desk.entity.TicketEntity;
import com.service_desk.exceptions.TicketNotFoundException;
import com.service_desk.model.AddTicketRequest;
import com.service_desk.model.TicketPriority;
import com.service_desk.model.UpdateTicketRequest;
import com.service_desk.repository.TicketEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TicketEntityServiceTest {
    @Mock
    TicketMapper ticketMapper;

    @Mock
    TicketEntityRepository ticketEntityRepository;

    @Captor
    ArgumentCaptor<TicketEntity> ticketEntityArgumentCaptor;
    @InjectMocks
    TicketService ticketService;

    @Test
    public void findByIdTest(){
        when(ticketMapper.map((TicketEntity) any())).thenCallRealMethod();
        when(ticketEntityRepository.findById(1L)).thenReturn(Optional.of(mockTicket1Entity));
        when(ticketEntityRepository.findById(2L)).thenReturn(Optional.of(mockTicket2Entity));
        var mockResponse1 = ticketMapper.map(mockTicket1Entity);
        var mockResponse2 = ticketMapper.map(mockTicket2Entity);

        assertEquals(mockResponse1, ticketService.getTicketById(1L));
        assertEquals(mockResponse2, ticketService.getTicketById(2L));
    }

    @Test
    public void findByIdThrowsErrorWhenNotFoundTest(){
        when(ticketEntityRepository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(4L));
    }



    @Test
    public void addTicketTest(){
        when(ticketEntityRepository.save(any())).thenReturn(mockTicket1Entity);
        var request = getAddTicketRequest();
        ticketService.addTicket(request);
        verify(ticketEntityRepository).save(ticketEntityArgumentCaptor.capture());
        var ticketEntity = ticketEntityArgumentCaptor.getValue();

        assertEquals(request.getProblem(), ticketEntity.getProblem());
        assertEquals(request.getTitle(), ticketEntity.getTitle());
        assertEquals(request.getEmail(), ticketEntity.getEmail());
        assertEquals(request.getPriority(), ticketEntity.getPriority());
    }

    private AddTicketRequest getAddTicketRequest(){
        var r = new AddTicketRequest();
        r.setProblem("Test problem");
        r.setTitle("Test title");
        r.setEmail("Test email");
        r.setPriority(TicketPriority.HIGHEST);
        return r;
    }

    @Test
    public void getAllOpenTicketsTest(){
        List<TicketEntity> testList = Arrays.asList(mockTicket1Entity, mockTicket2Entity);
        when(ticketEntityRepository.findAllByClosedFalseOrderByPriorityDesc()).thenReturn(testList);
        assertEquals(ticketMapper.map(testList), ticketService.getAllOpenTickets());
    }


    @Test
    public void updateTicketThrowsErrorWhenNotFoundTest(){
        when(ticketEntityRepository.findById(4L)).thenReturn(Optional.empty());
        var request = new UpdateTicketRequest();
        request.setId(4L);
        assertThrows(TicketNotFoundException.class, () -> ticketService.updateTicket(request));
    }

    @Test
    public void updateTicketTest(){
        var request = new UpdateTicketRequest();
        request.setId(1L);
        request.setProblem("New problem");
        request.setPriority(mockTicket1Entity.getPriority());
        request.setEmail(mockTicket1Entity.getEmail());
        request.setTitle(mockTicket1Entity.getTitle());


        when(ticketEntityRepository.findById(1L)).thenReturn(Optional.of(mockTicket1Entity));
       ticketService.updateTicket(request);
        verify(ticketEntityRepository).save(ticketEntityArgumentCaptor.capture());
        var entity = ticketEntityArgumentCaptor.getValue();

        assertEquals(request.getId(), entity.getId());
        assertEquals(mockTicket1Entity.getTitle(), entity.getTitle());
        assertEquals(mockTicket1Entity.getEmail(), entity.getEmail());
        assertEquals(request.getPriority(), entity.getPriority());

    }

    @Test
    public void closeTicketTest(){
        when(ticketEntityRepository.findById(1L)).thenReturn(Optional.of(mockTicket1Entity));
        ticketService.closeTicket(1L);
        verify(ticketEntityRepository).save(ticketEntityArgumentCaptor.capture());
        var entity = ticketEntityArgumentCaptor.getValue();
        assertEquals(true, entity.getClosed());

    }
    @Test
    public void closeTicketThrowsErrorWhenNotFoundTest(){
        when(ticketEntityRepository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.closeTicket(4L));
    }
    private final TicketEntity mockTicket1Entity = new TicketEntity(1L, "Test title 1", "Testemail1@test.com", "Test problem 1", TicketPriority.AVERAGE,false, LocalDate.now(), null);
    private final TicketEntity mockTicket2Entity = new TicketEntity(2L, "Test title 2", "Testemail2@test.com", "Test problem 2", TicketPriority.HIGH,false, LocalDate.now(), null);
}

