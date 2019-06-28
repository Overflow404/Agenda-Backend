/*
package service.booking;

import model.Booking;
import model.User;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import service.overlapping.OverlappingService;
import utils.TestUtils;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.Date;

import static model.User.GET_USER_BY_MAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static utils.TestUtils.RANDOM_GROUP;
import static utils.TestUtils.RANDOM_MAIL;

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
        bookingService.service = overlappingService;
        bookingService.registry = registry;

        start = new Timestamp(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        end = new Timestamp(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        booking = mock(Booking.class);
        when(booking.getStart()).thenReturn(start);
        when(booking.getEnd()).thenReturn(end);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void successfulBooking() {
        Mockito.doNothing().when(bookingService.registry).registerInterposedSynchronization(any());

        when(overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP))
                .thenReturn(Response.ok().build());

        doReturn(HttpStatus.SC_OK).when(overlappingService).checkIfDatesOverlap(start, end, RANDOM_GROUP).getStatus();

        User user = mock(User.class);
        when(user.getEmail()).thenReturn(RANDOM_MAIL);

        Booking booking = mock(Booking.class);
        when(booking.getStart()).thenReturn(new Date());

        TypedQuery<User> mockedQuery = (TypedQuery<User>) mock(TypedQuery.class);
        when(manager.createNamedQuery(GET_USER_BY_MAIL, User.class)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("email", RANDOM_MAIL)).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(user);

        bookingService.book(booking, RANDOM_MAIL);
    }




}
*/
