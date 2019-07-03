package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@XmlRootElement(name = "user")
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

    private final static Logger logger = LoggerFactory.getLogger(Calendar.class);

    public static final String FIND_USER_BY_EMAIL = "findUserByName";
    public static final String GET_PENDING_LIST = "getPendingList";
    public static final String SPECIFIC_USER = "specificUser";
    public static final String IS_REGISTERED = "isRegistered";
    public static final String GET_USER_BY_MAIL = "getUserByMail";
    public static final String FIND_USER_BY_EMAIL_AND_CALENDAR = "findUserByEmailAndCalendar";

    public User() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String gmt;
    private String email;
    private String password;
    private String groupName;
    private boolean owner;
    private boolean pending;

    @XmlTransient
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Calendar calendar;

    @JsonIgnore
    @XmlTransient
    @OneToMany(mappedBy = "ownerUser", cascade = CascadeType.ALL)
    private List<Pending> requests = new ArrayList<>();

    public User(String firstName, String lastName, String gmt, String email
            , String password, String group, boolean owner) {
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

    public void addPendingRequest(Pending request) {
        requests.add(request);
        request.setOwnerUser(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
