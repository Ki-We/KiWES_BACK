package server.api.kiwes.domain.member.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberServiceMessage {

    KAKAO_LOGIN_URL("https://kapi.kakao.com/v2/user/me"),
    KAKAO_LOGOUT_URL("https://kapi.kakao.com/v1/user/logout"),
    KAKAO_DELETE_URL("https://kapi.kakao.com/v1/user/unlink"),
    KAKAO_ACOUNT("kakao_account"),

    GOOGLE_LOGIN_URL("https://www.googleapis.com/oauth2/v1/userinfo"),
    GOOGLE_LOGOUT_URL(""),
    GOOGLE_DELETE_URL(""),
    GOOGLE_ACOUNT("google_account"),

    APPLE_LOGIN_URL("https://appleid.apple.com/"),
    APPLE_LOGOUT_URL(""),
    APPLE_DELETE_URL(""),
    APPLE_ACOUNT("apple_account"),

    VALID_NICKNAME("가능한 닉네임입니다"),
    EXISTED_NCIKNAME("이미 존재하는 닉네임입니다");
    private final String value;

}
