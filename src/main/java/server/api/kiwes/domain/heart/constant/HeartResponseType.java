package server.api.kiwes.domain.heart.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.kiwes.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum HeartResponseType implements BaseResponseType {
    HEART_SUCCEED(20201, "찜하기 성공",HttpStatus.OK),
    UNHEART_SUCCEED(20202, "찜하기 취소 성공",HttpStatus.OK),

    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
