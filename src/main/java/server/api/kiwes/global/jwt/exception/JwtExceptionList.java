package server.api.kiwes.global.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.kiwes.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum JwtExceptionList implements BaseResponseType {

    UNKNOWN_ERROR(40301,"예상치 못한 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MAL_FORMED_TOKEN(40302,"잘못된 JWT 서명입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(40303,"만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_TOKEN(40304, "지원되지 않는 토큰입니다.",HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(40305, "접근이 거부되었습니다.",HttpStatus.UNAUTHORIZED),
    ILLEGAL_TOKEN(40306, "JWT 토큰이 잘못되었습니다.",HttpStatus.UNAUTHORIZED),
    ADDITIONAL_REQUIRED_TOKEN(40307, "추가 정보를 입력해야 합니다.",HttpStatus.UNAUTHORIZED);

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;


    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}

