package service.overlapping;

import config.Configuration;
import dao.Dao;
import org.junit.*;
import service.Helper;
import service.Result;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static service.Helper.testGroup;
import static service.Helper.stringToMillis;

public class OverlappingServiceIT {

    private static Dao dao;
    private static EntityManager manager;
    private static OverlappingService oService;
    private static EntityManagerFactory entityManagerFactory;

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(Configuration.TEST_UNIT);
    }

    @Before
    public void setup() {
        manager = entityManagerFactory.createEntityManager();

        dao = new Dao(manager);
        oService = new OverlappingService(dao);

        Helper helper = new Helper();

        dao.start();
        helper.deleteTables(manager, dao);
        helper.registerSampleUser(dao);
        helper.insertSampleSlot(dao, oService);
    }


    @After
    public void tearDown() {
        dao.commit();
        manager.close();
    }

    @AfterClass
    public static void tearDownClass() {
        entityManagerFactory.close();
    }

    @Test
    public void afterTest() {
        Date start = new Date(stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 13:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(true));
    }

    @Test
    public void startTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 14:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(true));
    }

    @Test
    public void startInsideTest() {
        Date start = new Date(stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 14:01:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void insideStartTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 20:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);
        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void enclosingStartTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 15:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void enclosingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 15:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void enclosingEndTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void exactMatchTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void insideTest() {
        Date start = new Date(stringToMillis("2019-06-16 13:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:30:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void insideEndTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 13:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 16:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void endInsideTest() {
        Date start = new Date(stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 18:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void endTouchingTest() {
        Date start = new Date(stringToMillis("2019-06-16 16:00:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 18:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(true));
    }

    @Test
    public void beforeTest() {
        Date start = new Date(stringToMillis("2019-06-16 16:01:00.000000"));
        Date end = new Date(stringToMillis("2019-06-16 19:00:00.000000"));

        Result result = oService.checkIfDatesOverlap(start, end, testGroup);

        Assert.assertThat(result.success(), is(true));
    }

}
