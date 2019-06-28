package restful.login;


import model.User;
import service.AuthService;
import service.login.LoginService;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static config.Configuration.*;

@Path(ROOT_PATH)
public class LoginRestService {

    @EJB
    private LoginService loginService;

    @POST
    @Path(LOGIN_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("Authorization") String jwt, User user) {

        AuthService auth = new AuthService();

        if (userIsNull(user) || userIsEmpty(user)) {
            Response.status(Response.Status.PARTIAL_CONTENT).build();
        }

        if (auth.isAuthenticated(jwt)) {
            Response.status(Response.Status.ACCEPTED).build();
        }

        return loginService.login(user);
    }

    private boolean userIsNull(User user) {
        return user == null || user.getEmail() == null || user.getPassword() == null;
    }

    private boolean userIsEmpty(User user) {
        return user.getEmail().isBlank() || user.getPassword().isBlank();
    }
}
