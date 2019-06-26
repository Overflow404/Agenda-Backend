package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

import static model.User.FIND_USER_BY_EMAIL;
import static model.User.SPECIFIC_USER;

@NamedQueries({
        @NamedQuery(
                name = FIND_USER_BY_EMAIL,
                query = "SELECT c FROM User c WHERE c.email = :email"),
        @NamedQuery(
                name = SPECIFIC_USER,
                query = "SELECT u " +
                        "FROM User u " +
                        "WHERE u.firstName=:firstName and u.lastName=:lastName and" +
                        " u.email=:email and u.gmt=:gmt and u.password=:password")
})

@Entity
@XmlRootElement(name = "user")
@Getter
@Setter
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

    public static final String FIND_USER_BY_EMAIL = "findUserByName";
    public static final String SPECIFIC_USER = "specificUser";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private String gmt;
    private String email;
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Booking> bookings = new HashSet<>();

    public User(String firstName, String lastName, String gmt, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gmt = gmt;
        this.email = email;
        this.password = password;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setUser(this);
    }

    public User() {

    }

}
