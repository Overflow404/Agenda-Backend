package service.pending;

import config.Configuration;
import dao.Dao;
import org.junit.*;
import service.Helper;
import service.registration.RegistrationService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PendingServiceIT {

    private static Dao dao;
    private static EntityManager manager;
    private static PendingService pService;
    private static RegistrationService rService;
    private static EntityManagerFactory entityManagerFactory;

    @BeforeClass
    public static void setupClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory(Configuration.TEST_UNIT);
    }

    @Before
    public void setup() {
        manager = entityManagerFactory.createEntityManager();

        dao = new Dao(manager);
        pService = new PendingService(dao);
        rService = new RegistrationService(dao);

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

/*    @Test
    public void pendingListShouldContainEmailOfWaitingUsers() {
        User owner = Helper.getNewOwnerUser();
        owner.setEmail("test0@gmail.com");
        rService.register(owner);

        User first = Helper.getNewNonOwnerUser();
        owner.setEmail("test1@gmail.com");
        rService.register(first);

        User second = Helper.getNewNonOwnerUser();
        owner.setEmail("test2@gmail.com");
        rService.register(second);

        Result result = pService.pendingList("test0@gmail.com");
        System.out.println(result.getContent());
    }*/
}
