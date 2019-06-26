package service.overlapping;

import model.Booking;
import org.apache.http.HttpStatus;
import org.junit.*;
import utils.TestUtils;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;


public class OverlappingServiceIT {

    private static EntityManagerFactory entityManagerFactory;
    private final static String PERSISTENCE_UNIT = "testUnit";
    private EntityManager entityManager;
    private OverlappingService overlappingService;
    private TestUtils utils = new TestUtils();

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    @Before
    public void setup() {
        entityManager = entityManagerFactory.createEntityManager();
        overlappingService = new OverlappingService();
        overlappingService.setManager(entityManager);

        deleteTables();
        insertSampleSlot();
    }

    @After
    public void teardown() {
        entityManager.close();
    }

    @AfterClass
    public static void teardownClass() {
        entityManagerFactory.close();
    }

    @Test
    public void afterTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 13:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    public void startTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 14:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    public void startInsideTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 10:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 14:01:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void insideStartTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 20:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void enclosingStartTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 15:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void enclosingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 15:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void enclosingEndTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void exactMatchTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void insideTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 13:30:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 16:30:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void insideEndTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 13:30:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void endInsideTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:30:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 18:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void endTouchingTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 18:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    @Test
    public void beforeTest() {
        Date start = new Date(utils.stringToMillis("2019-06-16 16:01:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 19:00:00.000000"));

        Response response = overlappingService.checkIfDatesOverlap(start, end);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_OK));
    }

    private void deleteTables() {
        entityManager.getTransaction().begin();

        Query deleteBookings = entityManager.createQuery("DELETE FROM Booking");
        Query deleteUsers = entityManager.createQuery("DELETE FROM User");

        deleteBookings.executeUpdate();
        deleteUsers.executeUpdate();

        entityManager.getTransaction().commit();
    }

    private void insertSampleSlot() {
        Date start = new Date(utils.stringToMillis("2019-06-16 14:00:00.000000"));
        Date end = new Date(utils.stringToMillis("2019-06-16 16:00:00.000000"));

        entityManager.getTransaction().begin();
        Booking booking = new Booking("Test subject", "Test description", start, end);
        entityManager.persist(booking);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }
}
