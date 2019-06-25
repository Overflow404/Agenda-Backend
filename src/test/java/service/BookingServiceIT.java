package service;


import model.booking.Booking;
import org.junit.*;
import org.mockito.Mock;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.TransactionSynchronizationRegistry;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static model.booking.Booking.SPECIFIC_BOOKING;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookingServiceIT {

    private final static String PERSISTENCE_UNIT = "testUnit";
    private static EntityManagerFactory entityManagerFactory;
    private static Response freeSlot = Response.success(Response.FREE_SLOT);
    private static Response busySlot = Response.failure(Response.BUSY_SLOT);
    private static String subject;
    private static String description;

    private EntityManager entityManager;
    private BookingService bookingService;

    @Mock
    private OverlappingService overlappingService;


    @BeforeClass
    public static void setupClass() {
        subject = "Test subject";
        description = "Test description";
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    @Before
    public void setup() throws ParseException {
        entityManager = entityManagerFactory.createEntityManager();
        bookingService = new BookingService();
        bookingService.entityManager = entityManager;

        overlappingService = mock(OverlappingService.class);
        overlappingService.entityManager = entityManager;

        bookingService.transactionSynchronizationRegistry = mock(TransactionSynchronizationRegistry.class);

        bookingService.overlappingService = overlappingService;
        deleteTables();
        insertSampleSlot();
    }

    @After
    public void teardown() {
        entityManager.close();
    }

    @AfterClass
    public static void teardownClass() {
        entityManagerFactory.close();
    }

    @Test
    public void AfterTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 10:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 13:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(freeSlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertTrue(recordIsInDatabase(booking));
    }

    @Test
    public void StartTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 10:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 14:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(freeSlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertTrue(recordIsInDatabase(booking));
    }

    @Test
    public void StartInsideTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 10:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 14:01:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(busySlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertFalse(recordIsInDatabase(booking));
    }

    @Test
    public void InsideStartTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 20:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(busySlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertFalse(recordIsInDatabase(booking));
    }

    @Test
    public void EnclosingStartTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 15:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(busySlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertFalse(recordIsInDatabase(booking));
    }

    @Test
    public void EnclosingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:30:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 15:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(busySlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertFalse(recordIsInDatabase(booking));
    }

    @Test
    public void EnclosingEndTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:30:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 16:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(busySlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertFalse(recordIsInDatabase(booking));
    }

    @Test
    public void ExactMatchTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 16:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(busySlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertEquals(1, countRecordsInDatabase(booking));
    }

    @Test
    public void InsideTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 13:30:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 16:30:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(busySlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertFalse(recordIsInDatabase(booking));
    }

    @Test
    public void InsideEndTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 13:30:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 16:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(busySlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertFalse(recordIsInDatabase(booking));
    }

    @Test
    public void EndInsideTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:30:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 18:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(busySlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertFalse(recordIsInDatabase(booking));
    }

    @Test
    public void EndTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 16:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 18:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(freeSlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertTrue(recordIsInDatabase(booking));
    }

    @Test
    public void BeforeTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 16:01:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 19:00:00.000000"));

        when(overlappingService.checkIfDatesOverlap(start, end)).thenReturn(freeSlot);

        Booking booking = new Booking(subject, description, start, end);
        bookingService.book(booking);

        Assert.assertTrue(recordIsInDatabase(booking));
    }

    private boolean recordIsInDatabase(Booking booking) {
        List<Booking> bookings = getBookings(booking);
        return !bookings.isEmpty();
    }

    private int countRecordsInDatabase(Booking booking) {
        List<Booking> bookings = getBookings(booking);
        return bookings.size();
    }

    private List<Booking> getBookings(Booking booking) {
        entityManager.getTransaction().begin();

        List<Booking> bookings = entityManager
                .createNamedQuery(SPECIFIC_BOOKING, Booking.class)
                .setParameter("subject", booking.getSubject())
                .setParameter("description", booking.getDescription())
                .setParameter("start", booking.getStart())
                .setParameter("end", booking.getEnd())
                .getResultList();

        entityManager.getTransaction().commit();

        return bookings;
    }

    private long stringToMillis(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date d = sdf.parse(date);
        return d.getTime();
    }

    private void deleteTables() {
        entityManager.getTransaction().begin();

        Query deleteBookings = entityManager.createQuery("DELETE FROM Booking");
        Query deleteUsers = entityManager.createQuery("DELETE FROM User");

        deleteBookings.executeUpdate();
        deleteUsers.executeUpdate();

        entityManager.getTransaction().commit();
    }

    private void insertSampleSlot() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 16:00:00.000000"));

        entityManager.getTransaction().begin();
        Booking booking = new Booking("Test subject", "Test description", start, end);
        entityManager.persist(booking);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

}
