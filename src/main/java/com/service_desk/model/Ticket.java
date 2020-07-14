package com.service_desk.model;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tickets")
@NamedQueries({
        @NamedQuery(name = "Ticket.FindAllOpen", query = "SELECT t FROM Ticket t WHERE t.closed=false")})
public class Ticket {
    public enum Priority{
        HIGHEST(5), HIGH(4), AVERAGE(3), LOW(2), LOWEST(1);
        Priority(int i) {}

    }
    @Size(max = 50, message = "Title must be less than 50 letters")
    private String title;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Email(regexp = ".+@.+\\..+", message = "Incorrect email")
    private String email;

    private String problem;
    private Priority priority;
    @Column(name = "closed", insertable = false)
    private Boolean closed;

    public Ticket(Integer id, String title, String email, String problem, Priority priority, Boolean closed) {
        this.id = id;
        this.title = title;
        this.email = email;
        this.problem = problem;
        this.priority = priority;
        this.closed = closed;
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
