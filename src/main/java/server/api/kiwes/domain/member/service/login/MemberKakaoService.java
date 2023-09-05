package server.api.kiwes.domain.member.service.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.response.BizException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static server.api.kiwes.domain.member.constant.MemberResponseType.CONNECT_ERROR;
import static server.api.kiwes.domain.member.constant.MemberResponseType.NOT_FOUND_EMAIL;
import static server.api.kiwes.domain.member.constant.MemberServiceMessage.KAKAO_ACOUNT;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberKakaoService implements MemberLoginService{
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_SNS_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_SNS_SECRET_URL;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_SNS_REDIRECT_URL;
    @Override
    public String getOauthRedirectURL(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+KAKAO_SNS_CLIENT_ID);
        sb.append("&redirect_uri="+KAKAO_SNS_REDIRECT_URL);
        sb.append("&client_secret="+KAKAO_SNS_SECRET_URL);
        sb.append("&code=" + code);

        return sb.toString();
    }

    /**
     *
     * 카카오 연결
     */
    public JsonObject connect(String reqURL, String token) {
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;

            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();
            return json;
        } catch (IOException e) {
            log.info(CONNECT_ERROR.getMessage());
            throw new BizException(CONNECT_ERROR);
        }

    }

    /**
     * saveMember() 할 때
     */
    @Override
    public String getEmail(JsonObject userInfo) {
        if (userInfo.getAsJsonObject(KAKAO_ACOUNT.getValue()).has("email")) {
            return userInfo.getAsJsonObject(KAKAO_ACOUNT.getValue()).get("email").getAsString();
        }
        throw new BizException(NOT_FOUND_EMAIL);
    }
    /**
     * saveMember() 할 때
     */
    @Override
    public String getProfileUrl(JsonObject userInfo) {
        return userInfo.getAsJsonObject("properties").get("profile_image").getAsString();
    }

    /**
     * saveMember() 할 때
     */
    @Override
    public String getGender(JsonObject userInfo) {
        if (userInfo.getAsJsonObject(KAKAO_ACOUNT.getValue()).has("gender")) {
            return userInfo.getAsJsonObject(KAKAO_ACOUNT.getValue()).get("gender").getAsString();
        }
        return "NOTAGREE";
    }


}
