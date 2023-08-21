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
//        Map<String, Object> params = new HashMap<>();
//        params.put("scope", "profile");
//        params.put("response_type", "code");
//        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
//        params.put("redirect_uri", GOOGLE_SNS_REDIRECT_URL);
//
//        String parameterString = params.entrySet().stream()
//                .map(x -> x.getKey() + "=" + x.getValue())
//                .collect(Collectors.joining("&"));
//        return GOOGLE_SNS_BASE_URL + "?" + parameterString;

//        Map<String, Object> params = new HashMap<>();
//        params.put("code", code);
//        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
//        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
//        params.put("redirect_uri", GOOGLE_SNS_REDIRECT_URL);
//        params.put("grant_type", "authorization_code");
//
//        String parameterString = params.entrySet().stream()
//                .map(x -> x.getKey() + "=" + x.getValue())
//                .collect(Collectors.joining("&"));
//
//        return parameterString;

        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+GOOGLE_SNS_CLIENT_ID); // TODO REST_API_KEY 입력
        sb.append("&redirect_uri="+GOOGLE_SNS_REDIRECT_URL); // TODO 인가코드 받은 redirect_uri 입력
        sb.append("&client_secret="+GOOGLE_SNS_CLIENT_SECRET);
        sb.append("&code=" + code);

        return sb.toString();
    }

    @Override
    public JsonObject connect(String reqURL, String token) {
        try {
            System.out.println(reqURL);
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setDoOutput(true);

            Map<String, Object> params = new HashMap<>();
            params.put("code", token);
            params.put("client_id", GOOGLE_SNS_CLIENT_ID);
            params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
            params.put("redirect_uri", GOOGLE_SNS_REDIRECT_URL);
            params.put("grant_type", "authorization_code");

            String parameterString = params.entrySet().stream()
                    .map(x -> x.getKey() + "=" + x.getValue())
                    .collect(Collectors.joining("&"));

            BufferedOutputStream bous = new BufferedOutputStream(conn.getOutputStream());
            bous.write(parameterString.getBytes());
            bous.flush();
            bous.close();

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
        if (userInfo.getAsJsonObject(GOOGLE_ACOUNT.getValue()).get("has_email").getAsBoolean()) {
            return userInfo.getAsJsonObject(GOOGLE_ACOUNT.getValue()).get("email").getAsString();
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
        if (userInfo.getAsJsonObject(GOOGLE_ACOUNT.getValue()).get("has_gender").getAsBoolean() &&
                !userInfo.getAsJsonObject(GOOGLE_ACOUNT.getValue()).get("gender_needs_agreement").getAsBoolean()) {
            return userInfo.getAsJsonObject(GOOGLE_ACOUNT.getValue()).get("gender").getAsString();
        }
        return "동의안함";
    }


}
