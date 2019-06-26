package restful.registration;

import model.User;
import service.registration.RegistrationService;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static config.Configuration.REGISTRATION_SERVICE_PATH;
import static config.Configuration.ROOT_PATH;

@Path(ROOT_PATH)
public class RegistrationRestService {

    @EJB
    private RegistrationService registrationService;

    @POST
    @Path(REGISTRATION_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        if (user == null || userIsEmpty(user)) {
            Response.status(Response.Status.PARTIAL_CONTENT).build();
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
