package service.registration;

import model.User;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;

public class RegistrationServiceTest {

    @Mock
    private EntityManager manager;

    private RegistrationService registrationService;

    @Before
    public void setup() {
        manager = mock(EntityManager.class);

        registrationService = new RegistrationService();
        registrationService.manager = manager;
    }

    @Test
    public void successfulRegistration() {
        User user = mock(User.class);
        Mockito.doNothing().when(manager).persist(user);

        Response response = registrationService.register(user);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    public void alreadyRegistered() {
        User user = mock(User.class);
        Mockito.doThrow(new PersistenceException()).when(manager).persist(user);

        Response response = registrationService.register(user);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }
}
