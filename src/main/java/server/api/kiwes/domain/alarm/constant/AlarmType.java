package server.api.kiwes.domain.alarm.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {
    NOTICE("NOTICE"),
    EVENT("EVENT"),
    CLUB("CLUB"),
    CHAT("CHAT"),
    ACCESS("ACCESS"),
    ;

    private final String status;
}
