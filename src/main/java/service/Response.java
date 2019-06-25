package service;

import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
public class Response<T> {
    //TODO mapping with http status code ande xceptions
    static final String BUSY_SLOT = "This slot is busy!";
    static final String START_AFTER_END = "End time must be greater than start time!";
    static final String FREE_SLOT = "This slot is free!";
    static final String NULL_DATA = "Registration data cannot be null!";
    static final String EMPTY_DATA = "Registration data cannot be empty!";
    static final String ALREADY_REGISTERED = "User already registered!";
    static final String SUCCESS_REGISTRATION = "User registered!";

    private Result result;
    private T content;
    private String failureReason;

    public enum Result {
        SUCCESS,
        FAILURE
    }

    private Response(T content, Result result, String failureReason) {
        this.content = content;
        this.result = result;
        this.failureReason = failureReason;
    }

    static <T> Response success(T content) {
        return new Response<T>(content, Result.SUCCESS, null);
    }


    static <T> Response failure(String reason) {
        return new Response<T>(null, Result.FAILURE, reason);
    }

    public boolean success() {
        return result.equals(Result.SUCCESS);
    }

    public T getContent() {
        return content;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
