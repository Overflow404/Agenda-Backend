package service.booking;

import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import service.overlapping.OverlappingService;
import utils.TestUtils;
import javax.persistence.EntityManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookingServiceTest {

    private Timestamp start;
    private Timestamp end;
    private BookingService bookingService;
    private TestUtils utils = new TestUtils();

    @Mock
    private OverlappingService overlappingService;

    @Mock
    private Booking booking;

    @Mock
    private EntityManager manager;

    @Mock
    private TransactionSynchronizationRegistry registry;


    @Before
    public void setup() {
        overlappingService = mock(OverlappingService.class);
        manager = mock(EntityManager.class);
        registry = mock(TransactionSynchronizationRegistry.class);

        bookingService = new BookingService();
        bookingService.manager = manager;
        bookingService.overlappingService = overlappingService;
        bookingService.registry = registry;

        start = new Timestamp(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        end = new Timestamp(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        booking = mock(Booking.class);
        when(booking.getStart()).thenReturn(start);
        when(booking.getEnd()).thenReturn(end);
    }

    @Test
    public void successfulBooking() {
        when(overlappingService.checkIfDatesOverlap(start, end))
                .thenReturn(Response.ok().build());

        Response response = bookingService.book(booking);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    public void unsuccessfulBooking() {
        when(overlappingService.checkIfDatesOverlap(start, end))
                .thenReturn(Response.status(Response.Status.CONFLICT).build());

        Response response = bookingService.book(booking);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

}
