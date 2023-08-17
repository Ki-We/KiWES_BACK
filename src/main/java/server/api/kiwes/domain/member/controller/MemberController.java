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
import server.api.kiwes.domain.member.dto.AdditionInfoRequest;
import server.api.kiwes.domain.member.dto.RefreshTokenRequest;
import server.api.kiwes.domain.member.service.MemberService;
import server.api.kiwes.domain.member.service.auth.MemberAuthenticationService;
import server.api.kiwes.global.aws.PreSignedUrlService;
import server.api.kiwes.global.jwt.TokenProvider;
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
    private final TokenProvider tokenProvider;

//    @ApiOperation(value = "accessToken 값 위한 API", notes = "https://kauth.kakao.com/oauth/authorize?client_id=93df5ea9a1445313343f4bb0f1d362ce&redirect_uri=http://localhost:8080/oauth/kakao&response_type=code 요청 시 리다이렉트 됨")
    @ApiOperation(value = "accessToken 값 위한 API", notes = "테스트")
    @GetMapping("/auth/{socialLoginType}")
    public ApiResponse<Object> CallBack(@PathVariable(name="socialLoginType") SocialLoginType socialLoginType,
                                              @RequestParam String code){
        System.out.println(socialLoginType+"     1212321312312321321321321");
        System.out.println(code);
        return ApiResponse.of(MemberResponseType.CALL_BACK_SUCCESS,
                authenticationService.getAccessToken(socialLoginType, code));
    }
    /**
     * API
     *
     * @return ApiResponse
     * @Author Seungyeon, Jeong
     */
    @ApiOperation(value = "외부 로그인", notes = "외부 로그인 API")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20001, message = "로그인 객체 정상 리턴 (200 OK)"),
            @io.swagger.annotations.ApiResponse(code = 40001, message = "parameter 누락 (400 BAD_REQUEST)")
    })
    @PostMapping("/auth/{socialLoginType}")
    public ApiResponse<Object> login(@PathVariable(name="socialLoginType") SocialLoginType socialLoginType,
                                     @RequestHeader(name = "Authorization") String token) {
        return ApiResponse.of(MemberResponseType.LOGIN_SUCCESS,
                authenticationService.login(socialLoginType,token));
    }
    /**
     * API
     *
     * @param additionInfoRequest
     * @return ResponseMessage
     * @Author Seungyeon, Jeong
     */

    @ApiOperation(value = "추가 정보 입력", notes = "추가 정보를 입력합니다.")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20001, message = "Foo 객체 정상 리턴 (200 OK)"),
            @io.swagger.annotations.ApiResponse(code = 40001, message = "parameter 누락 (400 BAD_REQUEST)")
    })
    @PostMapping("/additional-info")
    public ApiResponse<Object> signUp(
            @Parameter(name = "추가 정보 입력 객체", description = "회원가입시 추가정보 입력 위한 객체", in = QUERY, required = false) @RequestBody(required = false) AdditionInfoRequest additionInfoRequest
    ) {


        if (additionInfoRequest == null || additionInfoRequest.equals("error")) {
            log.error(FooResponseType.INVALID_PARAMETER.getMessage());
            throw new BizException(FooResponseType.INVALID_PARAMETER);
        }

        authenticationService.signUp(additionInfoRequest);

        return ApiResponse.of(MemberResponseType.SIGN_UP_SUCCESS);
    }


    @ApiOperation(value = "토큰 재발급", notes = "토큰을 재발급합니다.")
    @PostMapping("/auth/refresh")
    public ApiResponse<Object> tokenRefresh(

            @RequestBody RefreshTokenRequest refreshTokenRequest

    ) {

        return ApiResponse.of(MemberResponseType.TOKEN_REFRESH_SUCCESS,authenticationService.refreshToken(refreshTokenRequest));

    }


    @ApiOperation(value = "닉네임 중복검사", notes = "중복되는 닉네임이 있는지 검사합니다..")
    @PostMapping("/nickname")
    public ApiResponse<Object> nickname(
            @RequestBody String nickname
    ) {
        return ApiResponse.of(MemberResponseType.NICKNAME_DUPLICATE_SUCCESS, memberService.nicknameDuplicateCheck(nickname));
    }

    @ApiOperation(value = "자기소개 수정", notes = "자기소개를 수정합니다.")
    @PostMapping("/mypage/introduction")
    public ApiResponse<Object> introduction(
            @RequestBody String introduction
    ) {
        return ApiResponse.of(MemberResponseType.INTRODUCTION_UPDATE_SUCCESS, memberService.updateIntroduction(introduction));
    }

    @ApiOperation(value = "프로필 이미지 수정", notes = "프로필 이미지 변경을 위한 presigned-url 을 받아옵니다.")
    @GetMapping("mypage/profileImg")
    public ApiResponse<Object> profileImg(

    ) {

        String nickname = memberService.changeProfileImg() + ".jpg";
        return ApiResponse.of(MemberResponseType.PROFILE_IMG_SUCCESS, preSignedUrlService.getPreSignedUrl("profileimg/", nickname));

    }

    @ApiOperation(value = "마이페이지 정보 ", notes = "마이페이지 내 정보 가져오기.")
    @GetMapping("/mypage")
    public ApiResponse<Object> myPage(
    ) throws ParseException {
        return ApiResponse.of(MemberResponseType.MYPAGE_LOAD_SUCCESS, memberService.myPage());

    }

    @ApiOperation(value = "로그아웃", notes = "로그아웃을 합니다.")
    @PostMapping("/auth/logout")
    public ApiResponse<Object> logout( ){
        return ApiResponse.of(MemberResponseType.LOGOUT_SUCCESS,authenticationService.logout());
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원탈퇴를 합니다.")
    @PostMapping("/auth/quit")
    public ApiResponse<Object> quit( ){
        return ApiResponse.of(MemberResponseType.LOGOUT_SUCCESS,authenticationService.quit());
    }




//    @GetMapping("/login")
//    public String showLoginPage() {
//        // 로그인 페이지를 보여주는 코드 작성
//        return "login"; // 예시로 "login"을 리턴하면 "login.jsp" 등의 뷰를 템플릿 엔진으로 렌더링합니다.
//    }


}
