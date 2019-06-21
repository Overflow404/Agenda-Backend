package service.booking;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter @Setter
public class BookingResult {

    private static final String NULL_DATA = "Booking data cannot be null!";
    private static final String EMPTY_DATA = "Booking data cannot be empty!";
    private static final String ALREADY_BOOKED = "This time slot is already booked!";

    private Result result;
    private String content;
    private String failureReason;

    public enum Result {
        SUCCESS,
        FAILURE
    }

    private BookingResult(String content, Result result, String failureReason) {
        this.content = content;
        this.result = result;
        this.failureReason = failureReason;
    }

    public static BookingResult success(String content) {
        return new BookingResult(content, Result.SUCCESS, null);
    }


    public static BookingResult failure(String reason) {
        return new BookingResult(null, Result.FAILURE, reason);
    }

    public boolean success() {
        return result.equals(Result.SUCCESS);
    }

    public String getContent() {
        return content;
    }

    public String getFailureReason() {
        return failureReason;
    }

}
