package service;

import model.booking.Booking;
import org.junit.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static model.booking.Booking.SPECIFIC_BOOKING;

public class RetrieveBookingsServiceIT {

    private final static String PERSISTENCE_UNIT = "testUnit";
    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;
    private RetrieveBookingsService retrieveBookingsService;

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    @Before
    public void setup() {
        entityManager = entityManagerFactory.createEntityManager();
        retrieveBookingsService = new RetrieveBookingsService();
        retrieveBookingsService.entityManager = entityManager;
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
    public void successfulRetrieve() throws ParseException {
        Date start = new Date(stringToMillis("1996-12-6 14:00:00.000000"));
        Date end = new Date(stringToMillis("1996-12-6 16:00:00.000000"));


        Booking booking = new Booking("Subject", "Description", start, end);
        entityManager.persist(booking);

        Response response = retrieveBookingsService.retrieve("6", "12", "1996");

        Assert.assertTrue(response.success());
        Assert.assertTrue(recordIsInDatabase(booking));
    }


    //TODO 3 testF
    @Test
    public void unsuccessfulRetrieve() {
        Response response = retrieveBookingsService.retrieve("ciao", "not a number", "2019");

        Assert.assertFalse(response.success());
    }

    private long stringToMillis(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date d = sdf.parse(date);
        return d.getTime();
    }

    private boolean recordIsInDatabase(Booking booking) {
        List<Booking> bookings = getBookings(booking);
        return !bookings.isEmpty();
    }

    private List<Booking> getBookings(Booking booking) {
        entityManager.getTransaction().begin();

        List<Booking> bookings = entityManager
                .createNamedQuery(SPECIFIC_BOOKING, Booking.class)
                .setParameter("subject", booking.getSubject())
                .setParameter("description", booking.getDescription())
                .setParameter("start", booking.getStart())
                .setParameter("end", booking.getEnd())
                .getResultList();

        entityManager.getTransaction().commit();

        return bookings;
    }
}
