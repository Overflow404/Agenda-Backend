package service.retrieve;

import config.Configuration;
import model.Booking;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.List;

import static model.Booking.GET_BOOKING_FROM_DATE;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RetrieveBookingsService {

    @PersistenceContext(unitName = Configuration.UNIT)
    EntityManager manager;

    public Response retrieve(int day, int month, int year) {

        List<Booking> bookings = manager
                .createNamedQuery(GET_BOOKING_FROM_DATE, Booking.class)
                .setParameter("inputDay", day)
                .setParameter("inputMonth", month)
                .setParameter("inputYear", year)
                .getResultList();

        System.out.println(bookings);

        return Response.ok().entity(bookings).build();

    }


}
