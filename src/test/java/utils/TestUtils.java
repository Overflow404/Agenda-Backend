package utils;

import model.Booking;
import model.User;
import service.booking.BookingService;
import service.registration.RegistrationService;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static model.Booking.SPECIFIC_BOOKING;
import static model.User.SPECIFIC_USER;

public class TestUtils {

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

    public boolean userIsInDatabase(User user, EntityManager manager) {
        return getUser(user, manager) != null;
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

        deleteBookings.executeUpdate();
        deleteUsers.executeUpdate();

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
        registrationService.register(new User("", "","","test",""));
    }
}
