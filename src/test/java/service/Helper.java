package service;

import dao.Dao;
import model.Booking;
import model.User;
import service.booking.BookingService;
import service.overlapping.OverlappingService;
import service.registration.RegistrationService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static model.Booking.SPECIFIC_BOOKING;

@SuppressWarnings("WeakerAccess")
public class Helper {

    public static String testSubject = "testSubject";
    public static String testDescription = "testSubject";
    public static Date testStart = new Date(stringToMillis("2019-06-16 14:00:00.000000"));
    public static Date testEnd = new Date(stringToMillis("2019-06-16 16:00:00.000000"));

    public static String testFirstName = "testFirstName";
    public static String testLastName = "testLastName";
    public static String testGmt = "testGmt";
    public static String testEmail = "testEmail";
    public static String testPassword = "testPassword";
    public static String testGroup = "testGroup";

    public static Result successResult = Result.success("Success!");
    public static Result failureResult = Result.failure("Falire!");


    public static Booking testBooking = new Booking(testSubject, testDescription, testStart, testEnd);
    public static User testOwnerUser = new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, true);
    public static User testNonOwnerUser = new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, false);

    public static Long stringToMillis(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date d = sdf.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            System.err.println("Error in stringToMillis during date parsing!");
        }
        return 1L;
    }

    public static User getNewOwnerUser() {
        return new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, true);
    }

    public static User getNewNonOwnerUser() {
        return new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, false);
    }

    public void deleteTables(EntityManager manager, Dao dao) {
        Query deleteBookings = manager.createQuery("DELETE FROM Booking");
        Query deletePendings = manager.createQuery("DELETE FROM Pending");
        Query deleteUsers = manager.createQuery("DELETE FROM User");
        Query deleteCalendar = manager.createQuery("DELETE FROM Calendar");

        deletePendings.executeUpdate();
        deleteBookings.executeUpdate();
        deleteUsers.executeUpdate();
        deleteCalendar.executeUpdate();
    }

    public void insertTestBooking(Dao dao, OverlappingService oService) {
        BookingService bService = new BookingService(oService, dao);
        Booking booking = new Booking(testSubject, testDescription, testStart, testEnd);
        bService.book(booking, testEmail);
    }


    public void registerTestUser(Dao dao) {
        RegistrationService registrationService = new RegistrationService(dao);
        User user = new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, true);
        registrationService.register(user);
    }

    public boolean bookingIsInDatabase(Booking booking, EntityManager manager) {
        return getBooking(booking, manager) != null;
    }

    public Booking getBooking(Booking booking, EntityManager manager) {
        try {
            return manager
                    .createNamedQuery(SPECIFIC_BOOKING, Booking.class)
                    .setParameter("subject", booking.getSubject())
                    .setParameter("description", booking.getDescription())
                    .setParameter("start", booking.getStart())
                    .setParameter("end", booking.getEnd())
                    .setParameter("email", testEmail)
                    .getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }

}

