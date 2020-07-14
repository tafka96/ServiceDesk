package com.service_desk.services;

import com.service_desk.model.Ticket;
import com.service_desk.repository.TicketRepository;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;

    public Ticket getTicketById(Integer id){
        return ticketRepository.findById(id);
    }


    public Ticket addTicket(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllOpenTickets(){
        return ticketRepository.findAllOpenTickets();
    }

    public Ticket updateTicket(Ticket ticket){
        return ticketRepository.update(ticket);
    }

}
