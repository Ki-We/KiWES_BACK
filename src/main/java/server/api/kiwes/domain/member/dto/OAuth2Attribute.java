package server.api.kiwes.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import server.api.kiwes.domain.member.entity.Member;

import java.util.HashMap;
import java.util.Map;

import static server.api.kiwes.domain.member.constant.Role.ROLE_USER;

@Getter
@Builder
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String picture;

    public static OAuth2Attribute of(String provider, String attributeKey,
                                     Map<String, Object> attributes) {
        switch (provider) {
            case "kakao":
                return ofKakao("id", attributes);
            case "google":
                return ofGoogle("id", attributes);
            default:
                throw new RuntimeException();
        }
    }


    private static OAuth2Attribute ofKakao(String attributeKey,
                                           Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .picture((String)attributes.get("profile_image_url"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }
    private static OAuth2Attribute ofGoogle(String attributeKey,
                                           Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email((String) attributes.get("email"))
                .picture((String)attributes.get("profile_image_url"))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("picture", picture);

        return map;
    }
}
