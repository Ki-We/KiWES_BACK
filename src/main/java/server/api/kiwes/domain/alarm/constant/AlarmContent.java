package server.api.kiwes.domain.alarm.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmContent {
    PARTICIPATE("님이 회원님의 모임에 참여했습니다."),
    QUESTION("님이 회원님의 모임에 문의를 남겼습니다."),
    REVIEW("님이 회원님의 모임에 후기를 남겼습니다."),
    ANSWER("회원님의 문의에 답변이 달렸습니다."),
    REVIEW_ANSWER("회원님의 후기에 답변이 달렸습니다."),
    APPROVE("회원님의 모임 참여가 승인되었습니다. 채팅에서 새로운 모임을 확인해주세요."),
    DENY("회원님의 모임 참여가 거절되었습니다."),
    KICKOUT("참여 중인 모임에서 강퇴되었습니다."),
    ;

    private final String content;
}
