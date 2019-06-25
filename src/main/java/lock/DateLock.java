package lock;

import org.apache.commons.lang3.time.DateUtils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class DateLock {

    private static DateLock dateLock;
    private static ConcurrentHashMap<Date, ReentrantLock> locks;

    public static DateLock getInstance() {
        if (dateLock == null) {
            locks = new ConcurrentHashMap<>();
            dateLock = new DateLock();
        }
        return dateLock;
    }

    //Performs lock based on day.
    public void lock(Date date) {
        Date truncatedDate = DateUtils.truncate(date, Calendar.DATE);
        locks.compute(truncatedDate, (k,v) -> {
            if (v == null) {
                v = new ReentrantLock();
            }
            return v;
        }).lock();

    }

    //Performs unlock based on day.
    public void unlock(Date date) {
        Date truncatedDate = DateUtils.truncate(date, Calendar.DATE);
        locks.computeIfPresent(truncatedDate, (k, v) -> {
            v.unlock();
            return v;
        });
    }

}

