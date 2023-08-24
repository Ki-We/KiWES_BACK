package server.api.kiwes.domain.member.service.login;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import server.api.kiwes.response.BizException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static server.api.kiwes.domain.member.constant.MemberResponseType.CONNECT_ERROR;
import static server.api.kiwes.domain.member.constant.MemberResponseType.NOT_FOUND_EMAIL;
import static server.api.kiwes.domain.member.constant.MemberServiceMessage.GOOGLE_ACOUNT;
import static server.api.kiwes.domain.member.constant.MemberServiceMessage.KAKAO_ACOUNT;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberGoogleService implements MemberLoginService{
    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String GOOGLE_SNS_BASE_URL;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_SNS_REDIRECT_URL;

    @Override
    public String getOauthRedirectURL(String code) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+GOOGLE_SNS_CLIENT_ID); // TODO REST_API_KEY 입력
        sb.append("&redirect_uri="+GOOGLE_SNS_REDIRECT_URL); // TODO 인가코드 받은 redirect_uri 입력
        sb.append("&client_secret="+GOOGLE_SNS_CLIENT_SECRET);
        sb.append("&code=" + code);
        sb.append("&access_type=offline");


        return sb.toString();
    }

    @Override
    public JsonObject connect(String reqURL, String token) {
        try {
            System.out.println(reqURL);
            System.out.println(token);
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer response = new StringBuffer();
            String inputLine;
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
        if (userInfo.getAsJsonObject().has("email")) {
            return userInfo.getAsJsonObject().get("email").getAsString();
        }
        throw new BizException(NOT_FOUND_EMAIL);
    }
    /**
     * saveMember() 할 때
     */
    @Override
    public String getProfileUrl(JsonObject userInfo) {
        return userInfo.getAsJsonObject().get("picture").getAsString();
    }

    /**
     * saveMember() 할 때
     */
    @Override
    public String getGender(JsonObject userInfo) {
        if (userInfo.getAsJsonObject().has("gender")) {
            return userInfo.getAsJsonObject().get("gender").getAsString();
        }
        return "NOTAGREE";
    }


}
