/*
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
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.Date;

import static model.Booking.OVERLAPPING;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestUtils.RANDOM_GROUP;

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

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void datesDoesNotOverlap() {
        Date start = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 19:00:00.000000"));

        TypedQuery<Booking> result = successfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    public void datesOverlap() {
        Date start = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 19:00:00.000000"));

        TypedQuery<Booking> result = unsuccessfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void afterTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 13:00:00.000000"));

        TypedQuery<Booking> result = successfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    public void startTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 14:00:00.000000"));

        TypedQuery<Booking> result = successfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    public void startInsideTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 14:01:00.000000"));

        TypedQuery<Booking> result = unsuccessfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void insideStartTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 20:00:00.000000"));

        TypedQuery<Booking> result = unsuccessfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void enclosingStartTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 15:00:00.000000"));

        TypedQuery<Booking> result = unsuccessfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void enclosingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 15:00:00.000000"));

        TypedQuery<Booking> result = unsuccessfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void enclosingEndTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        TypedQuery<Booking> result = unsuccessfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void exactMatchTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        TypedQuery<Booking> result = unsuccessfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void insideTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 13:30:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 16:30:00.000000"));

        TypedQuery<Booking> result = unsuccessfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void insideEndTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 13:30:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        TypedQuery<Booking> result = unsuccessfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void endInsideTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 18:00:00.000000"));

        TypedQuery<Booking> result = unsuccessfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void endTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 18:00:00.000000"));

        TypedQuery<Booking> result = successfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    public void beforeTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 16:01:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 19:00:00.000000"));

        TypedQuery<Booking> result = successfulMockedQuery(start, end);
        when(manager.createNamedQuery(OVERLAPPING, Booking.class)).thenReturn(result);

        Response response = overlappingService.checkIfDatesOverlap(start, end, RANDOM_GROUP);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @SuppressWarnings("unchecked")
    private TypedQuery<Booking> successfulMockedQuery(Date start, Date end) {
        TypedQuery<Booking> mockedQuery = (TypedQuery<Booking>) mock(TypedQuery.class);
        when(mockedQuery.setParameter("inputStartDate", start)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("inputEndDate", end)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("groupName", RANDOM_GROUP)).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenThrow(NoResultException.class);
        return mockedQuery;
    }

    @SuppressWarnings("unchecked")
    private TypedQuery<Booking> unsuccessfulMockedQuery(Date start, Date end) {
        Booking booking = mock(Booking.class);
        TypedQuery<Booking> mockedQuery = (TypedQuery<Booking>) mock(TypedQuery.class);
        when(mockedQuery.setParameter("inputStartDate", start)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("inputEndDate", end)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("groupName", RANDOM_GROUP)).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(booking);
        return mockedQuery;
    }
}
*/
