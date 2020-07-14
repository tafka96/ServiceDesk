package com.service_desk.repository;

import com.service_desk.model.Ticket;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class TicketRepository {
    @PersistenceContext
    EntityManager entityManager;

    public List<Ticket> findAllOpenTickets() {
        TypedQuery<Ticket> query = entityManager.createNamedQuery("Ticket.FindAllOpen", Ticket.class);
        return query.getResultList();
    }

    @Transactional
    public Ticket save(Ticket ticket){
        ticket.setClosed(false);
        entityManager.persist(ticket);
        return ticket;
    }
    @Transactional
    public Ticket update(Ticket  ticket){
        entityManager.merge(ticket);
        return ticket;
    }

    public Ticket findById(Integer id){
        return entityManager.find(Ticket.class, id);
    }
}
