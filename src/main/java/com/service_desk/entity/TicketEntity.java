package com.service_desk.entity;

import com.service_desk.model.TicketPriority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "tickets")
@Data
@EqualsAndHashCode
public class TicketEntity {

    @Column(name = "title")
    private String title;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 100, message = "Email must be at most 100 letters")
    private String email;

    @Column(name = "problem")
    private String problem;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "priority")
    private TicketPriority priority;

    @Column(name = "closed")
    private Boolean closed;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "closed_date")
    private LocalDate closedDate;


    public TicketEntity(Long id, String title, String email, String problem, TicketPriority priority, Boolean closed, LocalDate createdDate, LocalDate closedDate) {
        this.id = id;
        this.title = title;
        this.email = email;
        this.problem = problem;
        this.priority = priority;
        this.closed = closed;
        this.createdDate = createdDate;
        this.closedDate = closedDate;
    }

    public TicketEntity() {
    }

}
