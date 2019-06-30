package restful.overlapping;

import service.Result;
import service.overlapping.OverlappingService;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

import static config.Configuration.OVERLAPPING_SERVICE_PATH;
import static config.Configuration.ROOT_PATH;

@Path(ROOT_PATH)
public class OverlappingRestService {

    @EJB
    private OverlappingService overlappingService;

    @GET
    @Path(OVERLAPPING_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response overlap(@HeaderParam("Authorization") String jwt,
                            @QueryParam("startDate") long startDate,
                            @QueryParam("endDate") long endDate,
                            @QueryParam("group") String group){

        Date start = new Date(startDate);
        Date end = new Date(endDate);

        if (endTimeIsGreaterThanStartTime(start, end)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Result result = overlappingService.checkIfDatesOverlap(start, end, group);

        if (result.success()) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity(result.getFailureReason())
                    .build();
        }
    }

    private boolean endTimeIsGreaterThanStartTime(Date start, Date end) {
        return start.after(end) || start.equals(end);
    }
}
