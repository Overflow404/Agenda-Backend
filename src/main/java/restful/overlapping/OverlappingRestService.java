package restful.overlapping;


import service.overlapping.OverlappingService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    public Response overlap(@QueryParam("startDate") long startDate,
                            @QueryParam("endDate") long endDate) {

        Date startMillisDate = new Date(startDate);
        Date endMillisDate = new Date(endDate);

        return overlappingService.checkIfDatesOverlap(startMillisDate, endMillisDate);
    }
}
