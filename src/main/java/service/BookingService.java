package service;

import model.booking.Booking;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;
import lock.DateLock;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BookingService {

    @PersistenceContext(unitName = "agendaUnit")
    EntityManager entityManager;

    @Resource(mappedName = "java:comp/TransactionSynchronizationRegistry")
    TransactionSynchronizationRegistry transactionSynchronizationRegistry;

    @EJB
    OverlappingService overlappingService;

    private DateLock dateLock = DateLock.getInstance();

    public Response book(final Booking booking) {
        dateLock.lock(booking.getStart());

        transactionSynchronizationRegistry.registerInterposedSynchronization(new Synchronization() {
            @Override
            public void beforeCompletion() {

            }

            @Override
            public void afterCompletion(int status) {
                dateLock.unlock(booking.getStart());
            }
        });


        Response overlappingResponse =
                overlappingService.checkIfDatesOverlap(booking.getStart(), booking.getEnd());

        if (overlappingResponse.success()) {
            entityManager.persist(booking);
            return Response.success("Booking confirmed!");
        } else {
            return Response.failure("Something went wrong!");
        }
    }

}
