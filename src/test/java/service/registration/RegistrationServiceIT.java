package service.registration;

import config.Configuration;
import dao.Dao;
import model.User;
import org.junit.*;
import service.Helper;
import service.Result;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.hamcrest.core.Is.is;
import static service.Helper.*;

public class RegistrationServiceIT {

    private static Dao dao;
    private static EntityManager manager;
    private static RegistrationService rService;
    private static EntityManagerFactory entityManagerFactory;

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(Configuration.TEST_UNIT);
    }

    @Before
    public void setup() {
        manager = entityManagerFactory.createEntityManager();
        Helper helper = new Helper();
        dao = new Dao(manager);

        rService = new RegistrationService(dao);
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
    public void attemptToOwnAnotherGroup() {
        rService.register(new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, true));
        Result result = rService.register(new User(testFirstName, testLastName, testGmt, "newEmail", testPassword, testGroup, true));

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void successfulOwnerRegistration() {
        Result result = rService.register(new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, true));

        Assert.assertThat(result.success(), is(true));
    }

    @Test
    public void alreadyRegisteredOwner() {
        rService.register(new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, true));
        Result result =rService.register(new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, true));

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void alreadyRegisteredNonOwner() {
        rService.register(new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, false));
        Result result = rService.register(new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, false));

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void registerNonOwnerToInexistentGroup() {
        Result result = rService.register(new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, false));

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void registerNonOwnerToExistentGroup() {
        rService.register(testOwnerUser);
        Result result = rService.register(new User(testFirstName, testLastName, testGmt, "newEmail", testPassword, testGroup, false));

        Assert.assertThat(result.success(), is(true));
    }

}
