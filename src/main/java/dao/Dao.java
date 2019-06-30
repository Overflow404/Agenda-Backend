package dao;

import config.Configuration;
import model.Booking;
import model.Calendar;
import model.User;
import service.Result;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static model.Booking.GET_BOOKINGS_FROM_CALENDAR_AND_DATE_AND_USER;
import static model.Booking.OVERLAPPING;
import static model.Calendar.GET_CALENDAR_BY_NAME;
import static model.User.GET_USER_BY_MAIL;
import static model.User.IS_REGISTERED;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Dao {

    @PersistenceContext(unitName = Configuration.UNIT)
    EntityManager manager;

    public Dao(EntityManager manager) {
        this.manager = manager;
    }

    public Dao() {

    }

    public User getUserByMail(String email) {
        return manager
                .createNamedQuery(GET_USER_BY_MAIL, User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public void addBookingToUser(Booking booking, User user) {
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
        } catch (NoResultException e) {
            return Result.success("This time slots does not overlap!");
        }
        return Result.failure("This time slots overlap!");
    }

    public List<Booking> retrieveBookingsFrom(int day, int month, int year, String email) {
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

    public Result persistCalendar(Calendar calendar) {
        try {
            manager.persist(calendar);
        } catch (PersistenceException e) {
            return Result.failure("Already registered!");
        }
        return Result.success("Registration successful!");
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

    public void flush() {
        manager.flush();
    }

    public void clear() {
        manager.clear();
    }
}
