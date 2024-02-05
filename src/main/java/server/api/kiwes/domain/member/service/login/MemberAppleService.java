package server.api.kiwes.domain.member.service.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import server.api.kiwes.response.BizException;

import java.io.*;
import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Date;

import static server.api.kiwes.domain.member.constant.MemberResponseType.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberAppleService implements  MemberLoginService{
    @Value("${apple.aud}")
    private String APPLE_CLIENT_ID;
    @Value("${apple.redirect-uri}")
    private String APPLE_REDIRECT_URL;
    @Value("${apple.team-id}")
    private String APPLE_TEAM_ID;
    @Value("${apple.key.id}")
    private String APPLE_LOGIN_KEY;
    @Value("${apple.key.path}")
    private String APPLE_KEY_PATH;

    private final static String APPLE_AUTH_URL = "https://appleid.apple.com";
    @Override
    public String getOauthRedirectURL(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+APPLE_CLIENT_ID);
        sb.append("&redirect_uri="+APPLE_REDIRECT_URL);
        sb.append("&client_secret="+APPLE_LOGIN_KEY);
        sb.append("&code=" + code);

        return sb.toString();
    }
    public JsonObject connect(String reqURL,String code){
        if (code == null) throw new BizException(CONNECT_ERROR);

        try {
            String clientSecret = createClientSecret();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type"   , "authorization_code");
            params.add("client_id"    , APPLE_CLIENT_ID);
            params.add("client_secret", clientSecret);
            params.add("code"         , code);
            params.add("redirect_uri" , APPLE_REDIRECT_URL);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    reqURL + "/auth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            return JsonParser.parseString(response.getBody()).getAsJsonObject();

        } catch (IOException | ParseException | org.json.simple.parser.ParseException e) {
            log.info(CONNECT_ERROR.getMessage());
            throw new BizException(CONNECT_ERROR);
        } catch (Exception e) {
            log.info(CANT_CREATE_SECRET.getMessage());
            throw new BizException(CANT_CREATE_SECRET);
        }


    }

    private String createClientSecret() throws Exception {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(APPLE_LOGIN_KEY).build();
        JWTClaimsSet claimsSet = new JWTClaimsSet();

        Date now = new Date();
        claimsSet.setIssuer(APPLE_TEAM_ID);
        claimsSet.setIssueTime(now);
        claimsSet.setExpirationTime(new Date(now.getTime() + 3600000));
        claimsSet.setAudience(APPLE_AUTH_URL);
        claimsSet.setSubject(APPLE_CLIENT_ID);

        SignedJWT jwt = new SignedJWT(header, claimsSet);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(getPrivateKey());
        KeyFactory kf = KeyFactory.getInstance("EC");
        try {
            ECPrivateKey ecPrivateKey = (ECPrivateKey) kf.generatePrivate(spec);
            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getS());
            jwt.sign(jwsSigner);
        } catch (JOSEException e) {
            log.info(CANT_CREATE_SECRET.getMessage());
            throw new BizException(CANT_CREATE_SECRET);
        }

        return jwt.serialize();
    }

    private byte[] getPrivateKey() {
        byte[] content = null;
        File file = null;

        URL res = getClass().getResource(APPLE_KEY_PATH);
        if ("jar".equals(res.getProtocol())) {
            try {
                InputStream input = getClass().getResourceAsStream(APPLE_KEY_PATH);
                file = File.createTempFile("tempfile", ".tmp");
                OutputStream out = new FileOutputStream(file);

                int read;
                byte[] bytes = new byte[1024];

                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                out.close();
                file.deleteOnExit();
            } catch (IOException ex) {
                throw new BizException(NOT_FOUND_KEYFILE);
            }
        } else {
            file = new File(res.getFile());
        }

        if (file.exists()) {
            try (FileReader keyReader = new FileReader(file);
                 PemReader pemReader = new PemReader(keyReader)){
                PemObject pemObject = pemReader.readPemObject();
                content = pemObject.getContent();
            } catch (IOException e) {
                throw new BizException(NOT_FOUND_KEYFILE);
            }
        } else {
            throw new BizException(NOT_FOUND_KEYFILE);
        }

        return content;
    }

    @Override
    public String getEmail(JsonObject userInfo) {
        return String.valueOf(getIdentifier(userInfo).get("email"));
    }
    @Override
    public String getProfileUrl(JsonObject userInfo) {
        return "NOTAGREE";
    }
    @Override
    public String getGender(JsonObject userInfo) {
        return "NOTAGREE";
    }

    public JSONObject getIdentifier(JsonObject userInfo) {
        try {
            //ID TOKEN을 통해 회원 고유 식별자 받기
            SignedJWT signedJWT = SignedJWT.parse(String.valueOf(userInfo.get("id_token")));
            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
            ObjectMapper objectMapper = new ObjectMapper(){};
            JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), JSONObject.class);
            return payload;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
