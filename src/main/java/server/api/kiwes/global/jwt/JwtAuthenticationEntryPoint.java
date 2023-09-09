package server.api.kiwes.global.jwt;

//import com.nimbusds.jose.shaded.json.JSONObject; //todo 망할 충돌점
import com.nimbusds.oauth2.sdk.util.OrderedJSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import server.api.kiwes.global.jwt.exception.JwtExceptionList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static server.api.kiwes.global.jwt.exception.JwtExceptionList.*;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 액세스 할 때 호출
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = String.valueOf(request.getAttribute("exception"));

        if(exception.equals(ADDITIONAL_REQUIRED_TOKEN.getCode()))
            setResponse(response, ADDITIONAL_REQUIRED_TOKEN);
        //잘못된 타입의 토큰인 경우
        else if(exception.equals(UNKNOWN_ERROR.getCode()))
            setResponse(response, UNKNOWN_ERROR);

        else if(exception.equals(MAL_FORMED_TOKEN.getCode()))
            setResponse(response, MAL_FORMED_TOKEN);

        else if(exception.equals(ILLEGAL_TOKEN.getCode()))
            setResponse(response, ILLEGAL_TOKEN);

        //토큰 만료된 경우
        else if(exception.equals(EXPIRED_TOKEN.getCode()))
            setResponse(response, EXPIRED_TOKEN);
        //지원되지 않는 토큰인 경우
        else if(exception.equals(UNSUPPORTED_TOKEN.getCode()))
            setResponse(response, UNSUPPORTED_TOKEN);

        else setResponse(response, ACCESS_DENIED);

    }

    private void setResponse(HttpServletResponse response, JwtExceptionList exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        OrderedJSONObject responseJson = new OrderedJSONObject();
        responseJson.put("timestamp", LocalDateTime.now().withNano(0).toString());
        responseJson.put("message", exceptionCode.getMessage());
        responseJson.put("errorCode", exceptionCode.getCode());

        response.getWriter().print(responseJson);
    }
}