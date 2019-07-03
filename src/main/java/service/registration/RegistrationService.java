package service.registration;

import dao.Dao;
import model.Calendar;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class RegistrationService {

    private final static Logger logger = LoggerFactory.getLogger(RegistrationService.class);

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
            logger.info("Attempting to own another group!");
            return Result.failure("Attempting to own another group!");
        }
        if (groupOwner(user)) {
            return dao.persistToNewCalendar(group, user);
        }

        if (!dao.groupExist(group)) {
            logger.info("Group not found!");
            return Result.failure("Group not found!");
        }

        return dao.persistToAlreadyCalendar(group, user);
    }

    private boolean attemptToOwnAnotherGroup(User user, String group) {
        return groupOwner(user) && dao.groupExist(group);
    }


    private boolean groupOwner(User user) {
        return user.isOwner();
    }
}
