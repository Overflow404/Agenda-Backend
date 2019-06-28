package service.retrieve;

import config.Configuration;
import model.Booking;
import model.User;
import service.AuthService;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;

import static model.Booking.GET_BOOKINGS_FROM_DATE_AND_USER;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RetrieveBookingsService {

    @PersistenceContext(unitName = Configuration.UNIT)
    EntityManager manager;

    public Response retrieve(int day, int month, int year, String email) {

        List<Booking> bookings = manager
                .createNamedQuery(GET_BOOKINGS_FROM_DATE_AND_USER, Booking.class)
                .setParameter("inputDay", day)
                .setParameter("inputMonth", month)
                .setParameter("inputYear", year)
                .setParameter("email", email)
                .getResultList();

        return Response.ok().entity(bookings).build();

    }


}
