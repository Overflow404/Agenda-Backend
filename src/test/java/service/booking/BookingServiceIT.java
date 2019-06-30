package service.booking;

import config.Configuration;
import dao.Dao;
import model.Booking;
import org.junit.*;
import service.Helper;
import service.overlapping.OverlappingService;
import javax.persistence.*;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static service.Helper.*;

public class BookingServiceIT {

    private static Dao dao;
    private static EntityManager manager;
    private static BookingService bService;
    private static EntityManagerFactory entityManagerFactory;
    private static Helper helper;

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(Configuration.TEST_UNIT);
    }

    @Before
    public void setup() {
        manager = entityManagerFactory.createEntityManager();

        dao = new Dao(manager);
        OverlappingService oService = new OverlappingService(dao);
        bService = new BookingService(oService, dao);
        helper = new Helper();

        dao.start();
        helper.deleteTables(manager, dao);
        helper.registerSampleUser(dao);
        helper.insertSampleSlot(dao, oService);
    }

    @After
    public void tearDown() {
        dao.commit();
        manager.close();
    }

    @AfterClass
    public static void tearDownClass() {
        entityManagerFactory.close();
    }

    @Test
    public void AfterTest() {
        Date start = new Date(stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 13:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(true));
    }

    @Test
    public void StartTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 14:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(true));
    }

    @Test
    public void StartInsideTest() {
        Date start = new Date(stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 14:01:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void InsideStartTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 20:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void EnclosingStartTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 15:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void EnclosingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 15:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void EnclosingEndTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void ExactMatchTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertNotSame(helper.getBooking(booking, manager), is(booking));

    }

    @Test
    public void InsideTest() {
        Date start = new Date(stringToMillis("2019-06-16 13:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:30:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void InsideEndTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 13:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void EndInsideTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 18:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void EndTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 16:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 18:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(true));
    }

    @Test
    public void BeforeTest() {
        Date start = new Date(stringToMillis("2019-06-16 16:01:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 19:00:00.000000"));

        Booking booking = new Booking(testSubject, testDescription, start, end);

        bService.book(booking, testEmail);

        Assert.assertThat(helper.bookingIsInDatabase(booking, manager), is(true));
    }

}
