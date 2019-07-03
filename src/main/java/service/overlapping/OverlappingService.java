package service.overlapping;

        import dao.Dao;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import service.Result;
        import javax.ejb.EJB;
        import javax.ejb.Stateless;
        import java.util.Date;

@Stateless
public class OverlappingService {

    private final static Logger logger = LoggerFactory.getLogger(OverlappingService.class);

    @EJB
    private Dao dao;

    public OverlappingService(Dao dao) {
        this.dao = dao;
    }

    public OverlappingService() {

    }

    public Result checkIfDatesOverlap(Date start, Date end, String calendarName) {
        return dao.checkOverlap(start, end, calendarName);
    }

}
