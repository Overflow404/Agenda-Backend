package restful.retrieve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import service.retrieve.RetrieveBookingsService;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static config.Configuration.RETRIEVE_SERVICE_PATH;
import static config.Configuration.ROOT_PATH;

@Path(ROOT_PATH)
public class RetrieveRestService {

    private final static Logger logger = LoggerFactory.getLogger(RetrieveRestService.class);


    @EJB
    private RetrieveBookingsService retrieveBookingsService;

    @GET
    @Path(RETRIEVE_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(@HeaderParam("Authorization") String jwt,
                             @QueryParam("day") int day,
                             @QueryParam("month") int month,
                             @QueryParam("year") int year,
                             @QueryParam("email") String email) {

        Result result = retrieveBookingsService.retrieve(day, month, year, email);
        return Response.ok().entity(result.getContent()).build();
    }
}
