package server.api.kiwes.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.kiwes.response.BaseResponseType;

@Getter
public class BizException extends RuntimeException{
    private final BaseResponseType baseExceptionType;

    public BizException(BaseResponseType baseExceptionType){
        super(baseExceptionType.getMessage());
        this.baseExceptionType = baseExceptionType;
    }

}
