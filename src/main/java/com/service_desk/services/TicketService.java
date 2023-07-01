package com.service_desk.services;

import com.service_desk.entity.TicketEntity;
import com.service_desk.exceptions.TicketNotFoundException;
import com.service_desk.model.AddTicketRequest;
import com.service_desk.model.TicketResponse;
import com.service_desk.model.UpdateTicketRequest;
import com.service_desk.repository.TicketEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketEntityRepository ticketEntityRepository;
    private final TicketMapper ticketMapper;


    public TicketResponse getTicketById(Long id){
        var ticketEntity = getTicketEntity(id);
        return ticketMapper.map(ticketEntity);
    }


    public TicketResponse addTicket(AddTicketRequest request){
        var ticketEntity = new TicketEntity();
        ticketEntity.setPriority(request.getPriority());
        ticketEntity.setTitle(request.getTitle());
        ticketEntity.setEmail(request.getEmail());
        ticketEntity.setProblem(request.getProblem());
        ticketEntity.setClosed(false);
        ticketEntity.setCreatedDate(LocalDate.now());
        return ticketMapper.map(ticketEntityRepository.save(ticketEntity));
    }


    public List<TicketResponse> getAllOpenTickets(){
        List<TicketEntity> ticketEntities =  ticketEntityRepository.findAllByClosedFalseOrderByPriorityDesc();
        return ticketMapper.map(ticketEntities);
    }

    public TicketResponse updateTicket(UpdateTicketRequest request){
        var ticketEntity = getTicketEntity(request.getId());

        ticketEntity.setProblem(request.getProblem());
        ticketEntity.setTitle(request.getTitle());
        ticketEntity.setEmail(request.getEmail());
        ticketEntity.setPriority(request.getPriority());

        return ticketMapper.map(ticketEntityRepository.save(ticketEntity));
    }

    public TicketResponse closeTicket(Long id){
        var ticket = getTicketEntity(id);

        ticket.setClosed(true);
        ticket.setClosedDate(LocalDate.now());
        return ticketMapper.map(ticketEntityRepository.save(ticket));
    }

    private TicketEntity getTicketEntity(Long id){
        return ticketEntityRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket with id"+id+ " not found"));
    }
}
