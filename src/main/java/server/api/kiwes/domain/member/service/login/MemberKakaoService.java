package server.api.kiwes.domain.member.service.login;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class MemberKakaoService implements  MemberLoginService{
    @Override
    public String getOauthRedirectURL(String code) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id=93df5ea9a1445313343f4bb0f1d362ce"); // TODO REST_API_KEY 입력
        sb.append("&redirect_uri=http://43.200.185.205:8080/oauth/kakao"); // TODO 인가코드 받은 redirect_uri 입력
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
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

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
        // TODO 카카오 비즈 전환 해야하는?
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
