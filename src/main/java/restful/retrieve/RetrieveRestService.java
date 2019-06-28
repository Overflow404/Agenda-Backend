package restful.retrieve;

import service.AuthService;
import service.retrieve.RetrieveBookingsService;
import javax.ejb.EJB;
import javax.ws.rs.*;
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
    public Response retrieve(@HeaderParam("Authorization") String jwt,
                             @QueryParam("day") int day,
                             @QueryParam("month") int month,
                             @QueryParam("year") int year) {
        AuthService auth = new AuthService();

        if (auth.isAuthenticated(jwt)) {
            String email = auth.getEmail(jwt);
            return retrieveBookingsService.retrieve(day, month, year, email);
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();

    }
}
