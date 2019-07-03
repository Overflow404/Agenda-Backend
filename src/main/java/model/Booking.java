package model;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

@Entity
@XmlRootElement(name = "booking")
@Getter @Setter
public class Booking {

    private final static Logger logger = LoggerFactory.getLogger(Booking.class);

    public static final String OVERLAPPING = "overlapping";
    public static final String GET_BOOKING_FROM_CALENDAR_AND_DATE = "getFromDay";
    public static final String SPECIFIC_BOOKING = "specificBooking";
    public static final String GET_ALL_BOOKINGS = "uniqueRecord";
    public static final String GET_BOOKINGS_FROM_CALENDAR_AND_DATE_AND_USER = "getBookingsFromDateAndUser";

    public Booking() {
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String subject;
    private String description;
    private Date start;
    private Date end;

    @XmlTransient @ManyToOne @JoinColumn
    private Calendar calendar;

    public Booking(String subject, String description, Date startDate, Date endDate) {
        this.subject = subject;
        this.start = startDate;
        this.end = endDate;
        this.description = description;
    }

    void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

}
