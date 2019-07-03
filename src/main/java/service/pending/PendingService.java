package service.pending;

import dao.Dao;
import model.Pending;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PendingService {

    private final static Logger logger = LoggerFactory.getLogger(PendingService.class);

    @EJB
    private Dao dao;

    public PendingService(Dao dao) {
        this.dao = dao;
    }

    public PendingService() {

    }

    public Result pendingList(String email) {
        List<Pending> requests =  dao.getPendingList(email);
        List<String> mails = new ArrayList<>();
        requests.forEach(request -> mails.add(request.getWaiterMail()));
        return Result.success(mails);
    }

    public Result acceptRequest(String ownerEmail, String toRemoveEmail) {
        dao.removeFrom(ownerEmail, toRemoveEmail);
        dao.setAccepted(toRemoveEmail);
        return Result.success("Request accepted");

    }

    public Result numberOfPending(String ownerEmail) {
        return dao.getNumberOfPendingsFor(ownerEmail);
    }
}
