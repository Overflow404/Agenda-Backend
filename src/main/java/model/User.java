package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static model.User.*;

@NamedQueries({
        @NamedQuery(
                name = FIND_USER_BY_EMAIL,
                query = "SELECT c FROM User c WHERE c.email = :email"),
        @NamedQuery(
                name = SPECIFIC_USER,
                query = "SELECT u " +
                        "FROM User u " +
                        "WHERE u.firstName=:firstName and u.lastName=:lastName and" +
                        " u.email=:email and u.gmt=:gmt and u.password=:password"),
        @NamedQuery(
                name = IS_REGISTERED,
                query = "SELECT u " +
                        "FROM User u " +
                        "WHERE u.email=:email and u.password=:password"),
        @NamedQuery(
                name = GET_USER_BY_MAIL,
                query = "SELECT u " +
                        "FROM User u " +
                        "WHERE u.email=:email"),
})

@Entity
@XmlRootElement(name = "user")
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

    public static final String FIND_USER_BY_EMAIL = "findUserByName";
    public static final String SPECIFIC_USER = "specificUser";
    public static final String IS_REGISTERED = "isRegistered";
    public static final String GET_USER_BY_MAIL = "getUserByMail";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private String gmt;
    private String email;
    private String password;
    private String group;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

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
