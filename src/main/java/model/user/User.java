package model.user;

import model.booking.Booking;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

@Entity
@XmlRootElement(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Booking> bookings = new HashSet<Booking>();

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setUser(this);
    }
}
