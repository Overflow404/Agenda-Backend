package service;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter @Setter
public class Result<T> {

    private final static Logger logger = LoggerFactory.getLogger(Result.class);


    private ResultType resultType;
    private T content;
    private String failureReason;

    public enum ResultType {
        SUCCESS,
        FAILURE
    }

    private Result(T content, ResultType resultType, String failureReason) {
        this.content = content;
        this.resultType = resultType;
        this.failureReason = failureReason;
    }

    public static <T> service.Result success(T content) {
        return new service.Result<>(content, ResultType.SUCCESS, null);
    }

    public static <T> service.Result failure(String reason) {
        return new service.Result<T>(null, ResultType.FAILURE, reason);
    }

    public boolean success() {
        return resultType.equals(ResultType.SUCCESS);
    }
}
