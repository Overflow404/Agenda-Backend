package service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.Dao;
import model.User;
import service.Result;
import service.auth.AuthService;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Optional;

@Stateless
public class LoginService {

    @EJB
    private Dao dao;

    private AuthService auth;

    LoginService(AuthService auth, Dao dao) {
        this.dao = dao;
        this.auth = auth;
    }

    public LoginService() {

    }

    public Result login(User user) {
        Optional<User> realUser = dao.verifyUserRegistered(user);
        if (realUser.isEmpty()) {
            return Result.failure("User not found!");
        }

        try {
            ObjectNode info = auth.createInfo(realUser.get());
            return Result.success(info);
        } catch (JsonProcessingException e) {
            return Result.failure("Error parsing user info!");
        }

    }

}
