package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Entity
@XmlRootElement(name = "calendar")
@Getter @Setter @ToString
public class Calendar {

    public static final String GET_CALENDAR_BY_NAME = "getCalendarByName";

    public Calendar() {

    }

    public Calendar(String calendarName) {
        this.calendarName = calendarName;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String calendarName;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Booking> bookings = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
        user.setCalendar(this);
    }

    void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setCalendar(this);
    }
}
