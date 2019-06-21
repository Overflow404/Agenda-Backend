package restful;

import service.overlapping.OverlappingResult;
import service.overlapping.OverlappingService;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;

@Path("/date")
public class RestfulBookingService {

    @EJB
    private OverlappingService overlappingService;

    @GET
    @Path("/overlapping")
    @Produces(MediaType.APPLICATION_JSON)
    public OverlappingResult overlap(@QueryParam("startDate") String startDate,
                                     @QueryParam("endDate") String endDate) {

        Timestamp startMillisDate = new Timestamp(Long.valueOf(startDate));
        Timestamp endMillisDate = new Timestamp(Long.valueOf(endDate));
        return overlappingService.checkIfDatesOverlap(startMillisDate, endMillisDate);
    }

}
