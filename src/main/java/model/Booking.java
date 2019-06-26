package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.Objects;

import static model.Booking.*;

@NamedQueries({
        @NamedQuery(
                name = OVERLAPPING,
                query = "SELECT b " +
                        "FROM Booking b " +
                        "WHERE b.start < :inputEndDate and :inputStartDate < b.end"),
        @NamedQuery(
                name = GET_BOOKING_FROM_DATE,
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
                name = GET_ALL_BOOKINGS,
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
    public static final String GET_BOOKING_FROM_DATE = "getFromDay";
    public static final String SPECIFIC_BOOKING = "specificBooking";
    public static final String GET_ALL_BOOKINGS = "uniqueRecord";

    public Booking() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String subject;
    private String description;
    private Date start;
    private Date end;

    @XmlTransient
    @ManyToOne
    @JoinColumn
    private User user;

    public Booking(String subject, String description, Date startDate, Date endDate) {
        this.subject = subject;
        this.start = startDate;
        this.end = endDate;
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return subject.equals(booking.subject) &&
                Objects.equals(description, booking.description) &&
                start.equals(booking.start) &&
                end.equals(booking.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, description, start, end);
    }
}
