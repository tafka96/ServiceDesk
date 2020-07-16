package com.service_desk.services;

import com.service_desk.model.Ticket;
import com.service_desk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;

    public Ticket getTicketById(Integer id){
        return ticketRepository.findById(id);
    }


    public Ticket addTicket(Ticket ticket){
        ticket.setClosed(false);
        ticket.setCreatedDate(LocalDate.now());
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllOpenTickets(){
        List<Ticket> tickets =  ticketRepository.findAllOpenTickets();
        tickets.sort(Comparator.comparing(Ticket::getPriority, Comparator.reverseOrder()));
        return tickets;
    }

    public Ticket updateTicket(Ticket ticket){
        return ticketRepository.update(ticket);
    }

    public Ticket closeTicket(Integer id){
        return ticketRepository.close(id);
    }

}
