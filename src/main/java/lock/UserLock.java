package lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class UserLock {

    private static UserLock userLock;
    private static ConcurrentHashMap<Integer, ReentrantLock> locks;
    private static final Integer SLOT = 128;

    public static UserLock getInstance() {
        if (userLock == null) {
            locks = new ConcurrentHashMap<>();
            userLock = new UserLock();
        }
        return userLock;
    }

    //Performs lock based on (sum_i{[email.char(i)]}) % SLOT
    public void lock(String email) {
        Integer position = position(email);
        locks.compute(position, (k,v) -> {
            if (v == null) {
                v = new ReentrantLock();
            }
            return v;
        }).lock();

    }

    //Performs unlock based on (sum_i{[email.char(i)]}) % SLOT
    public void unlock(String email) {
        Integer position = position(email);
        locks.computeIfPresent(position, (k, v) -> {
            v.unlock();
            return v;
        });
    }

    private Integer position(String email) {
        Integer sum = 0;
        for (int i = 0; i < email.length(); i++) {
            sum = sum + email.charAt(i);
        }

        return sum % SLOT;
    }
}
