package model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement(name = "user")
@Getter @Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

    public static final String FIND_USER_BY_EMAIL = "findUserByName";
    public static final String SPECIFIC_USER = "specificUser";
    public static final String IS_REGISTERED = "isRegistered";
    public static final String GET_USER_BY_MAIL = "getUserByMail";
    public static final String FIND_USER_BY_EMAIL_AND_CALENDAR = "findUserByEmailAndCalendar";

    public User() {

    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String gmt;
    private String email;
    private String password;
    private String groupName;
    private boolean owner;

    @XmlTransient
    @ManyToOne
    @JoinColumn
    private Calendar calendar;

    public User(String firstName, String lastName, String gmt, String email, String password, String group, boolean owner) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gmt = gmt;
        this.email = email;
        this.password = password;
        this.groupName = group;
        this.owner = owner;
    }

    void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void addBooking(Booking booking) {
        calendar.addBooking(booking);
    }

}
