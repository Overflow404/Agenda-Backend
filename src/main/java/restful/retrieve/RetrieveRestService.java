package restful.retrieve;

import service.retrieve.RetrieveBookingsService;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static config.Configuration.RETRIEVE_SERVICE_PATH;
import static config.Configuration.ROOT_PATH;

@Path(ROOT_PATH)
public class RetrieveRestService {

    @EJB
    private RetrieveBookingsService retrieveBookingsService;

    @GET
    @Path(RETRIEVE_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(@QueryParam("day") int day,
                             @QueryParam("month") int month,
                             @QueryParam("year") int year) {

        return retrieveBookingsService.retrieve(day, month, year);
    }
}
