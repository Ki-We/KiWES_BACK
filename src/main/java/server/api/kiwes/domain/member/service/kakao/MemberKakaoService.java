package server.api.kiwes.domain.member.service.kakao;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.response.BizException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static server.api.kiwes.domain.member.constant.MemberResponseType.KAKAO_CONNECT_ERROR;
import static server.api.kiwes.domain.member.constant.MemberResponseType.NOT_FOUND_EMAIL;
import static server.api.kiwes.domain.member.constant.MemberServiceMessage.KAKAO_ACOUNT;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberKakaoService {

    /**
     *
     * 카카오 연결
     */
    public JsonObject connectKakao(String reqURL, String token) {
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
            log.info(KAKAO_CONNECT_ERROR.getMessage());
            throw new BizException(KAKAO_CONNECT_ERROR);
        }

    }

    /**
     * saveMember() 할 때
     */
    public String getEmail(JsonObject userInfo) {
        if (userInfo.getAsJsonObject(KAKAO_ACOUNT.getValue()).get("has_email").getAsBoolean()) {
            return userInfo.getAsJsonObject(KAKAO_ACOUNT.getValue()).get("email").getAsString();
        }
        throw new BizException(NOT_FOUND_EMAIL);
    }
    /**
     * saveMember() 할 때
     */
    public String getProfileUrl(JsonObject userInfo) {
        return userInfo.getAsJsonObject("properties").get("profile_image").getAsString();
    }

    /**
     * saveMember() 할 때
     */
    public String getGender(JsonObject userInfo) {
        if (userInfo.getAsJsonObject(KAKAO_ACOUNT.getValue()).get("has_gender").getAsBoolean() &&
                !userInfo.getAsJsonObject(KAKAO_ACOUNT.getValue()).get("gender_needs_agreement").getAsBoolean()) {
            return userInfo.getAsJsonObject(KAKAO_ACOUNT.getValue()).get("gender").getAsString();
        }
        return "동의안함";
    }


}
