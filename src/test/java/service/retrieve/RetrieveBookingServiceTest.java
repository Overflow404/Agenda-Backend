package service.retrieve;


import dao.Dao;
import model.Booking;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.Result;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static service.Helper.testBooking;
import static service.Helper.testEmail;

public class RetrieveBookingServiceTest {

    private static Dao dao;
    private static RetrieveBookingsService rService;

    @BeforeClass
    public static void setupClass() {
        dao = mock(Dao.class);
        rService = new RetrieveBookingsService(dao);
    }

    @Test
    public void retrieveEmptyList() {
        List<Booking> emptyList = new ArrayList<>();
        when(dao.retrieveBookingsFrom(6, 12, 1996, testEmail)).thenReturn(emptyList);

        Result result = rService.retrieve(6, 12, 1996, testEmail);

        Assert.assertThat(result.success(), is(true));
        Assert.assertThat(result.getContent(), is(emptyList));
    }

    @Test
    public void retrieve() {
        List<Booking> list = new ArrayList<>();
        list.add(testBooking);

        when(dao.retrieveBookingsFrom(6, 12, 1996, testEmail)).thenReturn(list);

        Result result = rService.retrieve(6, 12, 1996, testEmail);

        Assert.assertThat(result.success(), is(true));
        Assert.assertThat(result.getContent(), is(list));
    }

}
