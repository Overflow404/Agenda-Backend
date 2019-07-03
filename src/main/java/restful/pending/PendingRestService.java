package restful.pending;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import service.pending.PendingService;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static config.Configuration.*;

@Path(ROOT_PATH)
public class PendingRestService {

    private final static Logger logger = LoggerFactory.getLogger(PendingRestService.class);

    @EJB
    private PendingService pendingService;

    @GET
    @Path(PENDING_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pendingList(@HeaderParam("Authorization") String jwt,
                            @QueryParam("email") String email) {
        Result result  = pendingService.pendingList(email);
        return Response.ok().entity(result.getContent()).build();

    }

    @GET
    @Path(ACCEPT_PENDING_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptPending(@HeaderParam("Authorization") String jwt,
                                  @QueryParam("ownerEmail") String ownerEmail,
                                  @QueryParam("toRemoveEmail") String toRemoveEmail) {
        pendingService.acceptRequest(ownerEmail, toRemoveEmail);
        return Response.ok().build();

    }

    @GET
    @Path(NUMBER_OF_PENDING_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptPending(@HeaderParam("Authorization") String jwt,
                                  @QueryParam("ownerEmail") String ownerEmail) {
        Result result  = pendingService.numberOfPending(ownerEmail);
        return Response.ok().entity(result.getContent()).build();

    }
}
