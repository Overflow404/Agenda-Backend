package restful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import service.auth.AuthService;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.util.List;

import static config.Configuration.*;

@Provider
public class RequestAuthFilter implements ContainerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(RequestAuthFilter.class);


    private AuthService auth = new AuthService();

    @Context
    private UriInfo info;

    @Override
    public void filter(ContainerRequestContext ctx) {
        String path = info.getPath();
        if (isNotARegisterOrLoginOperation(path)) {
            Result result = Result.failure("Not authenticated!");
            Response response = Response.status(Response.Status.UNAUTHORIZED)
                    .entity(result.getFailureReason())
                    .build();

            List<String> tokens = ctx.getHeaders().get("Authorization");

            if (tokens == null) {
                ctx.abortWith(response);
                return;
            }

            if (tokens.size() != 1) {
                ctx.abortWith(response);
                return;
            }

            String jwt = tokens.get(0);

            if (!auth.isAuthenticated(jwt)) {
                ctx.abortWith(response);
            }
        }
    }

    private boolean isNotARegisterOrLoginOperation(String path) {
        return !path.equals(ROOT_PATH + LOGIN_SERVICE_PATH)
                && !path.equals(ROOT_PATH + REGISTRATION_SERVICE_PATH);
    }
}