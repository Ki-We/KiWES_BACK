package server.api.kiwes.response;

import org.springframework.http.HttpStatus;

public interface BaseResponseType {
    Integer getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
