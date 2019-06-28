/*
package service.registration;

import model.Calendar;
import model.User;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

import static model.Calendar.GET_CALENDAR_BY_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestUtils.RANDOM_GROUP;

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
    public void registerAnOwnerUser() {
        User user = mock(User.class);

        when(user.getGroupName()).thenReturn(RANDOM_GROUP);
        when(user.isOwner()).thenReturn(true);

        Mockito.doNothing().when(manager).persistCalendar(any());

        Response response = registrationService.register(user);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void registerUserToUnknownGroup() {
        User user = mock(User.class);

        when(user.getGroupName()).thenReturn(RANDOM_GROUP);
        when(user.isOwner()).thenReturn(false);

        TypedQuery<Calendar> mockedQuery = (TypedQuery<Calendar>) mock(TypedQuery.class);
        when(manager.createNamedQuery(GET_CALENDAR_BY_NAME, Calendar.class)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("name", RANDOM_GROUP)).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(NoResultException.class);

        Response response = registrationService.register(user);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void registerUserToKnownGroup() {
        User user = mock(User.class);

        when(user.getGroupName()).thenReturn(RANDOM_GROUP);
        when(user.isOwner()).thenReturn(false);

        Calendar calendar = mock(Calendar.class);

        TypedQuery<Calendar> mockedQuery = (TypedQuery<Calendar>) mock(TypedQuery.class);
        when(manager.createNamedQuery(GET_CALENDAR_BY_NAME, Calendar.class)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("name", RANDOM_GROUP)).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(calendar);

        Response response = registrationService.register(user);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }


    @Test
    public void registerAnAlreadyRegisteredOwnerUser() {
        User user = mock(User.class);

        when(user.getGroupName()).thenReturn(RANDOM_GROUP);
        when(user.isOwner()).thenReturn(true);

        Mockito.doThrow(PersistenceException.class).when(manager).persistCalendar(any());

        Response response = registrationService.register(user);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void registerAnAlreadyRegisteredNonOwnerUser() {
        User user = mock(User.class);

        when(user.getGroupName()).thenReturn(RANDOM_GROUP);
        when(user.isOwner()).thenReturn(false);

        Calendar calendar = mock(Calendar.class);

        TypedQuery<Calendar> mockedQuery = (TypedQuery<Calendar>) mock(TypedQuery.class);
        when(manager.createNamedQuery(GET_CALENDAR_BY_NAME, Calendar.class)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("name", RANDOM_GROUP)).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(calendar);

        Mockito.doThrow(PersistenceException.class).when(calendar).addUser(user);

        Response response = registrationService.register(user);
        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }
}
*/
