package restful.pending;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;
import service.Result;
import service.pending.PendingService;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PendingRestServiceTest {

    @Mock
    private PendingService mockService;

    private PendingRestService restService;

    @Before
    public void setup() {
        restService = new PendingRestService();
        mockService = mock(PendingService.class);
        Whitebox.setInternalState(restService, PendingService.class, mockService);
    }

    @Test
    public void successfulRetrieveOfMailList() {
        List<String> mails = Arrays.asList("test1@gmail.com", "test2@gmail.com", "test3@gmail.com");
        Result result = Result.success(mails);

        when(mockService.pendingList("test0@gmail.com")).thenReturn(result);

        Response response = restService.pendingList("test", "test0@gmail.com");

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
        Assert.assertThat(response.getEntity(), is(mails));
    }

    @Test
    public void successfulRetrieveOfEmptyMailList() {
        List<String> mails = new ArrayList<>();
        Result result = Result.success(mails);

        when(mockService.pendingList("test0@gmail.com")).thenReturn(result);

        Response response = restService.pendingList("test", "test0@gmail.com");

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
        Assert.assertThat(response.getEntity(), is(mails));
    }


}
