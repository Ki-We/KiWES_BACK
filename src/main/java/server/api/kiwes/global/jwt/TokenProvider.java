package server.api.kiwes.global.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import server.api.kiwes.domain.member.dto.RefreshTokenRequest;
import server.api.kiwes.domain.member.dto.RefreshTokenResponse;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.repository.MemberRepository;

import io.jsonwebtoken.io.Decoders;
import server.api.kiwes.domain.member.dto.TokenInfoResponse;
import server.api.kiwes.domain.member.repository.RefreshTokenRepository;
import server.api.kiwes.domain.member.service.auth.MemberAuthenticationService;
import server.api.kiwes.global.jwt.entity.RefreshToken;
import server.api.kiwes.global.security.service.CustomUserDetails;
import server.api.kiwes.response.BizException;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static server.api.kiwes.domain.member.constant.MemberResponseType.NOT_FOUND_EMAIL;
import static server.api.kiwes.global.jwt.exception.JwtExceptionList.*;
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider implements InitializingBean {


    private static final String AUTHORITIES_KEY = "auth";
    private static final String ADDITIONAL_INFO="isAdditionalInfoProvided";

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityTime;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityTime;


    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * 토큰 만드는 함수
     * @param authentication
     * @return TokenInfoResponse
     */
    public TokenInfoResponse createToken(Authentication authentication, boolean isAdditionalInfoProvided, Long userId) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date accessTokenValidity = new Date(now + 1000*this.accessTokenValidityTime);
        Date refreshTokenValidity = new Date(now + 1000*this.refreshTokenValidityTime);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(ADDITIONAL_INFO, isAdditionalInfoProvided) // 추가 정보 입력 여부를 클레임에 추가
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(accessTokenValidity)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenValidity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        refreshTokenRepository.save(refreshToken, userId);

        return TokenInfoResponse.from("Bearer", accessToken, refreshToken, refreshTokenValidityTime);

    }





    public boolean getAdditionalInfoProvided(String token){
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.get(ADDITIONAL_INFO, Boolean.class);
    }

    /**
     * 인증하는 함수
     * @param token
     * @return authentication
     * nameAttributeKey 와 authorizedClientRegistrationId 나중에 확인 다시 하기
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        Member member = this.memberRepository.findNotDeletedByEmail(claims.getSubject())
                .orElseThrow(() -> new BizException(NOT_FOUND_EMAIL));

        return new UsernamePasswordAuthenticationToken(new CustomUserDetails(member), token, authorities);
    }

    /**
     * AccessToken 검증하는 함수
     * @param token
     * @return true/ false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new BizException(MAL_FORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new BizException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new BizException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new BizException(ILLEGAL_TOKEN);
        } catch(Exception e){
            log.info(e.getMessage());
            throw e;
        }
    }

    /**
     * RefreshToken 검증
     * @param token
     * @return true/false
     */
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new BizException(MAL_FORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new BizException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new BizException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new BizException(ILLEGAL_TOKEN);
        } catch(Exception e){
            log.info(e.getMessage());
            throw new BizException(UNKNOWN_ERROR);
        } finally {
            return false;
        }
    }

    /**
     * 주어진 JWT 토큰에서 클레임을 추출
     * @param accessToken
     * @return Claims
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * JWT 토큰에서 만료 시간을 확인하고, 현재 시간과 비교하여 남은 시간을 반환
     * @param accessToken
     * @return 남은 시간
     */
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

}
