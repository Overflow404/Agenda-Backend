package restful.booking;

import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import service.booking.BookingService;
import javax.ws.rs.core.Response;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookingRestServiceTest {

    private BookingRestService service;

    @Before
    public void setup() {
        service = new BookingRestService();
    }

    @Test
    public void nullSubject() {
        Response response = service.book(null, "description", 1, 1);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptySubject() {
        Response response = service.book("", "description", 1, 1);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

/*    @Test
    public void successfulBooking() {
        BookingService bookingService = mock(BookingService.class);

        Booking booking = mock(Booking.class);
        when(booking.getStart()).thenReturn(new Date());
        when(booking.getEnd()).thenReturn(new Date());
        when(booking.getSubject()).thenReturn("Subject");
        when(booking.getDescription()).thenReturn("Description");

        Whitebox.setInternalState(service, "bookingService", bookingService);
        when(bookingService.book(any(Booking.class))).thenReturn(Response.ok().build());

        Response response = service.book(booking.getSubject(), booking.getDescription(),
                booking.getStart().getTime(),booking.getEnd().getTime());

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }*/
}
