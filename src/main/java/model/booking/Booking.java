package model.booking;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.user.User;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.sql.Timestamp;

import static model.booking.Booking.*;

@NamedQueries({
        @NamedQuery(
                name = OVERLAPPING,
                query = "SELECT b " +
                        "FROM Booking b " +
                        "WHERE b.start < :inputEndDate and :inputStartDate < b.end"),
        @NamedQuery(
                name = GET_FROM_DATE,
                query = "SELECT b " +
                        "FROM Booking b " +
                        "WHERE function('DAY', b.start) =:inputDay" +
                        " and function('MONTH', b.start) =:inputMonth" +
                        " and function('YEAR', b.start) =:inputYear"),
        @NamedQuery(
                name = SPECIFIC_BOOKING,
                query = "SELECT b " +
                        "FROM Booking b " +
                        "WHERE b.subject=:subject and b.description=:description and" +
                        " b.start=:start and b.end=:end"),
        @NamedQuery(
                name = ALL,
                query = "SELECT b " +
                        "FROM Booking b ")
})

@Entity
@XmlRootElement(name = "booking")
@Getter
@Setter
@ToString

public class Booking {

    public static final String OVERLAPPING = "overlapping";
    public static final String GET_FROM_DATE = "getFromDay";
    public static final String SPECIFIC_BOOKING = "specificBooking";
    public static final String ALL = "uniqueRecord";

    public Booking() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getStart() {
        return start;
    }

    public Timestamp getEnd() {
        return end;
    }

}
