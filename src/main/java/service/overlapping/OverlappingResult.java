package service.overlapping;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
public class OverlappingResult {

    static final String BUSY_SLOT = "This slot is busy!";
    static final String START_AFTER_END = "End time must be greater than start time!";

    private Result result;
    private String content;
    private String failureReason;

    public enum Result {
        SUCCESS,
        FAILURE
    }

    private OverlappingResult(String content, Result result, String failureReason) {
        this.content = content;
        this.result = result;
        this.failureReason = failureReason;
    }

    public static OverlappingResult success(String content) {
        return new OverlappingResult(content, Result.SUCCESS, null);
    }


    static OverlappingResult failure(String reason) {
        return new OverlappingResult(null, Result.FAILURE, reason);
    }

    boolean success() {
        return result.equals(Result.SUCCESS);
    }

    public String getContent() {
        return content;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
