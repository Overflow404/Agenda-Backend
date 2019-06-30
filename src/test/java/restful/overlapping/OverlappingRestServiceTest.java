package restful.overlapping;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;
import service.Result;
import service.overlapping.OverlappingService;
import javax.ws.rs.core.Response;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static service.Helper.stringToMillis;

public class OverlappingRestServiceTest {

    @Mock
    private OverlappingService mockService;

    private OverlappingRestService restService;

    @Before
    public void setup() {
        restService = new OverlappingRestService();
        mockService = mock(OverlappingService.class);

        Whitebox.setInternalState(restService, OverlappingService.class, mockService);
    }

    @After
    public void teardown() {

    }

    @Test
    public void endTimeGreaterThanStartTime() {
        long start = stringToMillis("2019-06-16 10:00:00.000000");
        long end = stringToMillis("2019-06-16 13:00:00.000000");
        Response response = restService.overlap("test", end, start, "test");

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void twoDatesOverlap() {
        long start = stringToMillis("2019-06-16 10:00:00.000000");
        long end = stringToMillis("2019-06-16 13:00:00.000000");
        String group = "testGroup";

        when(mockService.checkIfDatesOverlap(new Date(start), new Date(end), group))
                .thenReturn(Result.failure("Overlapping!"));

        Response response = restService.overlap("test", start, end, group);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PRECONDITION_FAILED));
    }

    @Test
    public void twoDatesDoesNotOverlap() {
        long start = stringToMillis("2019-06-16 10:00:00.000000");
        long end = stringToMillis("2019-06-16 13:00:00.000000");
        String group = "testGroup";

        when(mockService.checkIfDatesOverlap(new Date(start), new Date(end), group))
                .thenReturn(Result.success("No overlapping!"));

        Response response = restService.overlap("test", start, end, group);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }
}
