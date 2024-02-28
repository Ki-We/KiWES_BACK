package server.api.kiwes.domain.alarm.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlarmContent {
    PARTICIPATE("님이 회원님의 모임에 참여했습니다.", " have participated in your meeting."),
    QUESTION("님이 회원님의 모임에 문의를 남겼습니다.", " have left an inquiry with your group."),
    REVIEW("님이 회원님의 모임에 후기를 남겼습니다.", " have left a review for your meeting."),
    ANSWER("회원님의 문의에 답변이 달렸습니다.", "Your question is answered."),
    REVIEW_ANSWER("회원님의 후기에 답변이 달렸습니다.", "Your review has been answered."),
    APPROVE("회원님의 모임 참여가 승되었습니다. 채팅에서 새로운 모임을 확인해주세요.", "Your participation in the meeting has been approved. Please check the chat for the new meeting."),
    DENY("회원님의 모임 참여가 거절되었습니다.", "Your participation in the meeting has been rejected."),
    KICKOUT("참여 중인 모임에서 강퇴되었습니다.", "You have been kicked out of the meeting you are participating in.");

    private final String contentKo;
    private final String contentEn;

    public String getContent(String language) {
        if ("EN".equals(language)) {
            return contentEn;
        } else {
            return contentKo;
        }
    }
}