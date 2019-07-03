package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@XmlRootElement(name = "calendar")
@Getter
@Setter
@ToString
public class Calendar {

    private final static Logger logger = LoggerFactory.getLogger(Calendar.class);
    public static final String GET_CALENDAR_BY_NAME = "getCalendarByName";

    public Calendar() {

    }

    public Calendar(String calendarName) {
        this.calendarName = calendarName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String calendarName;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Booking> bookings = new ArrayList<>();


    public boolean addUser(User user) {
        if (users.contains(user)) {
            return false;
        }
        users.add(user);
        user.setCalendar(this);
        return true;
    }

    void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setCalendar(this);
    }

    public Optional<User> getOwner() {
        for(User user: users) {
            if (user.isOwner()) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }
}
