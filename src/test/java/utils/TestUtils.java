/*
package utils;

import model.Booking;
import model.Calendar;
import model.User;
import service.booking.BookingService;
import service.registration.RegistrationService;
import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static model.Booking.SPECIFIC_BOOKING;
import static model.User.FIND_USER_BY_EMAIL_AND_CALENDAR;
import static model.User.SPECIFIC_USER;

public class TestUtils {

    public final static String RANDOM_GROUP = "randomGroup";
    public final static String RANDOM_MAIL = "randomMail";

    public Long stringToMillis(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date d = sdf.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            System.err.println("Error in stringToMillis during date parsing!");
            return null;
        }
    }

    public boolean bookingIsInDatabase(Booking booking, EntityManager manager) {
        return getBooking(booking, manager) != null;
    }

    private Booking getBooking(Booking booking, EntityManager manager) {
        try {
            manager.getTransaction().begin();
            Booking b = manager
                    .createNamedQuery(SPECIFIC_BOOKING, Booking.class)
                    .setParameter("subject", booking.getSubject())
                    .setParameter("description", booking.getDescription())
                    .setParameter("start", booking.getStart())
                    .setParameter("end", booking.getEnd())
                    .setParameter("email", "test")
                    .getSingleResult();
            manager.getTransaction().commit();
            return b;
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }


    private User getUser(User user, EntityManager manager) {
        try {
            manager.getTransaction().begin();
            User u = manager
                    .createNamedQuery(SPECIFIC_USER, User.class)
                    .setParameter("firstName", user.getFirstName())
                    .setParameter("lastName", user.getLastName())
                    .setParameter("email", user.getEmail())
                    .setParameter("gmt", user.getGmt())
                    .setParameter("password", user.getPassword())
                    .getSingleResult();
            manager.getTransaction().commit();
            return u;
        } catch (PersistenceException e) {
            return null;
        }
    }

    public void deleteTables(EntityManager manager) {
        manager.getTransaction().begin();

        Query deleteBookings = manager.createQuery("DELETE FROM Booking");
        Query deleteUsers = manager.createQuery("DELETE FROM User");
        Query deleteCalendar = manager.createQuery("DELETE FROM Calendar");

        deleteBookings.executeUpdate();
        deleteUsers.executeUpdate();
        deleteCalendar.executeUpdate();

        manager.getTransaction().commit();
    }

    public void insertSampleSlot(EntityManager manager, BookingService service, Booking booking) {
        manager.getTransaction().begin();
        service.book(booking, "test");
        manager.getTransaction().commit();
    }


    public void registerSampleUser(EntityManager manager) {
        RegistrationService registrationService = new RegistrationService();
        registrationService.setManager(manager);
        registrationService.register(new User("", "","","test","", "", true));
    }


    public Calendar createSampleCalendar(EntityManager manager) {
        manager.getTransaction().begin();
        Calendar calendar = new Calendar(RANDOM_GROUP);
        manager.persistCalendar(calendar);
        manager.getTransaction().commit();

        return calendar;
    }

    public User findUserByCalendarAndEmail(String group, String email, EntityManager manager) {
        manager.getTransaction().begin();
        User u = manager
                .createNamedQuery(FIND_USER_BY_EMAIL_AND_CALENDAR, User.class)
                .setParameter("mail", email)
                .setParameter("name", group)
                .getSingleResult();
        manager.getTransaction().commit();
        return u;
    }
}
*/
