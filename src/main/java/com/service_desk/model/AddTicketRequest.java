package com.service_desk.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddTicketRequest {
    @Size(min= 3, max = 50, message = "Title must be between 3 and 50 letters")
    @NotNull(message = "Title must be between 3 and 50 letters")
    private String title;

    @Email(regexp = ".+@.+\\..+", message = "Please enter a correct email")
    @NotNull(message = "Please enter a correct email")
    @Size(max = 100, message = "Email must be at most 100 letters")
    private String email;

    @Size(min = 3, message = "Problem description must be at least 3 letters")
    @Size(max = 1000, message = "Problem description must be most 1000 letters")
    private String problem;

    @NotNull(message = "Please set problem priority")
    private TicketPriority priority;
}
