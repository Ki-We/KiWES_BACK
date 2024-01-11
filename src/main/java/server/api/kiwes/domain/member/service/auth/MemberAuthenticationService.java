package server.api.kiwes.domain.member.service.auth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.alarm.entity.Alarm;
import server.api.kiwes.domain.club_member.entity.ClubMember;
import server.api.kiwes.domain.club_member.repository.ClubMemberRepository;
import server.api.kiwes.domain.club_member.service.ClubMemberService;
import server.api.kiwes.domain.language.entity.Language;
import server.api.kiwes.domain.language.language.LanguageRepository;
import server.api.kiwes.domain.language.type.LanguageType;
import server.api.kiwes.domain.member.constant.SocialLoginType;
import server.api.kiwes.domain.member.dto.*;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.entity.MemberDeleted;
import server.api.kiwes.domain.member.entity.Nationality;
import server.api.kiwes.domain.member.repository.MemberDeletedRepository;
import server.api.kiwes.domain.member.repository.MemberRepository;
import server.api.kiwes.domain.member.repository.RefreshTokenRepository;
import server.api.kiwes.domain.member.service.login.MemberLoginService;
import server.api.kiwes.domain.member.service.validate.MemberValidationService;
import server.api.kiwes.domain.member_language.entity.MemberLanguage;
import server.api.kiwes.domain.member_language.repository.MemberLanguageRepository;
import server.api.kiwes.global.entity.Gender;
import server.api.kiwes.global.jwt.TokenProvider;
import server.api.kiwes.global.security.util.SecurityUtils;
import server.api.kiwes.response.BizException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static server.api.kiwes.domain.member.constant.MemberResponseType.*;
import static server.api.kiwes.domain.member.constant.MemberServiceMessage.*;
import static server.api.kiwes.domain.member.constant.Role.ROLE_USER;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberAuthenticationService {

    private final MemberRepository memberRepository;
    private final LanguageRepository languageRepository;
    private final MemberLanguageRepository memberLanguageRepository;
    private final MemberDeletedRepository memberDeletedRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ClubMemberRepository clubMemberRepository;

    private final List<MemberLoginService> loginServiceList;

    private final MemberValidationService validateService;
    private final TokenProvider tokenProvider;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URL;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String GOOGLE_TOKEN_URL;

    private MemberLoginService loginService;

    /**
     * 카카오 연결해서 엑세스 토큰 발급 받기
     */
    public String getAccessToken(SocialLoginType socialLoginType, String code) {
        loginService = findSocialOauthByType(socialLoginType);
        String access_Token="";
        String refresh_Token ="";
        String reqURL = null;
        String refreshTokenName="refresh_token";
        try{
        switch (socialLoginType){
            case kakao:
                reqURL = KAKAO_TOKEN_URL;
                break;
            case google:
                reqURL = GOOGLE_TOKEN_URL;
                refreshTokenName ="id_token";
                break;
            default:
                break;
        }

            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(loginService.getOauthRedirectURL(code));
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            System.out.println("access_token : " + access_Token);
            refresh_Token = element.getAsJsonObject().get(refreshTokenName).getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return access_Token;
    }

    public LoginResponse login(SocialLoginType socialLoginType, String token) {
        loginService = findSocialOauthByType(socialLoginType);

        // access token 으로 사용자 정보 가져오기
        JsonObject memberInfo = null;
        switch (socialLoginType){
            case kakao:
                memberInfo = loginService.connect(KAKAO_LOGIN_URL.getValue(), token);
                break;
            case google:
                memberInfo = loginService.connect(GOOGLE_LOGIN_URL.getValue(), token);
                break;
            case apple:
                memberInfo = loginService.connect(APPLE_LOGIN_URL.getValue(), token);
                break;
            default:
                break;
        }

        System.out.println(memberInfo.toString());
        Member member = saveMember(loginService.getEmail(memberInfo), loginService.getProfileUrl(memberInfo),loginService.getGender(memberInfo));
        boolean isSignedUp = member.getEmail() != null;

        //2. 스프링 시큐리티 처리
        List<GrantedAuthority> authorities = initAuthorities();
        OAuth2User userDetails = createOAuth2UserByJson(authorities, memberInfo, loginService.getEmail(memberInfo));
        OAuth2AuthenticationToken auth = configureAuthentication(userDetails, authorities);

        //3. JWT 토큰 생성
        TokenInfoResponse tokenInfoResponse = tokenProvider.createToken(auth, isSignedUp, member.getId());
        return LoginResponse.from(tokenInfoResponse, isSignedUp ? LOGIN_SUCCESS.getMessage() : SIGN_UP_ING.getMessage(), member.getId());

    }

    /**
     *
     * @param additionInfoRequest
     * @return LoginResponse
     * 가입 되어있지 않은 경우, 추가 정보 입력 후 카카오 회원가입
     */
    public LoginResponse signUp(AdditionInfoRequest additionInfoRequest) {

        //추가 정보 입력 시
        //1. 프론트엔드에게 받은 (자체) 액세스 토큰 이용해서 사용자 이메일 가져오기
        Authentication authentication = tokenProvider.getAuthentication(additionInfoRequest.getAccessToken());
        Member member = validateService.validateEmail(authentication.getName());

        //2. 추가 정보 저장
        member.setMember(additionInfoRequest.getNickName(), additionInfoRequest.getBirth(), additionInfoRequest.getIntroduction(), additionInfoRequest.getNationality());

        //one to many 저장
        member.setLanguages(getMemberLanguageEntities(additionInfoRequest.getLanguages(), member));
        memberRepository.save(member);

        //3. 스프링 시큐리티 처리
        List<GrantedAuthority> authorities = initAuthorities();
        OAuth2User userDetails = createOAuth2UserByMember(authorities, member);
        OAuth2AuthenticationToken auth = configureAuthentication(userDetails, authorities);

        //4. JWT 토큰 생성
        TokenInfoResponse tokenInfoResponse = tokenProvider.createToken(auth, true, member.getId());
        return LoginResponse.from(tokenInfoResponse, SIGN_UP_SUCCESS.getMessage(), member.getId());

    }

    /**
     * 리프레쉬 토큰 발급
     */

    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        Long userId=tokenProvider.getUserId(refreshTokenRequest.getRefreshToken());
        System.out.println(userId);
        //1. refreshToken 검증
        refreshTokenRepository.findById(userId).orElseThrow(() -> new BizException(NOT_FOUND_EMAIL));
        //2. 새로운 accessToken 재발급
        //2.1 시큐리티 설정
        Member member = memberRepository.findById(userId).orElseThrow(() -> new BizException(NOT_FOUND_EMAIL));
        List<GrantedAuthority> authorities = initAuthorities();
        OAuth2User userDetails = createOAuth2UserByMember(authorities, member);
        OAuth2AuthenticationToken auth = configureAuthentication(userDetails, authorities);
        //2.2 JWT 토큰 생성
        TokenInfoResponse tokenInfoResponse = tokenProvider.createToken(auth, true, member.getId());
        return RefreshTokenResponse.from(tokenInfoResponse);
    }

    public Member saveMember(String email,String profileImg, String gender) {
        // 가입 여부 확인
        if (!memberRepository.existsByEmail(email)&&!memberDeletedRepository.existsByEmail(email)) {
            Member member = new Member(email,profileImg, Gender.valueOf(gender.toUpperCase()));
            member.setMember(member.getEmail().split("@")[0],"NOT SETTING","String", "FOREIGN");
            memberRepository.save(member);
        }

        return memberRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("해당 유저는 미가입 혹은 탈퇴 유저입니다."));

    }

    public List<GrantedAuthority> initAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(String.valueOf(ROLE_USER)));
        return authorities;
    }

    public OAuth2User createOAuth2UserByMember(List<GrantedAuthority> authorities, Member member) {
        Map<String, Object> memberMap = new HashMap<>();
        memberMap.put("email", member.getEmail());
        memberMap.put("profileImg", member.getProfileImg());
        memberMap.put("nickname", member.getNickname());
        memberMap.put("birth", member.getBirth());
        memberMap.put("introduction", member.getIntroduction());
        memberMap.put("nationality", member.getNationality());
        OAuth2User userDetails = new DefaultOAuth2User(authorities, memberMap, "email");
        return userDetails;
    }

    /**
     * userInfo, email -> OAuth2User
     *
     * @param authorities
     * @param userInfo
     * @param email
     * @return OAuth2User
     */

    private OAuth2User createOAuth2UserByJson(List<GrantedAuthority> authorities, JsonObject userInfo, String email) {
        Map<String, Object> memberMap = new HashMap<>();
        memberMap.put("email", email);
        memberMap.put("profileUrl", loginService.getProfileUrl(userInfo));
        memberMap.put("gender", loginService.getGender(userInfo));
        authorities.add(new SimpleGrantedAuthority(String.valueOf(ROLE_USER)));
        return new DefaultOAuth2User(authorities, memberMap, "email");
    }



    public OAuth2AuthenticationToken configureAuthentication(OAuth2User userDetails, List<GrantedAuthority> authorities) {
        OAuth2AuthenticationToken auth = new OAuth2AuthenticationToken(userDetails, authorities, "email");
        auth.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }

    private List<MemberLanguage> getMemberLanguageEntities(List<String> languageStrings, Member member){
        List<MemberLanguage> memberLanguages = new ArrayList<>();
        System.out.println(languageStrings);

        for(String languageString : languageStrings){
            LanguageType type = LanguageType.valueOf(languageString);
            Language language = languageRepository.findByName(type);
            System.out.println(language.getId());
            MemberLanguage memberLanguage = MemberLanguage.builder()
                    .member(member)
                    .language(language)
                    .build();
            memberLanguageRepository.save(memberLanguage);
            memberLanguages.add(memberLanguage);

        }

        return memberLanguages;
    }

    public String logout() {

        Member user = validateService.validateEmail(SecurityUtils.getLoggedInUser().getEmail());
        refreshTokenRepository.deleteById(user.getId());
        return "hi";
    }

    public String quit() {
        Member user = validateService.validateEmail(SecurityUtils.getLoggedInUser().getEmail());
        List<ClubMember> clubMember = clubMemberRepository.findByMemberHost(user);
        Member hostdummy = memberRepository.findById(0L).get();
        System.out.println(hostdummy.getId());
        for(ClubMember cm : clubMember){
            cm.setMember(hostdummy);
        }
        user.setIsDeleted(); //memberRepository.delete(user);
        memberDeletedRepository.save(MemberDeleted.builder().email(user.getEmail()).build());

        return "bye";
    }
    public void deleteOld() {
        List<MemberDeleted> allMemberDeleted = memberDeletedRepository.findAll();
        for ( MemberDeleted MemberDeleted : allMemberDeleted ){
            if ( MemberDeleted.getCreatedDate().plusDays(30).isBefore(LocalDateTime.now())){
                memberDeletedRepository.delete(MemberDeleted);
            }
        }
    }

    private MemberLoginService findSocialOauthByType(SocialLoginType socialLoginType) {
        return loginServiceList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

}




