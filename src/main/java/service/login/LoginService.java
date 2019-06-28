package service.login;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.Dao;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import model.User;
import service.Result;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.Optional;

import static config.Configuration.JWT_KEY;

@Stateless
public class LoginService {

    @EJB
    Dao dao;

    public Result login(User user) {
        Optional<User> realUser = dao.verifyUserRegistered(user);
        if (realUser.isEmpty()) {
            return Result.failure("User not found!");
        }

        ObjectNode jwt = createJwt(realUser.get());
        return Result.success(jwt);
    }

    private ObjectNode createJwt(User user) {
        long currentTime = System.currentTimeMillis();

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + 60_000))
                .claim("email", user.getEmail())
                .compact();

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = factory.objectNode();
        root.put("jwt", jwt);
        root.put("group", user.getGroupName());
        return root;
    }
}
