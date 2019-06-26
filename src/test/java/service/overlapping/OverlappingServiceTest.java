package service.overlapping;

import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import utils.TestUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.Date;

import static model.Booking.OVERLAPPING;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OverlappingServiceTest {

    private TestUtils utils = new TestUtils();
    private OverlappingService overlappingService;

    @Mock
    private EntityManager manager;

    @Before
    public void setup() {
        manager = mock(EntityManager.class);

        overlappingService = new OverlappingService();
        overlappingService.setManager(manager);
    }

    @Test
    public void endTimeGreaterThanStartTime() {
        Date start = new Date(utils.stringToMillis("2019-06-16 19:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void datesDoesNotOverlap() {
        Date start = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 19:00:00.000000"));

        TypedQuery<Booking> mockedQuery = (TypedQuery<Booking>) mock(TypedQuery.class);
        when(mockedQuery.setParameter("inputStartDate", start)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("inputEndDate", end)).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(NoResultException.class);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(mockedQuery);

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void datesOverlap() {
        Date start = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 19:00:00.000000"));

        Booking booking = mock(Booking.class);

        TypedQuery<Booking> mockedQuery = (TypedQuery<Booking>) mock(TypedQuery.class);
        when(mockedQuery.setParameter("inputStartDate", start)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("inputEndDate", end)).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(booking);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(mockedQuery);

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }
}
