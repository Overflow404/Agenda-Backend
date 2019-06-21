package service.booking;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.annotation.XmlRootElement;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@XmlRootElement(name="booking")
public class BookingService {

    @PersistenceContext(unitName = "agendaUnit")
    EntityManager entityManager;


}
