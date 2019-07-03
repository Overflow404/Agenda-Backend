package service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.Dao;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Result;
import service.auth.AuthService;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Optional;

@Stateless
public class LoginService {

    private final static Logger logger = LoggerFactory.getLogger(LoginService.class);


    @EJB
    private Dao dao;

    private AuthService auth;

    LoginService(AuthService auth, Dao dao) {
        this.dao = dao;
        this.auth = auth;
    }

    public LoginService() {
        auth = new AuthService();
    }

    public Result login(User user) {
        Optional<User> realUser = dao.verifyUserRegistered(user);
        if (realUser.isEmpty()) {
            return Result.failure("User not found!");
        }

        if (realUser.get().isPending()) {
            return Result.failure("You have to wait the approvation!");
        }

        try {
            ObjectNode info = auth.createInfo(realUser.get());
            return Result.success(info);
        } catch (JsonProcessingException e) {
            return Result.failure("Error parsing user info!");
        }

    }

}
