package service;

import lock.UserLock;
import model.user.User;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;
import java.util.List;

import static model.user.User.QUERY_FIND_USER;
import static service.Response.*;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistrationService {

    @PersistenceContext(unitName = "agendaUnit")
    EntityManager entityManager;

    @Resource(mappedName = "java:comp/TransactionSynchronizationRegistry")
    TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    private UserLock userLock = UserLock.getInstance();

    public RegistrationService() {

    }

    public Response register(User user) {
        transactionSynchronizationRegistry.registerInterposedSynchronization(new Synchronization() {
            @Override
            public void beforeCompletion() {

            }

            @Override
            public void afterCompletion(int status) {
                userLock.unlock(user.getEmail());
            }
        });

        if (dataIsNull(user)) {
            return Response.failure(NULL_DATA);
        }

        if (dataIsEmpty(user)) {
            return Response.failure(EMPTY_DATA);
        }

        userLock.lock(user.getEmail());

        if(alreadyRegistered(user)) {
            return Response.failure(ALREADY_REGISTERED);
        }

        registerUser(user);
        return Response.success(SUCCESS_REGISTRATION);
    }

    private boolean dataIsNull(User user) {
        return user == null || user.getFirstName() == null || user.getLastName() == null
                || user.getEmail() == null || user.getPassword() == null || user.getGmt() == null;
    }

    private boolean dataIsEmpty(User user) {
        return user.getFirstName().isBlank() || user.getLastName().isBlank()
                || user.getEmail().isBlank() || user.getPassword().isBlank()
                || user.getGmt().isBlank();
    }

    private boolean alreadyRegistered(User user) {
        List<User> users = entityManager
                .createNamedQuery(QUERY_FIND_USER, User.class)
                .setParameter("email", user.getEmail())
                .getResultList();
        return !users.isEmpty();
    }

    private void registerUser(User user) {
        entityManager.persist(user);
    }

}
