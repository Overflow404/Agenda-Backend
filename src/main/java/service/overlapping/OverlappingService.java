package service.overlapping;

import model.Booking;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.List;

import static model.Booking.OVERLAPPING;
import static service.overlapping.OverlappingResult.BUSY_SLOT;
import static service.overlapping.OverlappingResult.START_AFTER_END;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@XmlRootElement(name="booking")
public class OverlappingService {

    @PersistenceContext(unitName = "agendaUnit")
    EntityManager entityManager;

    public OverlappingResult checkIfDatesOverlap(Timestamp startMillisDate, Timestamp endMillisDate) {
        if (startMillisDate.after(endMillisDate)) return OverlappingResult.failure(START_AFTER_END);

        List<Booking> bookings = entityManager
                .createNamedQuery(OVERLAPPING, Booking.class)
                .setParameter("inputStartDate", startMillisDate)
                .setParameter("inputEndDate", endMillisDate)
                .getResultList();
        if (bookings.isEmpty()) {
            entityManager.persist(new Booking("subject", "description", startMillisDate, endMillisDate));
            return OverlappingResult.success("This slot is free!");
        } else {
            return OverlappingResult.failure(BUSY_SLOT);
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
