package service.pending;

import dao.Dao;
import model.Pending;
import model.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import service.Helper;
import service.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PendingServiceTest {

    private static Dao dao;
    private static PendingService pService;


    @BeforeClass
    public static void setupClass() {
        dao = mock(Dao.class);
        pService = new PendingService(dao);
    }

/*    @Test
    public void getPendingListTest() {
        User waitingUser1 = Helper.getNewOwnerUser();
        User waitingUser2 = Helper.getNewOwnerUser();
        User waitingUser3 = Helper.getNewOwnerUser();

        waitingUser1.setEmail("test1@gmail.com");
        waitingUser2.setEmail("test2@gmail.com");
        waitingUser3.setEmail("test3@gmail.com");

        List<Pending> pendings = Arrays.asList(
                new Pending(waitingUser1),
                new Pending(waitingUser2),
                new Pending(waitingUser3)
                );

        when(dao.getPendingList("test0@gmail.com")).thenReturn(pendings);

        Result result = pService.pendingList("test0@gmail.com");

        Assert.assertThat(result.success(), is(true));
        Assert.assertThat(result.getContent(), is(Arrays.asList(
                "test1@gmail.com",
                "test2@gmail.com",
                "test3@gmail.com")));
    }*/

    @Test
    public void getEmptyPendingListTest() {
        List<Pending> pendings = new ArrayList<>();

        when(dao.getPendingList("test0@gmail.com")).thenReturn(pendings);

        Result result = pService.pendingList("test0@gmail.com");

        Assert.assertThat(result.success(), is(true));
        Assert.assertThat(result.getContent(), is(new ArrayList()));
    }
}
