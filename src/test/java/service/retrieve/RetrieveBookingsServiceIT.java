package service.retrieve;

import config.Configuration;
import dao.Dao;
import model.Booking;
import model.User;
import org.junit.*;
import service.Helper;
import service.Result;
import service.booking.BookingService;
import service.overlapping.OverlappingService;
import service.registration.RegistrationService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static service.Helper.*;
import static service.Helper.stringToMillis;

public class RetrieveBookingsServiceIT {

    private static Dao dao;
    private static  EntityManager manager;
    private static RetrieveBookingsService rService;
    private static EntityManagerFactory entityManagerFactory;

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(Configuration.TEST_UNIT);
    }

    @Before
    public void setup() {
        manager = entityManagerFactory.createEntityManager();
        dao = new Dao(manager);
        rService = new RetrieveBookingsService(dao);
        Helper helper = new Helper();
        dao.start();
        helper.deleteTables(manager, dao);
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
    public void retrieveEmptyList() {
        List<Booking> emptyList = new ArrayList<>();

        Result result = rService.retrieve(6, 12, 1996, testEmail);

        Assert.assertThat(result.success(), is(true));
        Assert.assertThat(result.getContent(), is(emptyList));
    }

    @Test
    public void retrieve() {
        RegistrationService registrationService = new RegistrationService(dao);
        OverlappingService oService = new OverlappingService(dao);
        BookingService bService = new BookingService(oService, dao);

        Date start = new Date(stringToMillis("1996-12-6 10:00:00.000000"));
        Date end = new Date(stringToMillis("1996-12-6 14:00:00.000000"));
        Booking booking = new Booking(testSubject, testDescription, start, end);

        User user = new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, true);

        registrationService.register(user);
        bService.book(booking, testEmail);
        Result result = rService.retrieve(6, 12, 1996, testEmail);

        Assert.assertThat(result.success(), is(true));
        Assert.assertThat(result.getContent(), is(List.of(booking)));
    }

}
