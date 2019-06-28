package restful.registration;

import model.User;
import service.AuthService;
import service.registration.RegistrationService;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static config.Configuration.*;

@Path(ROOT_PATH)
public class RegistrationRestService {

    @EJB
    private RegistrationService registrationService;

    @POST
    @Path(REGISTRATION_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(@HeaderParam("Authorization") String jwt, User user) {
        AuthService auth = new AuthService();

        if (userIsNull(user) || userIsEmpty(user)) {
            Response.status(Response.Status.PARTIAL_CONTENT).build();
        }

        if (auth.isAuthenticated(jwt)) {
            Response.status(Response.Status.ACCEPTED).build();
        }

        return registrationService.register(user);
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
