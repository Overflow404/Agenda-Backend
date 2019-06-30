package service.login;

import config.Configuration;
import dao.Dao;
import model.User;
import org.junit.*;
import service.Helper;
import service.Result;
import service.auth.AuthService;
import service.registration.RegistrationService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.hamcrest.core.Is.is;
import static service.Helper.*;

public class LoginServiceIT {

    private static Dao dao;
    private static EntityManager manager;
    private static LoginService lService;
    private static EntityManagerFactory entityManagerFactory;
    private static Helper helper;

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(Configuration.TEST_UNIT);
    }

    @Before
    public void setup() {
        manager = entityManagerFactory.createEntityManager();
        dao = new Dao(manager);
        helper = new Helper();
        AuthService auth = new AuthService();
        lService = new LoginService(auth, dao);
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
    public void nonRegisteredUserAttemptToLogin() {
        Result result = lService.login(testOwnerUser);

        Assert.assertThat(result.success(), is(false));
    }

    @Test
    public void registeredUserAttemptToLogin() {
        User user = new User(testFirstName, testLastName, testGmt, testEmail, testPassword, testGroup, true);
        RegistrationService registrationService = new RegistrationService(dao);
        registrationService.register(user);

        Result result = lService.login(testOwnerUser);

        Assert.assertThat(result.success(), is(true));
    }
}
