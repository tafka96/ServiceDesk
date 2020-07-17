package com.service_desk.model;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
@NamedQueries({
        @NamedQuery(name = "Ticket.FindAllOpen", query = "SELECT t FROM Ticket t WHERE t.closed=false")})
public class Ticket {
    public enum Priority{
        LOWEST, LOW, AVERAGE,  HIGH, HIGHEST
    }
    @Size(min= 3, max = 50, message = "Title must be between 3 and 50 letters")
    @NotNull(message = "Title must be between 3 and 50 letters")
    @Column(name = "title")
    private String title;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Email(regexp = ".+@.+\\..+", message = "Please enter a correct email")
    @NotNull(message = "Please enter a correct email")
    @Size(max = 100, message = "Email must be at most 100 letters")
    private String email;

    @Size(min = 3, message = "Problem description must be at least 3 letters")
    @Size(max = 1000, message = "Problem description must be most 1000 letters")
    @Column(name = "problem")
    private String problem;

    @Enumerated(EnumType.ORDINAL)
    @NotNull(message = "Please set problem priority")
    @Column(name = "priority")
    private Priority priority;

    @Column(name = "closed")
    private Boolean closed;

    @Column(name = "createdDate")
    private LocalDate createdDate;

    @Column(name = "closedDate")
    private LocalDate closedDate;


    public Ticket(Integer id, String title, String email, String problem, Priority priority, Boolean closed, LocalDate createdDate, LocalDate closedDate) {
        this.id = id;
        this.title = title;
        this.email = email;
        this.problem = problem;
        this.priority = priority;
        this.closed = closed;
        this.createdDate = createdDate;
        this.closedDate = closedDate;
    }

    public Ticket() {
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", problem='" + problem + '\'' +
                ", priority=" + priority +
                ", closed=" + closed +
                '}';
    }
}
