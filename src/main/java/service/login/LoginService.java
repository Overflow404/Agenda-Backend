package service.login;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import config.Configuration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import model.User;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

import java.util.Date;

import static config.Configuration.JWT_KEY;
import static model.User.IS_REGISTERED;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LoginService {

    @PersistenceContext(unitName = Configuration.UNIT)
    EntityManager manager;

    public Response login(User user) {
        try {
            verifyUser(user);
            ObjectNode jwt = createJwt(user);
            return Response.ok().entity(jwt).build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private void verifyUser(User user) throws NoResultException {
        String email = user.getEmail();
        String password = user.getPassword();

        manager
                .createNamedQuery(IS_REGISTERED, User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

    private ObjectNode createJwt(User user) {
        long currentTime = System.currentTimeMillis();

        String jwt =  Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + 60_000))
                .claim("email", user.getEmail())
                .compact();

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = factory.objectNode();
        root.put("jwt", jwt);
        return root;
    }
}
