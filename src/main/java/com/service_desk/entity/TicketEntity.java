package com.service_desk.entity;

import com.service_desk.model.TicketPriority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tickets")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

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

}
