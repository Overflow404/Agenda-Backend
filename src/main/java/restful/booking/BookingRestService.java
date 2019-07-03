package restful.booking;

import model.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import service.booking.BookingService;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

import static config.Configuration.BOOKING_SERVICE_PATH;
import static config.Configuration.ROOT_PATH;

@Path(ROOT_PATH)
public class BookingRestService {

    private final static Logger logger = LoggerFactory.getLogger(BookingRestService.class);


    @EJB
    private BookingService bookingService;

    @GET
    @Path(BOOKING_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response book(@HeaderParam("Authorization") String jwt,
                         @QueryParam("subject") String subject,
                         @QueryParam("description") String description,
                         @QueryParam("startDate") long startDate,
                         @QueryParam("endDate") long endDate,
                         @QueryParam("email") String email) {

        if (isEmptyOrNull(subject)) {
            return Response.status(Response.Status.PARTIAL_CONTENT).build();
        }

        Date start = new Date(startDate);
        Date end = new Date(endDate);

        Booking booking = new Booking(subject, description, start, end);

        Result result = bookingService.book(booking, email);

        if (result.success()) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity(result.getFailureReason())
                    .build();

        }
    }

    private boolean isEmptyOrNull(String arg) {
        return arg == null || arg.isBlank();
    }

}
