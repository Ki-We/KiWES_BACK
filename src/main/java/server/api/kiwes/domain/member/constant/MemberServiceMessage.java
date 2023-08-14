package server.api.kiwes.domain.member.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberServiceMessage {

    LOGIN_URL("https://kapi.kakao.com/v2/user/me"),
    LOGOUT_URL("https://kapi.kakao.com/v1/user/logout"),
    DELETE_URL("https://kapi.kakao.com/v1/user/unlink"),
    KAKAO_ACOUNT("kakao_account"),
    VALID_NICKNAME("가능한 닉네임입니다"),
    EXISTED_NCIKNAME("이미 존재하는 닉네임입니다");
    private final String value;

}
