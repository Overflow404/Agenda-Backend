/*
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
import static org.hamcrest.CoreMatchers.notNullValue;
import static utils.TestUtils.RANDOM_GROUP;

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

        utils.deleteTables(manager);
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
    public void registerAnOwnerUser() {
        User user = new User("test", "test", "test", "test",
                "test", RANDOM_GROUP, true);

        registrationService.register(user);

        User expected = utils.findUserByCalendarAndEmail(RANDOM_GROUP, "test", manager);

        Assert.assertThat(expected, is(notNullValue()));
    }

    @Test
    public void registerUserToUnknownGroup() {
        User user = new User("test", "test", "test", "test",
                "test", "inexistent", false);

        Response response = registrationService.register(user);

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_NOT_FOUND));

    }

    @Test
    public void registerUserToKnownGroup() {
        User owner = new User("test", "test", "test", "test1",
                "test", RANDOM_GROUP, true);

        manager.getTransaction().begin();
        registrationService.register(owner);

        User user = new User("test", "test", "test", "test2",
                "test", RANDOM_GROUP, false);

        registrationService.register(user);
        manager.getTransaction().commit();

        User expected = utils.findUserByCalendarAndEmail(RANDOM_GROUP, "test2", manager);

        Assert.assertThat(expected.getGroupName(), is(RANDOM_GROUP));
        Assert.assertThat(expected.getCalendar(), is(user.getCalendar()));
        Assert.assertThat(expected.isOwner(), is(false));
    }


    @Test
    public void registerAnAlreadyRegisteredOwnerUser() {
        User owner = new User("test", "test", "test", "test1",
                "test", RANDOM_GROUP, true);

        manager.getTransaction().begin();
        registrationService.register(owner);
        manager.flush();
        manager.clear();
        Response response = registrationService.register(owner);
        manager.getTransaction().commit();

        Assert.assertThat(response.getStatus(), is(HttpStatus.SC_CONFLICT));
    }

}
*/
