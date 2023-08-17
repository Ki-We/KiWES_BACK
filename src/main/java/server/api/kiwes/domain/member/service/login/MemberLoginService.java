package server.api.kiwes.domain.member.service.login;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.member.constant.SocialLoginType;

@Service
@Transactional
public interface MemberLoginService {

    public JsonObject connect(String reqURL, String token);

    /**
     * saveMember() 할 때
     */
    public String getEmail(JsonObject userInfo);
    /**
     * saveMember() 할 때
     */
    public String getProfileUrl(JsonObject userInfo);

    /**
     * saveMember() 할 때
     */
    public String getGender(JsonObject userInfo);

    default SocialLoginType type() {
        if (this instanceof MemberGoogleService) {
            return SocialLoginType.GOOGLE;
        } else if (this instanceof MemberKakaoService) {
            return SocialLoginType.KAKAO;
        } else if (this instanceof MemberAppleService) {
            return SocialLoginType.APPLE;
        }  else {
            return null;
        }
    }
}
