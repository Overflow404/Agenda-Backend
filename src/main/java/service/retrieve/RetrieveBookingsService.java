package service.retrieve;

import dao.Dao;
import model.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class RetrieveBookingsService {

    private final static Logger logger = LoggerFactory.getLogger(RetrieveBookingsService.class);


    @EJB
    private Dao dao;

    RetrieveBookingsService(Dao dao) {
        this.dao = dao;
    }

    public RetrieveBookingsService() {

    }

    public Result retrieve(int day, int month, int year, String email) {
        List<Booking> bookings = dao.retrieveBookingsFrom(day, month, year, email);
        return Result.success(bookings);

    }

}
