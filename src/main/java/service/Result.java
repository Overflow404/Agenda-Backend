package service;

import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
public class Result<T> {

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
        return new service.Result<T>(content, ResultType.SUCCESS, null);
    }

    public static <T> service.Result failure(String reason) {
        return new service.Result<T>(null, ResultType.FAILURE, reason);
    }

    public boolean success() {
        return resultType.equals(ResultType.SUCCESS);
    }
}
