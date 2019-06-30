package service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.Dao;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.Result;
import service.auth.AuthService;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static service.Helper.testOwnerUser;

public class LoginServiceTest {

    private static Dao dao;
    private static LoginService rService;
    private static AuthService authService;

    @BeforeClass
    public static void setupClass() {
        dao = mock(Dao.class);
        authService = mock(AuthService.class);
        rService = new LoginService(authService, dao);
    }

    @Test
    public void nonRegisteredUserAttemptToLogin() {
        when(dao.verifyUserRegistered(testOwnerUser)).thenReturn(Optional.empty());

        Result result = rService.login(testOwnerUser);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void registeredUserAttemptToLogin() throws JsonProcessingException {
        ObjectNode jsonJwt = mock(ObjectNode.class);

        when(dao.verifyUserRegistered(testOwnerUser)).thenReturn(Optional.of(testOwnerUser));
        when(authService.createInfo(testOwnerUser)).thenReturn(jsonJwt);

        Result result = rService.login(testOwnerUser);

        Assert.assertThat(result.success(), is(true));
        Assert.assertThat(result.getContent(), is(jsonJwt));
    }
}
