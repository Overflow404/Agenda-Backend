package service;

import model.booking.Booking;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;


import static model.booking.Booking.OVERLAPPING;
import static service.Response.*;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OverlappingService {

    @PersistenceContext(unitName = "agendaUnit")
    EntityManager entityManager;

    public Response checkIfDatesOverlap(Date startMillisDate, Date endMillisDate) {
        if (startMillisDate.after(endMillisDate) || startMillisDate.equals(endMillisDate)) {
            return Response.failure(START_AFTER_END);
        }

        List<Booking> bookings = entityManager
                .createNamedQuery(OVERLAPPING, Booking.class)
                .setParameter("inputStartDate", startMillisDate)
                .setParameter("inputEndDate", endMillisDate)
                .getResultList();

        if (bookings.isEmpty()) {
            return Response.success(FREE_SLOT);
        } else {
            return Response.failure(BUSY_SLOT);
        }
    }


}
