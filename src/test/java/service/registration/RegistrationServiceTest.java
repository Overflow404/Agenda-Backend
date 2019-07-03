package service.registration;

import dao.Dao;
import model.Calendar;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.Result;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static service.Helper.*;

public class RegistrationServiceTest {
    private static Dao dao;
    private static RegistrationService rService;

    @BeforeClass
    public static void setupClass() {
        dao = mock(Dao.class);
        rService = new RegistrationService(dao);
    }

    @Test
    public void attemptToOwnAnotherGroup() {
        when(dao.groupExist(testGroup)).thenReturn(true);

        Result result = rService.register(testOwnerUser);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void successfulOwnerRegistration() {
        when(dao.groupExist(testGroup)).thenReturn(false);
        when(dao.persistToNewCalendar(any(), any())).thenReturn(successResult);

        Result result = rService.register(testOwnerUser);

        Assert.assertThat(result.success(), is(true));
    }

    @Test
    public void alreadyRegisteredOwner() {
        when(dao.groupExist(testGroup)).thenReturn(false);
        when(dao.persistToNewCalendar(any(), any())).thenReturn(failureResult);

        Result result = rService.register(testOwnerUser);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void alreadyRegisteredNonOwner() {
        when(dao.groupExist(testGroup)).thenReturn(false);
        when(dao.persistToNewCalendar(any(), any())).thenReturn(failureResult);

        Result result = rService.register(testNonOwnerUser);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void registerNonOwnerToInexistentGroup() {
        when(dao.groupExist(testGroup)).thenReturn(false);

        Result result = rService.register(testNonOwnerUser);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void registerNonOwnerToExistentGroup() {
        Calendar calendar = new Calendar();
        when(dao.groupExist(testGroup)).thenReturn(true);
        when(dao.retrieveCalendar(testGroup)).thenReturn(calendar);
        when(dao.persistToAlreadyCalendar(any(), any())).thenReturn(Result.failure("Already registered!"));

        Result result = rService.register(testNonOwnerUser);

        Assert.assertThat(result.success(), is(false));
    }

}
