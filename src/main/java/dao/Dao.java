package dao;

import config.Configuration;
import model.Booking;
import model.Calendar;
import model.Pending;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static model.Booking.*;
import static model.Calendar.GET_CALENDAR_BY_NAME;
import static model.Pending.REMOVE;
import static model.User.*;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Dao {

    private final static Logger logger = LoggerFactory.getLogger(Dao.class);

    @PersistenceContext(unitName = Configuration.UNIT)
    EntityManager manager;

    public Dao(EntityManager manager) {
        this.manager = manager;
    }

    public Dao() {

    }

    public User getUserByMail(String email) {
        logger.debug("Retrieving user from mail: " + email);
        return manager
                .createNamedQuery(GET_USER_BY_MAIL, User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public void addBookingToUser(Booking booking, User user) {
        logger.debug("Adding booking " + booking + " to user " + user);
        user.addBooking(booking);
    }

    public Result checkOverlap(Date start, Date end, String calendarName) {
        try {
            manager
                    .createNamedQuery(OVERLAPPING, Booking.class)
                    .setParameter("inputStartDate", start)
                    .setParameter("inputEndDate", end)
                    .setParameter("groupName", calendarName)
                    .getSingleResult();
            logger.debug("Check if " + start + " overlap with " + end + " in calendar " + calendarName);
        } catch (NoResultException e) {
            logger.debug("No overlapping of " + start + " overlap with " + end + " in calendar " + calendarName);
            return Result.success("This time slots does not overlap!");
        }
        logger.debug("Overlapping of " + start + " overlap with " + end + " in calendar " + calendarName);
        return Result.failure("This time slots overlap!");
    }

    public List<Booking> retrieveBookingsFrom(int day, int month, int year, String email) {
        logger.debug("Retrieve bookings for " + day + "/" + month + "/" + year + " for " + email);
        return manager
                .createNamedQuery(GET_BOOKINGS_FROM_CALENDAR_AND_DATE_AND_USER, Booking.class)
                .setParameter("inputDay", day)
                .setParameter("inputMonth", month)
                .setParameter("inputYear", year)
                .setParameter("email", email)
                .getResultList();
    }

    public Calendar retrieveCalendar(String group) {
        return manager
                .createNamedQuery(GET_CALENDAR_BY_NAME, Calendar.class)
                .setParameter("name", group)
                .getSingleResult();

    }

    public Result persistToNewCalendar(String calendarName, User user) {
        try {
            user.setPending(false);
            Calendar calendar = new Calendar(calendarName);
            calendar.addUser(user);
            manager.persist(calendar);
        } catch (PersistenceException e) {
            logger.info("Already registered!");
            return Result.failure("Already registered!");
        }
        logger.info("Registration successful!");
        return Result.success("Registration successful!");
    }

    public Result persistToAlreadyCalendar(String calendarName, User user) {
        try {
            user.setPending(true);
            Calendar calendar = this.retrieveCalendar(calendarName);
            calendar.addUser(user);
            manager.persist(calendar);

            User calendarOwner = this.retrieveCalendarOwner(calendarName);
            Pending request = new Pending();
            request.setWaiterMail(user.getEmail());
            user.addPendingRequest(request);
            request.setOwnerUser(calendarOwner);
            request.setWaiterMail(user.getEmail());
        } catch (PersistenceException e) {
            logger.info("Already registered!");
            return Result.failure("Already registered!");
        }
        logger.info("Registration successful!");
        return Result.success("Registration successful!");
    }

    private User retrieveCalendarOwner(String calendarName) {
        Calendar calendar = this.retrieveCalendar(calendarName);
        Optional<User> owner = calendar.getOwner();
        return owner.get();//TODO
    }

    public boolean groupExist(String group) {
        try {
            manager
                    .createNamedQuery(GET_CALENDAR_BY_NAME, Calendar.class)
                    .setParameter("name", group)
                    .getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    public Optional<User> verifyUserRegistered(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        try {
            return Optional.of(manager
                    .createNamedQuery(IS_REGISTERED, User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }

    }

    public void start() {
        manager.getTransaction().begin();
    }

    public void commit() {
        manager.getTransaction().commit();
    }

    public List<Pending> getPendingList(String email) {
        return manager
                .createNamedQuery(GET_PENDING_LIST, Pending.class)
                .setParameter("email", email)
                .getResultList();
    }

    public void removeFrom(String ownerEmail, String waiterEmail) {
        int rows = manager
                .createNamedQuery(REMOVE)
                .setParameter("email", waiterEmail)
                .executeUpdate();
        System.out.println("Modified -> " + rows);
    }

    public void setAccepted(String mail) {
        User user = this.getUserByMail(mail);
        user.setPending(false);
    }

    public Result getNumberOfPendingsFor(String ownerEmail) {
        List<Pending> requests =  this.getPendingList(ownerEmail);
        int count = requests.size();
        return Result.success(count);
    }
}
