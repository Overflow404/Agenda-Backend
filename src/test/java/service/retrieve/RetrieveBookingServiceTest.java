/*
package service.retrieve;

import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static model.Booking.GET_BOOKINGS_FROM_CALENDAR_AND_DATE_AND_USER;
import static model.Booking.GET_BOOKING_FROM_CALENDAR_AND_DATE;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RetrieveBookingServiceTest {

    @Mock
    private EntityManager manager;

    private RetrieveBookingsService retrieveBookingsService;

    @Before
    public void setup() {
        manager = mock(EntityManager.class);

        retrieveBookingsService = new RetrieveBookingsService();
        retrieveBookingsService.manager = manager;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieveReturnEmptyList() {
        int day = 6;
        int month = 12;
        int year = 1996;

        TypedQuery<Booking> mockedQuery = (TypedQuery<Booking>) mock(TypedQuery.class);
        when(manager.createNamedQuery(GET_BOOKINGS_FROM_CALENDAR_AND_DATE_AND_USER, Booking.class))
                .thenReturn(mockedQuery);
        when(mockedQuery.setParameter("inputDay", day)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("inputMonth", month)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("inputYear", year)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("email", "test")).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(new ArrayList<>());
        when(manager.createNamedQuery(GET_BOOKING_FROM_CALENDAR_AND_DATE, Booking.class)).thenReturn(mockedQuery);

        Response response = retrieveBookingsService.retrieve(day, month, year, "test");

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
        Assert.assertThat(((List<Booking>)(response.getEntity())).isEmpty(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void retrieve() {
        Booking booking1 = mock(Booking.class);
        Booking booking2 = mock(Booking.class);

        ArrayList<Booking> result = new ArrayList();
        result.add(booking1);
        result.add(booking2);

        int day = 6;
        int month = 12;
        int year = 1996;

        TypedQuery<Booking> mockedQuery = (TypedQuery<Booking>) mock(TypedQuery.class);
        when(manager.createNamedQuery(GET_BOOKINGS_FROM_CALENDAR_AND_DATE_AND_USER, Booking.class))
                .thenReturn(mockedQuery);
        when(mockedQuery.setParameter("inputDay", day)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("inputMonth", month)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("inputYear", year)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("email", "test")).thenReturn(mockedQuery);

        when(mockedQuery.getResultList()).thenReturn(result);
        when(manager.createNamedQuery(GET_BOOKING_FROM_CALENDAR_AND_DATE, Booking.class)).thenReturn(mockedQuery);

        Response response = retrieveBookingsService.retrieve(day, month, year, "test");

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
        Assert.assertThat(((List<Booking>)(response.getEntity())).contains(booking1), is(true));
        Assert.assertThat(((List<Booking>)(response.getEntity())).contains(booking2), is(true));
    }
}
*/
