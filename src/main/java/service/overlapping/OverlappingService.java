package service.overlapping;

import dao.Dao;
import service.Result;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;

@Stateless
public class OverlappingService {

    @EJB
    Dao dao;

    public OverlappingService() {

    }

    public Result checkIfDatesOverlap(Date start, Date end, String calendarName) {
        return dao.checkOverlap(start, end, calendarName);
    }

}
