package service.booking;

import dao.Dao;
import model.Booking;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import service.overlapping.OverlappingService;
import java.util.Date;

@Singleton
public class BookingService {

    private final static Logger logger = LoggerFactory.getLogger(BookingService.class);


    @EJB
    private OverlappingService oService;

    @EJB
    private Dao dao;

    public BookingService(OverlappingService oService, Dao dao) {
        this.oService = oService;
        this.dao = dao;
    }

    public BookingService() {

    }

    public Result book(Booking booking, String email) {
        User user = dao.getUserByMail(email);

        Date start = booking.getStart();
        Date end = booking.getEnd();
        String calendarName = user.getGroupName();

        Result result = oService.checkIfDatesOverlap(start, end, calendarName);

        if (result.success()) {
            dao.addBookingToUser(booking, user);
            return Result.success("Booking confirmed!");
        }
        return Result.failure("This time slot is already booked!");
    }

}
