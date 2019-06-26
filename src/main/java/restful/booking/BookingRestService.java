package restful.booking;

import model.Booking;
import service.booking.BookingService;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

import static config.Configuration.BOOKING_SERVICE_PATH;
import static config.Configuration.ROOT_PATH;

@Path(ROOT_PATH)
public class BookingRestService {

    @EJB
    private BookingService bookingService;

    @GET
    @Path(BOOKING_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response book(@QueryParam("subject") String subject,
                         @QueryParam("description") String description,
                         @QueryParam("startDate") long startDate,
                         @QueryParam("endDate") long endDate) {

        if (isEmptyOrNull(subject)) {
            return Response.status(Response.Status.PARTIAL_CONTENT).build();
        }

        Date startMillisDate = new Date(startDate);
        Date endMillisDate = new Date(endDate);

        Booking booking = new Booking(subject, description, startMillisDate, endMillisDate);

        return bookingService.book(booking);
    }

    private boolean isEmptyOrNull(String ... args) {
        for (String arg : args) {
            if (arg == null || arg.isBlank()) {
                return true;
            }
        }
        return false;
    }

}
