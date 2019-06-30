package service.registration;

import dao.Dao;
import model.Calendar;
import model.User;
import service.Result;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class RegistrationService {

    @EJB
    private Dao dao;

    public RegistrationService(Dao dao) {
        this.dao = dao;
    }

    public RegistrationService() {

    }

    public Result register(User user) {

        String group = user.getGroupName();

        if (attemptToOwnAnotherGroup(user, group)) {
            return Result.failure("Attempting to own another group!");
        }

        if (groupOwner(user)) {
            Calendar calendar = new Calendar(group);
            calendar.addUser(user);
            return dao.persistCalendar(calendar);
        }

        if (!dao.groupExist(group)) {
            return Result.failure("Group not found!");
        }

        Calendar calendar = dao.retrieveCalendar(group);
        calendar.addUser(user);

        return Result.success("Registration successful!");
    }

    private boolean attemptToOwnAnotherGroup(User user, String group) {
        return groupOwner(user) && dao.groupExist(group);
    }


    private boolean groupOwner(User user) {
        return user.isOwner();
    }
}
