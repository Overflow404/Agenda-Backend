package service.booking;

import model.Booking;

import javax.transaction.Synchronization;

public class BookingSynchronization implements Synchronization {

    private BookingLock bookingLock = BookingLock.getInstance();

    Booking booking;

    @Override
    public void beforeCompletion() {

    }

    @Override
    public void afterCompletion(int status) {
        bookingLock.unlock(booking.getStart());
    }

}
