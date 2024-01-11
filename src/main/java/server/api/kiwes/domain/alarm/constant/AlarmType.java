package server.api.kiwes.domain.alarm.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {
    NOTICE("NOTICE"),
    EVENT("EVENT"),
//    QNA("QNA"),
//    REVIEW("REVIEW"),
    CLUB("CLUB"),
    REQUSET("REQUSET"),
    CHAT("CHAT"),
    ACCESS("ACCESS"),
    ;

    private final String status;
}
