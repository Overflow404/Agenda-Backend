package service;

import model.booking.Booking;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;


public class OverlappingServiceIT {

    private static EntityManagerFactory entityManagerFactory;
    private final static String PERSISTENCE_UNIT = "testUnit";
    private EntityManager entityManager;
    private OverlappingService overlappingService;

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    @Before
    public void setup() throws ParseException {
        entityManager = entityManagerFactory.createEntityManager();
        overlappingService = new OverlappingService();
        overlappingService.entityManager = entityManager;
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
    public void afterTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 10:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 13:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        //Assert.assertTrue(result.success());
        Assert.assertThat(result.success(), is(true));
    }

    @Test
    public void startTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 10:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 14:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertTrue(result.success());
    }

    @Test
    public void startInsideTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 10:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 14:01:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertFalse(result.success());
    }

    @Test
    public void insideStartTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 20:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertFalse(result.success());
    }

    @Test
    public void enclosingStartTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 15:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertFalse(result.success());
    }

    @Test
    public void enclosingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:30:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 15:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertFalse(result.success());
    }

    @Test
    public void enclosingEndTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:30:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 16:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertFalse(result.success());
    }

    @Test
    public void exactMatchTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 16:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertFalse(result.success());
    }

    @Test
    public void insideTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 13:30:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 16:30:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertFalse(result.success());
    }

    @Test
    public void insideEndTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 13:30:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 16:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertFalse(result.success());
    }

    @Test
    public void endInsideTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 14:30:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 18:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertFalse(result.success());
    }

    @Test
    public void endTouchingTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 16:00:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 18:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertTrue(result.success());
    }

    @Test
    public void beforeTest() throws ParseException {
        Timestamp start = new Timestamp(stringToMillis("2019-06-16 16:01:00.000000"));
        Timestamp end = new Timestamp(stringToMillis("2019-06-16 19:00:00.000000"));

        Response result = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertTrue(result.success());
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
