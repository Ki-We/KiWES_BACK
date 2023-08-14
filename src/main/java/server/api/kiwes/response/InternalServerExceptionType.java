package server.api.kiwes.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.kiwes.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum InternalServerExceptionType implements BaseResponseType {
    INTERNAL_SERVER_ERROR(50000, "서버 내부에 문제가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

}
