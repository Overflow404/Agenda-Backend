package restful.registration;

import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import service.registration.RegistrationService;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static config.Configuration.*;

@Path(ROOT_PATH)
public class RegistrationRestService {

    private final static Logger logger = LoggerFactory.getLogger(RegistrationRestService.class);

    @EJB
    private RegistrationService registrationService;

    @POST
    @Path(REGISTRATION_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(@HeaderParam("Authorization") String jwt, User user) {
        logger.info("Registration request arrived!");
        if (userIsNull(user) || userIsEmpty(user)) {
            return Response.status(Response.Status.PARTIAL_CONTENT).build();
        }

        Result result = registrationService.register(user);
        if (result.success()) {
            logger.info("Successful registration!");
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity(result.getFailureReason())
                    .build();
        }
    }

    private boolean userIsNull(User user) {
        return user == null || user.getFirstName() == null || user.getLastName() == null
                || user.getEmail() == null || user.getPassword() == null
                || user.getGmt() == null;
    }

    private boolean userIsEmpty(User user) {
        return user.getFirstName().isBlank() || user.getLastName().isBlank()
                || user.getEmail().isBlank() || user.getPassword().isBlank()
                || user.getGmt().isBlank();
    }
}
