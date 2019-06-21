package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.sql.Timestamp;

import static model.Booking.OVERLAPPING;

@NamedQuery(
        name = OVERLAPPING,
        query = "SELECT b " +
                "FROM Booking b " +
                "WHERE b.start < :inputEndDate and :inputStartDate < b.end")
@Entity
@XmlRootElement(name="booking")
@Getter
@Setter
@ToString
public class Booking {

    public static final String OVERLAPPING = "overlapping";
    public static final String ALL = "all";

    public Booking() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookingId;

    private String subject;
    private String description;
    private Timestamp start;
    private Timestamp end;

    @XmlTransient
    @ManyToOne
    @JoinColumn
    private User user;

    public Booking(String subject, Timestamp startDate, Timestamp endDate) {
        this.subject = subject;
        this.start = startDate;
        this.end = endDate;
    }

    public Booking(String subject, String description, Timestamp startDate, Timestamp endDate) {
        this.subject = subject;
        this.start = startDate;
        this.end = endDate;
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
