package server.api.kiwes.domain.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import server.api.kiwes.domain.member.constant.MemberResponseType;
import server.api.kiwes.domain.member.constant.SocialLoginType;
import server.api.kiwes.domain.member.dto.*;
import server.api.kiwes.domain.member.service.MemberService;
import server.api.kiwes.domain.member.service.auth.MemberAuthenticationService;
import server.api.kiwes.global.aws.PreSignedUrlService;
import server.api.kiwes.response.BizException;
import server.api.kiwes.response.ApiResponse;
import server.api.kiwes.response.foo.FooResponseType;

import java.text.ParseException;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@RestController
@AllArgsConstructor
//@RequestMapping("/api/v1/members")
@Api(tags = "Member")
@Slf4j
public class MemberController {

    private final MemberAuthenticationService authenticationService;
    private final MemberService memberService;
    private final PreSignedUrlService preSignedUrlService;

    @ApiOperation(value = "accessToken 값 위한 API",
            notes = "https://kauth.kakao.com/oauth/authorize?client_id=6f0216bfb31177fe4956e6a1a17bb5c6&redirect_uri=https://api.kiwes.org/oauth/kakao&response_type=code  카카오 요청 ~~~~~~~~~///~~~~" +
                    "https://accounts.google.com/o/oauth2/v2/auth?scope=profile%20email&response_type=code&redirect_uri=http://localhost:8080/login/oauth2/code/google&client_id=156388466486-i9b6usmht9jkmmtc7bpvmrmfks5489bp.apps.googleusercontent.com  구글 로그인 /// " +
                    " https://appleid.apple.com/auth/authorize?client_id=org.kiwes.KiWESApp&redirect_uri=https://api.kiwes.org/login/oauth/apple&response_type=code%20id_token&scope=name%20email&response_mode=form_post "+
                    " 애플 로그인"
    )
    @GetMapping(value = {"/oauth/{socialLoginType}","/login/oauth2/code/{socialLoginType}"})

    public ApiResponse<Object> callBack(
            @PathVariable(name="socialLoginType") SocialLoginType socialLoginType,
                                              @RequestParam String code){
        log.info(code);
        log.info(">> 사용자로부터 accessToken 요청을 받음 :: {} Social Login", socialLoginType);
        return ApiResponse.of(MemberResponseType.CALL_BACK_SUCCESS,
                authenticationService.getAccessToken(socialLoginType, code));
    }


    @ApiOperation(value = "외부 로그인", notes = "외부 로그인 API")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20001, message = "로그인 객체 정상 리턴 (200 OK)"),
            @io.swagger.annotations.ApiResponse(code = 40001, message = "parameter 누락 (400 BAD_REQUEST)")
    })
    @PostMapping(value = {"/oauth/{socialLoginType}","/login/oauth2/code/{socialLoginType}"}) //"/oauth/{socialLoginType}",
    public ApiResponse<Object> login(@PathVariable(name="socialLoginType") SocialLoginType socialLoginType,
                                        @RequestParam String token) {
        log.info(token);
        return ApiResponse.of(MemberResponseType.LOGIN_SUCCESS,
                authenticationService.login(socialLoginType,token));
    }

    @ApiOperation(value = "애플 로그인", notes = "애플 로그인 API" +
            " https://appleid.apple.com/auth/authorize?client_id=org.kiwes.KiWESApp&redirect_uri=https://api.kiwes.org/login/oauth/apple&response_type=code%20id_token&scope=name%20email&response_mode=form_post "
    )
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20001, message = "로그인 객체 정상 리턴 (200 OK)"),
            @io.swagger.annotations.ApiResponse(code = 40001, message = "parameter 누락 (400 BAD_REQUEST)")
    })
    @PostMapping(value = {"/login/oauth/{socialLoginType}"})
    public ApiResponse<Object>  loginApple(@PathVariable(name="socialLoginType") SocialLoginType socialLoginType,
                                                 @RequestParam String code) {
        log.info(">> 사용자로부터 accessToken 요청을 받음 :: {} Social Login "+ socialLoginType);
        return ApiResponse.of(MemberResponseType.LOGIN_SUCCESS,
                authenticationService.login(socialLoginType,code));
    }



    @ApiOperation(value = "추가 정보 입력", notes = "추가 정보를 입력합니다." +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 20002,\n" +
            "  \"message\": \"회원가입 성공\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20001, message = "Foo 객체 정상 리턴 (200 OK)"),
            @io.swagger.annotations.ApiResponse(code = 40001, message = "parameter 누락 (400 BAD_REQUEST)")
    })
    @PostMapping("/additional-info")
    public ApiResponse<Object> signUp(
            @Parameter(name = "추가 정보 입력 객체", description = "회원가입시 추가정보 입력 위한 객체", in = QUERY, required = false) @RequestBody(required = false) AdditionInfoRequest additionInfoRequest) {
        if (additionInfoRequest == null || additionInfoRequest.equals("error")) {
            log.error(FooResponseType.INVALID_PARAMETER.getMessage());
            throw new BizException(FooResponseType.INVALID_PARAMETER);
        }
        authenticationService.signUp(additionInfoRequest);
        return ApiResponse.of(MemberResponseType.SIGN_UP_SUCCESS);
    }


    @ApiOperation(value = "토큰 재발급", notes = "토큰을 재발급합니다." +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 20003,\n" +
            "  \"message\": \"토큰 재발급을 완료하였습니다\",\n" +
            "  \"data\": { accessToken : String\n" +
            "refreshToken: String" +
            "}")
    @PostMapping("/auth/refresh")
    public ApiResponse<Object> tokenRefresh(
            @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ApiResponse.of(MemberResponseType.TOKEN_REFRESH_SUCCESS,authenticationService.refreshToken(refreshTokenRequest));
    }


    @ApiOperation(value = "닉네임 중복검사", notes = "중복되는 닉네임이 있는지 검사합니다." +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 20005,\n" +
            "  \"message\": \"닉네임 중복체크 완료\",\n" +
            "  \"data\": null\n" +
            "}")
    @PostMapping("/nickname")
    public ApiResponse<Object> nickname(
            @RequestBody String nickname
    ) {
        return ApiResponse.of(MemberResponseType.NICKNAME_DUPLICATE_SUCCESS, memberService.nicknameDuplicateCheck(nickname));
    }

    @ApiOperation(value = "자기소개 수정", notes = "자기소개를 수정합니다." +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 20006,\n" +
            "  \"message\": \"자기소개 수정 완료\",\n" +
            "  \"data\": {nickname : String}\n" +
            "}")
    @PostMapping("/mypage/introduction")
    public ApiResponse<Object> introduction(
            @RequestBody String introduction
    ) {
        return ApiResponse.of(MemberResponseType.INTRODUCTION_UPDATE_SUCCESS, memberService.updateIntroduction(introduction));
    }

    @ApiOperation(value = "프로필 이미지 수정", notes = "프로필 이미지 변경을 위한 presigned-url 을 받아옵니다." +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 20003,\n" +
            "  \"message\": \"프로필 이미지 Presigned URL 발급 완료\",\n" +
            "  \"data\": {url : String}\n" +
            "}")
    @GetMapping("mypage/profileImg")
    public ApiResponse<Object> profileImg() {
        String nickname = memberService.changeProfileImg() + ".jpg";
        return ApiResponse.of(MemberResponseType.PROFILE_IMG_SUCCESS, preSignedUrlService.getPreSignedUrl("profileimg/", nickname));
    }

    @ApiOperation(value = "마이페이지 정보 ", notes = "마이페이지 내 정보 가져오기." +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 20007,\n" +
            "  \"message\": \"마이페이지 정보 조회 완료\",\n" +
            "  \"data\": " +
            "{profileImage : String\n" +
            "nickname : String\n" +
            "Nationality : String\n" +
            "age : String\n" +
            "gender : String\n" +
            "introduction : String\n" +
            "}")
    @GetMapping("/mypage")
    public ApiResponse<Object> myPage(
    ) throws ParseException {
        return ApiResponse.of(MemberResponseType.MYPAGE_LOAD_SUCCESS, memberService.myPage());

    }

    @ApiOperation(value = "로그아웃", notes = "로그아웃을 합니다." +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 20009,\n" +
            "  \"message\": \"로그아웃 성공\",\n" +
            "  \"data\": \"hi\"\n" +
            "}")
    @PostMapping("/auth/logout")
    public ApiResponse<Object> logout( ){
        return ApiResponse.of(MemberResponseType.LOGOUT_SUCCESS,authenticationService.logout());
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원탈퇴를 합니다." +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 20010,\n" +
            "  \"message\": \"회원 탈퇴 성공\",\n" +
            "  \"data\": \"bye\"\n" +
            "}")
    @PostMapping("/auth/quit")
    public ApiResponse<Object> quit( ){
        return ApiResponse.of(MemberResponseType.QUIT_SUCCESS,authenticationService.quit());
    }

}
