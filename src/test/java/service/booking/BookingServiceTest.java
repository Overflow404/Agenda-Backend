package service.booking;

import dao.Dao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import service.Result;
import service.overlapping.OverlappingService;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static service.Helper.*;

public class BookingServiceTest {

    private static Dao dao;
    private static BookingService bService;
    private static OverlappingService oService;

    @BeforeClass
    public static void setupClass() {
        dao = mock(Dao.class);
        oService = mock(OverlappingService.class);
        bService = new BookingService(oService, dao);
    }

    @Before
    public void setup() {
        when(dao.getUserByMail(testEmail)).thenReturn(testOwnerUser);
    }

    @Test
    public void successfulBooking() {
        when(oService
                .checkIfDatesOverlap(testStart, testEnd, testGroup))
                .thenReturn(successResult);

        Result result = bService.book(testBooking, testEmail);

        Assert.assertThat(result.success(), is(true));
    }

    @Test
    public void unsuccessfulBooking() {
        when(oService
                .checkIfDatesOverlap(testStart, testEnd, testGroup))
                .thenReturn(failureResult);

        Result result = bService.book(testBooking, testEmail);

        Assert.assertThat(result.success(), is(false));
    }

}
