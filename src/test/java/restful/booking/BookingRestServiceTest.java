package restful.booking;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;
import service.Result;
import service.booking.BookingService;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static service.Helper.stringToMillis;

public class BookingRestServiceTest {

    @Mock
    private BookingService mockBookingService;

    private BookingRestService restService;

    @Before
    public void setup() {
        restService = new BookingRestService();
        mockBookingService = mock(BookingService.class);

        Whitebox.setInternalState(restService, BookingService.class, mockBookingService);
    }

    @Test
    public void nullSubject() {
        Response response = restService.book("test", null, null, 1, 1, "test");

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptySubject() {
        Response response = restService.book("test", " ", null, 1, 1, "test");

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void failedBookingCauseOverlapping() {
        long start = stringToMillis("2019-06-16 10:00:00.000000");
        long end = stringToMillis("2019-06-16 13:00:00.000000");

        when(mockBookingService.book(any(), anyString())).thenReturn(Result.failure("Overlapping!"));

        Response response = restService.book("test", "subject", "description", start, end, "test");

        Assert.assertThat(response.getStatus(), Matchers.is(HttpStatus.SC_PRECONDITION_FAILED));
    }

    @Test
    public void successfulBooking() {
        long start = stringToMillis("2019-06-16 10:00:00.000000");
        long end = stringToMillis("2019-06-16 13:00:00.000000");

        when(mockBookingService.book(any(), anyString())).thenReturn(Result.success("Booking confirmed!"));

        Response response = restService.book("test", "subject", "description", start, end, "test");

        Assert.assertThat(response.getStatus(), Matchers.is(HttpStatus.SC_OK));
    }
}
