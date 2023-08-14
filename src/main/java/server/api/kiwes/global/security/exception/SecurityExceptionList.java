package server.api.kiwes.global.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.kiwes.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum SecurityExceptionList implements BaseResponseType {

    NO_AUTHENTICATION_FOUND(40351, "보안 컨텍스트에서 인증 정보를 찾을 수 없습니다.",HttpStatus.UNAUTHORIZED ),
    NOT_AUTH_WITH_OAUTH2(40352, "현재 사용자는 OAuth2를 사용하여 인증되지 않았습니다.",HttpStatus.UNAUTHORIZED),
    LOGGED_IN_NOT_FOUND(40353, "로그인한 사용자를 찾을 수 없습니다.",HttpStatus.UNAUTHORIZED ),
    REQUIRED_LOGGED_IN(40351, "해당 서비스는 로그인이 필요한 서비스입니다.",HttpStatus.UNAUTHORIZED );
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

