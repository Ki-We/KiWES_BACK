package server.api.kiwes.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BizException.class)
    public ResponseEntity<ApiResponse> bizException(BizException e){
        return new ResponseEntity<>(ApiResponse.of(e.getBaseExceptionType()), e.getBaseExceptionType().getHttpStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> notResolvedException(Exception e){
        log.error("[{}] - {}", e.getStackTrace()[0], e.getMessage());
        return new ResponseEntity<>(ApiResponse.of(InternalServerExceptionType.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
