package service.overlapping;

import dao.Dao;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.Result;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static service.Helper.*;

public class OverlappingServiceTest {

    private static Dao dao;
    private static OverlappingService oService;

    @BeforeClass
    public static void setupClass() {
        dao = mock(Dao.class);
        oService = new OverlappingService(dao);
    }


    @Test
    public void afterTest() {
        Date start = new Date(stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 13:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(successResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(true));
    }

    @Test
    public void startTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 14:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(successResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(true));
    }

    @Test
    public void startInsideTest() {
        Date start = new Date(stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 14:01:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(failureResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void insideStartTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 20:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(failureResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void enclosingStartTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 15:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(failureResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void enclosingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 15:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(failureResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void enclosingEndTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(failureResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void exactMatchTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(failureResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void insideTest() {
        Date start = new Date(stringToMillis("2019-06-16 13:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:30:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(failureResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void insideEndTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 13:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(failureResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void endInsideTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 18:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(failureResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void endTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 16:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 18:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(successResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(true));
    }

    @Test
    public void beforeTest() {
        Date start = new Date(stringToMillis("2019-06-16 16:01:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 19:00:00.000000"));

        when(dao.checkOverlap(start, end, testGroup)).thenReturn(successResult);

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(true));
    }

}
