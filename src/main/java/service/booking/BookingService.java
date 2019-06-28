package service.booking;

import config.Configuration;
import model.Booking;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.ws.rs.core.Response;
import model.User;
import org.apache.http.HttpStatus;
import service.overlapping.OverlappingService;

import static model.User.GET_USER_BY_MAIL;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BookingService {

    @PersistenceContext(unitName = Configuration.UNIT)
    EntityManager manager;

    @Resource(mappedName = "java:comp/TransactionSynchronizationRegistry")
    TransactionSynchronizationRegistry registry;

    @EJB
    OverlappingService overlappingService;

    private BookingLock bookingLock = BookingLock.getInstance();

    public Response book(final Booking booking, String email) {
        bookingLock.lock(booking.getStart());

        BookingSynchronization synchronization = new BookingSynchronization();
        synchronization.booking = booking;

        registry.registerInterposedSynchronization(synchronization);

        Response response = overlappingService.checkIfDatesOverlap(booking.getStart(), booking.getEnd());

        if (response.getStatus() == HttpStatus.SC_OK) {
            User user = getUserByMail(email);
            user.addBooking(booking);
            //manager.persist(booking);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    private User getUserByMail(String email) {
        return manager
                .createNamedQuery(GET_USER_BY_MAIL, User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    public void setOverlappingService(OverlappingService overlappingService) {
        this.overlappingService = overlappingService;
    }

    public void setRegistry(TransactionSynchronizationRegistry registry) {
        this.registry = registry;
    }


}
