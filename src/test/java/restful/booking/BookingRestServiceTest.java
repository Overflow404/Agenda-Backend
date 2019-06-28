package restful.booking;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import service.AuthService;
import javax.ws.rs.core.Response;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class BookingRestServiceTest {

    private BookingRestService service;

    @Before
    public void setup() {
        service = new BookingRestService();
    }

    @Test
    public void nullSubject() {
        Response response = service.book(null,null, "description", 1, 1);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptySubject() {
        Response response = service.book(null,"", "description", 1, 1);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

/*    @Test
    @PrepareForTest( { AuthService.class })
    public void bookIfNotAuthenticated() {

        PowerMockito.mockStatic(AuthService.class);
        when(AuthService.isAuthenticated(null)).thenReturn(true);

        Response response = service.book(null,"subject", "description", 1, 1);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_UNAUTHORIZED));

    }*/

}
