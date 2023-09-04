package server.api.kiwes.domain.alarm.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.kiwes.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum AlarmResponseType implements BaseResponseType {
    ALARMS(20401, "알림 가져오기 성공",HttpStatus.OK);

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
