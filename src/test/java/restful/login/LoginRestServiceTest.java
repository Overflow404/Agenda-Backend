package restful.login;

import model.User;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;
import service.Result;
import service.login.LoginService;

import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginRestServiceTest {

    @Mock
    private LoginService mockService;

    private LoginRestService restService;

    @Before
    public void setup() {
        restService = new LoginRestService();
        mockService = mock(LoginService.class);
        Whitebox.setInternalState(restService, LoginService.class, mockService);
    }

    @Test
    public void successfulLogin() {
        when(mockService.login(any())).thenReturn(Result.success("Successful login!"));

        User user = new User("test", "test","test","test","test","test",true);
        Response response = restService.login("test", user);

        Assert.assertThat(response.getStatus(), Matchers.is(HttpStatus.SC_OK));
    }

    @Test
    public void unsuccessfulLogin() {
        when(mockService.login(any())).thenReturn(Result.failure("Unsuccessful login!"));

        User user = new User("test", "test","test","test","test","test",true);
        Response response = restService.login("test", user);

        Assert.assertThat(response.getStatus(), Matchers.is(HttpStatus.SC_PRECONDITION_FAILED));
    }

    @Test
    public void nullUser() {
        Response response = restService.login("test", null);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void nullEmail() {
        User user = new User("test", "test","test",null,"test","test",true);
        Response response = restService.login("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void nullPassword() {
        User user = new User("test", "test","test","test",null,"test",true);
        Response response = restService.login("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptyEmail() {
        User user = new User("test", "test","test"," ","test","test",true);
        Response response = restService.login("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }

    @Test
    public void emptyPassword() {
        User user = new User("test", "test","test","test"," ","test",true);
        Response response = restService.login("test", user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_PARTIAL_CONTENT));
    }
}
