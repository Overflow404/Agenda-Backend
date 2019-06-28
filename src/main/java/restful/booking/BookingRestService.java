package restful.booking;

import model.Booking;
import service.AuthService;
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

    @EJB
    private BookingService bookingService;

    @GET
    @Path(BOOKING_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response book(@HeaderParam("Authorization") String jwt,
                         @QueryParam("subject") String subject,
                         @QueryParam("description") String description,
                         @QueryParam("startDate") long startDate,
                         @QueryParam("endDate") long endDate) {

        AuthService auth = new AuthService();

        if (isEmptyOrNull(subject)) {
            return Response.status(Response.Status.PARTIAL_CONTENT).build();
        }

        if (!auth.isAuthenticated(jwt)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Date startMillisDate = new Date(startDate);
        Date endMillisDate = new Date(endDate);

        Booking booking = new Booking(subject, description, startMillisDate, endMillisDate);
        String email = auth.getEmail(jwt);

        return bookingService.book(booking, email);
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
