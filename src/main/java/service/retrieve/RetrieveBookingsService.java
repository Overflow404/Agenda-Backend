package service.retrieve;

import dao.Dao;
import model.Booking;
import service.Result;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class RetrieveBookingsService {

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
