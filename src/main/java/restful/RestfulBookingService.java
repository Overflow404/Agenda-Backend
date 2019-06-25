package restful;

import model.booking.Booking;
import service.Response;
import service.BookingService;
import service.OverlappingService;
import service.RetrieveBookingsService;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;

@Path("/date")
public class RestfulBookingService {

    @EJB
    private OverlappingService overlappingService;

    @EJB
    private BookingService bookingService;

    @EJB
    private RetrieveBookingsService retrieveBookingsService;

    //TODO timestamp da fare dentro i service e controllare numberformat exception
    @GET
    @Path("/overlapping")
    @Produces(MediaType.APPLICATION_JSON)
    public Response overlap(@QueryParam("startDate") String startDate,
                            @QueryParam("endDate") String endDate) {

        Timestamp startMillisDate = new Timestamp(Long.valueOf(startDate));
        Timestamp endMillisDate = new Timestamp(Long.valueOf(endDate));

        Response response = overlappingService.checkIfDatesOverlap(startMillisDate, endMillisDate);
        return response;
    }

    @GET
    @Path("/book")
    @Produces(MediaType.APPLICATION_JSON)
    public Response book(@QueryParam("subject") String subject,
                         @QueryParam("description") String description,
                         @QueryParam("startDate") String startDate,
                         @QueryParam("endDate") String endDate) {

        Timestamp startMillisDate = new Timestamp(Long.valueOf(startDate));
        Timestamp endMillisDate = new Timestamp(Long.valueOf(endDate));

        Booking booking = new Booking(subject, description, startMillisDate, endMillisDate);

        Response response = bookingService.book(booking);
        return response;
    }

    @GET
    @Path("/retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(@QueryParam("day") String d,
                         @QueryParam("month") String m,
                         @QueryParam("year") String y) {

        Response response = retrieveBookingsService.retrieve(d, m, y);
        return response;
    }

    @GET
    @Path("/retrieveall")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveall() {

        Response response = retrieveBookingsService.retrieveAll();
        return response;
    }

}
