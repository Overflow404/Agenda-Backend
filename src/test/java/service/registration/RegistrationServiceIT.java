package service.registration;

import config.Configuration;
import model.User;
import org.apache.http.HttpStatus;
import org.junit.*;
import utils.TestUtils;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;

public class RegistrationServiceIT {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager manager;
    private RegistrationService registrationService;
    private TestUtils utils = new TestUtils();

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(Configuration.TEST_UNIT);
    }

    @Before
    public void setup() {
        manager = entityManagerFactory.createEntityManager();
        registrationService = new RegistrationService();
        registrationService.manager = manager;
    }

    @After
    public void teardown() {
        manager.close();
    }

    @AfterClass
    public static void teardownClass() {
        entityManagerFactory.close();
    }

    @Test
    public void successfulRegistration() {
        User user = new User("Gaspare", "Armato", "GMT+02",
                "gaspare.armato1@gmail.com", "myPassword");

        registrationService.register(user);
        boolean userRegistered = utils.userIsInDatabase(user, manager);

        Assert.assertThat(userRegistered, is(true));
    }

    @Test
    public void alreadyRegistered() {
        User user = new User("Gaspare", "Armato", "GMT+02",
                "gaspare.armato1@gmail.com", "myPassword");

        manager.getTransaction().begin();
        registrationService.register(user);
        manager.flush();
        manager.clear();
        Response response = registrationService.register(user);
        manager.getTransaction().commit();

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }
}
