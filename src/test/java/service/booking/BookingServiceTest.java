package service.booking;

import model.Booking;
import org.junit.Before;
import org.mockito.Mock;
import service.overlapping.OverlappingService;
import utils.TestUtils;
import javax.persistence.EntityManager;
import javax.transaction.TransactionSynchronizationRegistry;
import java.sql.Timestamp;
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

/*    @Test
    @PrepareForTest(BookingService.class)
    public void bookingAfterOverlappingVerify() throws Exception {
        when(overlappingService.checkIfDatesOverlap(start, end))
                .thenReturn(Response.ok().build());

        User user = mock(User.class);

        BookingService spy = PowerMockito.spy(new BookingService());
        PowerMockito.doReturn(user)
                .when(spy, method(BookingService.class, "getUserByMail", String.class))
                .withArguments("");

        Mockito.doThrow(new NoResultException()).when(user).addBooking(any());

        Response response = bookingService.book(booking, "");
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }*/



}
