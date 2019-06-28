package service.booking;

import dao.Dao;
import model.Booking;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import model.User;
import service.Result;
import service.overlapping.OverlappingService;
import java.util.Date;

@Stateless
public class BookingService {

    @EJB
    OverlappingService service;

    @EJB
    Dao dao;

    public Result book(Booking booking, String email) {
        User user = dao.getUserByMail(email);

        Date start = booking.getStart();
        Date end = booking.getEnd();
        String calendarName = user.getGroupName();

        Result result = service.checkIfDatesOverlap(start, end, calendarName);

        if (result.success()) {
            dao.addBookingToUser(booking, user);
            return Result.success("Booking confirmed!");
        }
        return Result.failure("This time slot is already booked!!");
    }

    public void setService(OverlappingService service) {
        this.service = service;
    }

}
