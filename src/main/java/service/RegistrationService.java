package service;

import model.user.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RegistrationService {

    @PersistenceContext
    EntityManager entityManager;


    public Response register(User user) {


        return null;
    }

}
