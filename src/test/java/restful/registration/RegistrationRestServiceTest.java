package restful.registration;

import model.User;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;
import service.Result;
import service.registration.RegistrationService;
import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegistrationRestServiceTest {

    @Mock
    private RegistrationService mockService;

    private RegistrationRestService restService;

    @Before
    public void setup() {
        restService = new RegistrationRestService();
        mockService = mock(RegistrationService.class);
        Whitebox.setInternalState(restService, RegistrationService.class, mockService);
    }

    @Test
    public void successfulRegistration() {
        when(mockService.register(any())).thenReturn(Result.success("Successful registration!"));

        User user = new User("test", "test","test","test","test","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), Matchers.is(HttpStatus.SC_OK));
    }

    @Test
    public void unsuccessfulRegistration() {
        when(mockService.register(any())).thenReturn(Result.failure("Unsuccessful registration!"));

        User user = new User("test", "test","test","test","test","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), Matchers.is(HttpStatus.SC_PRECONDITION_FAILED));
    }

    @Test
    public void nullUser() {
        Response response = restService.register("test", null);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void nullUsername() {
        User user = new User(null, "test","test","test","test","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void nullLastName() {
        User user = new User("test", null,"test","test","test","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void nullGmt() {
        User user = new User("test", "test",null,"test","test","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void nullEmail() {
        User user = new User("test", "test","test",null,"test","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void nullPassword() {
        User user = new User("test", "test","test","test",null,"test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void nullGroup() {
        User user = new User("test", "test","test","test","test",null,true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptyUsername() {
        User user = new User(" ", "test","test","test","test","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptyLastName() {
        User user = new User("test", " ","test","test","test","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptyGmt() {
        User user = new User("test", "test"," ","test","test","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptyEmail() {
        User user = new User("test", "test","test"," ","test","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptyPassword() {
        User user = new User("test", "test","test","test"," ","test",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptyGroup() {
        User user = new User("test", "test","test","test","test"," ",true);
        Response response = restService.register("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }
}
