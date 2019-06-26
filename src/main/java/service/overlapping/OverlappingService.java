package service.overlapping;

import config.Configuration;
import model.Booking;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.Date;

import static model.Booking.OVERLAPPING;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OverlappingService {

    @PersistenceContext(unitName = Configuration.UNIT)
    private EntityManager manager;

    public Response checkIfDatesOverlap(Date start, Date end) {
        if (endTimeIsGreaterThanStartTime(start, end)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            manager
                    .createNamedQuery(OVERLAPPING, Booking.class)
                    .setParameter("inputStartDate", start)
                    .setParameter("inputEndDate", end)
                    .getSingleResult();

            return Response.status(Response.Status.CONFLICT).build();
        } catch (NoResultException e) {
            return Response.ok().build();
        }
    }

    private boolean endTimeIsGreaterThanStartTime(Date start, Date end) {
        return start.after(end) || start.equals(end);
    }

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }
}
