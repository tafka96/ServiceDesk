package com.service_desk.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode
@Builder
public class TicketResponse {
    private String title;
    private Long id;
    private String email;
    private String problem;
    private String priority;
    private Boolean closed;
    private LocalDate createdDate;
    private LocalDate closedDate;

}
