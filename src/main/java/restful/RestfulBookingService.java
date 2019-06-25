package restful;

import model.booking.Booking;
import model.user.User;
import service.*;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("/date")
public class RestfulBookingService {

    @EJB
    private OverlappingService overlappingService;

    @EJB
    private BookingService bookingService;

    @EJB
    private RetrieveBookingsService retrieveBookingsService;

    @EJB
    private RegistrationService registrationService;

    @GET
    @Path("/overlapping")
    @Produces(MediaType.APPLICATION_JSON)
    public Response overlap(@QueryParam("startDate") String startDate,
                            @QueryParam("endDate") String endDate) {

        Date startMillisDate = new Date(Long.valueOf(startDate));
        Date endMillisDate = new Date(Long.valueOf(endDate));

        return overlappingService.checkIfDatesOverlap(startMillisDate, endMillisDate);
    }

    @GET
    @Path("/book")
    @Produces(MediaType.APPLICATION_JSON)
    public Response book(@QueryParam("subject") String subject,
                         @QueryParam("description") String description,
                         @QueryParam("startDate") String startDate,
                         @QueryParam("endDate") String endDate) {

        Date startMillisDate = new Date(Long.valueOf(startDate));
        Date endMillisDate = new Date(Long.valueOf(endDate));

        Booking booking = new Booking(subject, description, startMillisDate, endMillisDate);

        return bookingService.book(booking);
    }

    @GET
    @Path("/retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(@QueryParam("day") String d,
                         @QueryParam("month") String m,
                         @QueryParam("year") String y) {

        return retrieveBookingsService.retrieve(d, m, y);
    }

    @GET
    @Path("/retrieveAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAll() {
        return retrieveBookingsService.retrieveAll();
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        return registrationService.register(user);
    }

}
