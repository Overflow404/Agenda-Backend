package restful.overlapping;


import service.AuthService;
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
                            @QueryParam("endDate") long endDate) {

        AuthService auth = new AuthService();

        Date startMillisDate = new Date(startDate);
        Date endMillisDate = new Date(endDate);

        if (!auth.isAuthenticated(jwt)) {
            Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return overlappingService.checkIfDatesOverlap(startMillisDate, endMillisDate);
    }
}
