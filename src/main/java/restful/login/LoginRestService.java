package restful.login;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import service.login.LoginService;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static config.Configuration.*;

@Path(ROOT_PATH)
public class LoginRestService {

    private final static Logger logger = LoggerFactory.getLogger(LoginRestService.class);


    @EJB
    private LoginService loginService;

    @POST
    @Path(LOGIN_SERVICE_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("Authorization") String jwt, User user) {

        if (userIsNull(user) || userIsEmpty(user)) {
            return Response.status(Response.Status.PARTIAL_CONTENT).build();
        }

        Result result = loginService.login(user);

        if (result.success()) {
            return Response.ok().entity(result.getContent()).build();
        }

        return Response.status(Response.Status.PRECONDITION_FAILED)
                .entity(result.getFailureReason())
                .build();
    }

    private boolean userIsNull(User user) {
        return user == null || user.getEmail() == null || user.getPassword() == null;
    }

    private boolean userIsEmpty(User user) {
        return user.getEmail().isBlank() || user.getPassword().isBlank();
    }
}
