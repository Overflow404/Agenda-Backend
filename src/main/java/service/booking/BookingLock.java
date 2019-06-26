package service.booking;

import org.apache.commons.lang3.time.DateUtils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

class BookingLock {

    private static BookingLock bookingLock;
    private static ConcurrentHashMap<Date, ReentrantLock> locks;

    static BookingLock getInstance() {
        if (bookingLock == null) {
            locks = new ConcurrentHashMap<>();
            bookingLock = new BookingLock();
        }
        return bookingLock;
    }

    //Performs lock based on day.
    void lock(Date date) {
        Date truncatedDate = DateUtils.truncate(date, Calendar.DATE);
        locks.compute(truncatedDate, (k,v) -> {
            if (v == null) {
                v = new ReentrantLock();
            }
            return v;
        }).lock();
    }

    //Performs unlock based on day.
    void unlock(Date date) {
        Date truncatedDate = DateUtils.truncate(date, Calendar.DATE);
        locks.computeIfPresent(truncatedDate, (k, v) -> {
            v.unlock();
            return v;
        });
    }

}
