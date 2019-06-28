package restful.retrieve;

import service.auth.AuthService;
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

    private static AuthService auth = new AuthService();

    @EJB
    private RetrieveBookingsService retrieveBookingsService;

    @GET
    @Path(RETRIEVE_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieve(@HeaderParam("Authorization") String jwt,
                             @QueryParam("day") int day,
                             @QueryParam("month") int month,
                             @QueryParam("year") int year) {

        if (auth.isAuthenticated(jwt)) {
            String email = auth.getEmail(jwt);
            Result result =  retrieveBookingsService.retrieve(day, month, year, email);
            if (result.success()) {
                return Response.ok().entity(result.getContent()).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
