package service.booking;

import config.Configuration;
import model.Booking;
import org.junit.*;
import service.overlapping.OverlappingService;
import utils.TestUtils;
import javax.persistence.*;
import javax.transaction.TransactionSynchronizationRegistry;
import java.sql.Timestamp;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

public class BookingServiceIT {

    private static EntityManagerFactory entityManagerFactory;
    private static String subject;
    private static String description;

    private EntityManager manager;
    private BookingService bookingService;
    private BookingSynchronization synchronization;
    private TestUtils utils = new TestUtils();


    @BeforeClass
    public static void setupClass() {
        subject = "Subject";
        description = "Description";
        entityManagerFactory = Persistence.createEntityManagerFactory(Configuration.TEST_UNIT);
    }

    @Before
    public void setup() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 16:00:00.000000"));
        Booking sampleBooking = new Booking("Sample subject", "Sample description", start, end);

        manager = entityManagerFactory.createEntityManager();
        synchronization = new BookingSynchronization();

        OverlappingService overlappingService = new OverlappingService();
        overlappingService.setManager(manager);

        bookingService = new BookingService();
        bookingService.manager = manager;
        bookingService.registry = mock(TransactionSynchronizationRegistry.class);
        bookingService.overlappingService = overlappingService;


        utils.deleteTables(manager);
        utils.insertSampleSlot(manager, bookingService, sampleBooking);
    }

    @After
    public void teardown() {
        manager.close();
    }

    @AfterClass
    public static void teardownClass() {
        entityManagerFactory.close();
    }

    @Test
    public void AfterTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 10:00:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 13:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(true));
    }

    @Test
    public void StartTouchingTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 10:00:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 14:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(true));
    }

    @Test
    public void StartInsideTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 10:00:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 14:01:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void InsideStartTouchingTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 20:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void EnclosingStartTouchingTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 15:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void EnclosingTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 14:30:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 15:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void EnclosingEndTouchingTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 14:30:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void ExactMatchTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void InsideTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 13:30:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 16:30:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void InsideEndTouchingTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 13:30:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void EndInsideTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 14:30:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 18:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(false));
    }

    @Test
    public void EndTouchingTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 16:00:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 18:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(true));
    }

    @Test
    public void BeforeTest() {
        Timestamp start = new Timestamp(utils.stringToMillis("2019-06-16 16:01:00.000000"));
        Timestamp end = new Timestamp(utils.stringToMillis("2019-06-16 19:00:00.000000"));

        Booking booking = new Booking(subject, description, start, end);
        synchronization.booking = booking;

        bookingService.book(booking);

        Assert.assertThat(utils.bookingIsInDatabase(booking, manager), is(true));
    }

}
