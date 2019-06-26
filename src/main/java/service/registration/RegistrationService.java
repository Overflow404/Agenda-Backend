package service.registration;

import config.Configuration;
import model.User;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistrationService {

    @PersistenceContext(unitName = Configuration.UNIT)
    EntityManager manager;

    public Response register(User user) {
        try {
            System.out.println(user.getEmail());
            manager.persist(user);
            manager.flush();
            return Response.ok().build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

}
