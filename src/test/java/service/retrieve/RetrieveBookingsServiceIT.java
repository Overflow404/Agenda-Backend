/*
package service.retrieve;

import config.Configuration;
import model.Booking;
import org.junit.*;
import service.booking.BookingService;
import service.overlapping.OverlappingService;
import utils.TestUtils;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;

public class RetrieveBookingsServiceIT {
    private static EntityManagerFactory entityManagerFactory;

    private EntityManager manager;
    private RetrieveBookingsService retrieveBookingsService;
    private TestUtils utils = new TestUtils();
    private BookingService bookingService;

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(Configuration.TEST_UNIT);
    }

    @Before
    public void setup() {
        manager = entityManagerFactory.createEntityManager();

        OverlappingService overlappingService = new OverlappingService();
        overlappingService.setManager(manager);

        bookingService = new BookingService();
        bookingService.setManager(manager);
        bookingService.setService(overlappingService);
        bookingService.setRegistry(mock(TransactionSynchronizationRegistry.class));

        retrieveBookingsService = new RetrieveBookingsService();
        retrieveBookingsService.manager = manager;

        utils.deleteTables(manager);
        utils.registerSampleUser(manager);
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
    @SuppressWarnings("unchecked")
    public void retrieve() {
        int day = 16;
        int month = 6;
        int year = 2019;

        Timestamp start1 = new Timestamp(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Timestamp end1 = new Timestamp(utils.stringToMillis("2019-06-16 16:00:00.000000"));
        Booking booking1 = new Booking("First subject", "First description", start1, end1);

        Timestamp start2 = new Timestamp(utils.stringToMillis("2019-06-16 21:00:00.000000"));
        Timestamp end2 = new Timestamp(utils.stringToMillis("2019-06-16 23:00:00.000000"));
        Booking booking2 = new Booking("Second subject", "Second description", start2, end2);

        utils.insertSampleSlot(manager, bookingService, booking1);
        utils.insertSampleSlot(manager, bookingService, booking2);

        Response response = retrieveBookingsService.retrieve(day, month, year, "test");
        List<Booking> bookings = (List<Booking>)(response.getEntity());

        Assert.assertThat(bookings.get(0).equals(booking1), is(true));
        Assert.assertThat(bookings.get(1).equals(booking2), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveReturnEmptyList() {
        Response response = retrieveBookingsService.retrieve(16, 6, 2019, "test");
        Assert.assertThat(((List<Booking>) (response.getEntity())).isEmpty(), is(true));
    }


}
*/
