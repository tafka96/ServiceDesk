package com.service_desk.repository;

import com.service_desk.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketEntityRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findAllByClosedFalseOrderByPriorityDesc();

}
