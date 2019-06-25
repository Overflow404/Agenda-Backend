package service;

import model.booking.Booking;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static model.booking.Booking.ALL;
import static model.booking.Booking.GET_FROM_DATE;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RetrieveBookingsService {

    @PersistenceContext(unitName = "agendaUnit")
    EntityManager entityManager;

    public Response retrieve(String d, String m, String y) {
        try {
            Integer day = Integer.parseInt(d);
            Integer month = Integer.parseInt(m);
            Integer year = Integer.parseInt(y);

            List<Booking> bookings = entityManager
                    .createNamedQuery(GET_FROM_DATE, Booking.class)
                    .setParameter("inputDay", day)
                    .setParameter("inputMonth", month)
                    .setParameter("inputYear", year)
                    .getResultList();

            return Response.success(bookings);

        } catch (NumberFormatException e) {
            return Response.failure("Day, month or year is not a number!");
        }
    }

    public Response retrieveAll() {
        List<Booking> bookings = entityManager
                .createNamedQuery(ALL, Booking.class)
                .getResultList();
        return Response.success(bookings.size());
    }

}
