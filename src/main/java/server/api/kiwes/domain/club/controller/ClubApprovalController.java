package server.api.kiwes.domain.club.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.api.kiwes.domain.club.constant.ClubResponseType;
import server.api.kiwes.domain.club.dto.*;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.service.ClubApprovalService;
import server.api.kiwes.domain.club.service.ClubService;
import server.api.kiwes.domain.club_member.entity.ClubMember;
import server.api.kiwes.domain.club_member.service.ClubMemberService;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.service.MemberService;
import server.api.kiwes.response.ApiResponse;
import server.api.kiwes.response.BizException;

import java.util.List;

@Api(tags = "Club - 승인 대기, 승인 요청 리스트")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/club/approval")
public class ClubApprovalController {
    private final MemberService memberService;
    private final ClubApprovalService clubApprovalService;
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;

    @ApiOperation(value = "승인 요청, 대기 모임 각각 상위 2개씩", notes = "승인의 첫 페이지에 개략적으로 보여 줄 두개의 모임" +
            "\n예시 출력 데이터\n" +
            "\"status\": 20109,\n" +
            "\"message\": \"성공\",\n" +
            "\"data\": [\n" +
            "(List<ClubApprovalRequestSimpleDto>값 예시)\n" +
            "{currentPeople\": \"Integer\",\n" +
            "\"title\": \"String\",\n" +
            " \"clubId\": Long },\n" +
            "(List<ClubApprovalWaitingSimpleDto>값 예시)\n" +
            "{\"clubId\": Long }\n" +
            "\"title\": \"String\",\n" +
            "\"thumbnailImage\": \"String\",\n" +
            "\"date\": \"String\",\n" +
            "\"location\": \"String\",\n" +
            "\"languages\": \"List<String>\",\n" +
            " HeartStatus\": m\"enum{YES, NO}\",\n "+
            "]")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20109, message = "승인관련 리스트 리턴 성공"),
    })
    @GetMapping("/simple/approval")
    public ApiResponse<Object> getSimpleApproval(){
        Member member = memberService.getLoggedInMember();
        return ApiResponse.of(ClubResponseType.APPROVAL_LIST_GET_SUCCEED, clubApprovalService.getSimpleApproval(member));
    }
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20109, message = "승인관련 리스트 리턴 성공"),
    })
    @GetMapping("/simple/wating")
    public ApiResponse<Object> getSimpleWating(){
        Member member = memberService.getLoggedInMember();
        return ApiResponse.of(ClubResponseType.APPROVAL_LIST_GET_SUCCEED, clubApprovalService.getSimpleWating(member));
    }

    @ApiOperation(value = "내가 호스트인 모임 모두 보기", notes = "자기 페이지 정보는 0, 내가 호스트인 모임 전체 리스트" +
            "\n예시 출력 데이터\n" +
            "\"status\": 20109,\n" +
            "\"message\": \"성공\",\n" +
            "\"data\": [\n" +
            "(List<ClubApprovalRequestSimpleDto>값 예시)\n" +
            "{currentPeople\": \"Integer\",\n" +
            "\"title\": \"String\",\n" +
            " \"clubId\": Long }]")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20109, message = "승인 관련 리스트 리턴 성공"),
    })
    @GetMapping("/my-own-club")
    public ApiResponse<List<ClubApprovalRequestSimpleDto>> getRequestsApproval(@RequestParam int cursor){
        Member member= memberService.getLoggedInMember();
        List<ClubApprovalRequestSimpleDto> response = clubApprovalService.getRequestsResponse(member,cursor);

        return ApiResponse.of(ClubResponseType.APPROVAL_LIST_GET_SUCCEED, response);
    }

    @ApiOperation(value = "내 모임 간략히 보기", notes = "자기 페이지 정보는 0, 내 모임 전체 리스트 간략히" +
            "\n예시 출력 데이터\n" +
            "\"status\": 20109,\n" +
            "\"message\": \"성공\",\n" +
            "\"data\": [\n" +
            "(List<ClubApprovalRequestSimpleDto>값 예시)\n" +
            "{currentPeople\": \"Integer\",\n" +
            "\"title\": \"String\",\n" +
            " \"clubId\": Long }]")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20115, message = "내모임 리스트 리턴 성공"),
    })
    @GetMapping("/my-club")
    public ApiResponse<List<ClubApprovalRequestSimpleDto>> getAllMyClub(){
        Member member = memberService.getLoggedInMember();;
        List<ClubApprovalRequestSimpleDto> response = clubApprovalService.getAllMyClub(member);

        return ApiResponse.of(ClubResponseType.Club_LIST_GET_SUCCEED, response);
    }
    @ApiOperation(value = "내가 참여한 모임(이미지)", notes = "내 모임 전체 리스트")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20115, message = "내모임 리스트 리턴 성공"),
    })
    @GetMapping("/my-club-image/{memberId}")
    public ApiResponse<List<ClubMineImageDto>> getAllMyClubImage(@PathVariable Long memberId){
        Member member;
        if(memberId == 0){
            member = memberService.getLoggedInMember();
        }else{
            member = memberService.findById(memberId);
        }
        List<ClubMineImageDto> response = clubApprovalService.getAllMyClubImage(member);

        return ApiResponse.of(ClubResponseType.Club_LIST_GET_SUCCEED, response);
    }
    @ApiOperation(value = "내가 호스트인 모임들 자세히 보기", notes = "내가 호스트인 모임 전체 리스트")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20115, message = "내가 호스트인 모임 리스트 리턴 성공"),
    })
    @GetMapping("/host-club-detail/{memberId}")
    public ApiResponse<List<ClubApprovalWaitingSimpleDto>> getAllMyOwnClubDetail(@PathVariable Long memberId, @RequestParam int cursor){
        Member member;
        if(memberId == 0){
            member = memberService.getLoggedInMember();
        }else{
            member = memberService.findById(memberId);
        }
        List<ClubApprovalWaitingSimpleDto> response = clubApprovalService.getAllMyOwnClubDetail(member,cursor);

        return ApiResponse.of(ClubResponseType.Club_LIST_GET_SUCCEED, response);
    }

    @ApiOperation(value = "대기 중인 모임 모두 보기", notes = "" +
            "예시 출력 데이터\n" +
            "\"status\": 20109,\n" +
            "\"message\": \"성공\",\n" +
            "\"data\": [\n" +
            "(List<ClubApprovalWaitingSimpleDto>값 예시)\n" +
            "{\"clubId\": Long }\n" +
            "\"title\": \"String\",\n" +
            "\"thumbnailImage\": \"String\",\n" +
            "\"date\": \"String\",\n" +
            "\"location\": \"String\",\n" +
            "\"languages\": \"List<String>\",\n" +
            " isHeart\": \"enum{YES, NO}\"] ")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20109, message = "승인 관련 리스트 리턴 성공"),
    })
    @GetMapping("/my-waitings")
    public ApiResponse<List<ClubApprovalWaitingSimpleDto>> getWaitingApproval(@RequestParam int cursor){
        Member member = memberService.getLoggedInMember();
        List<ClubApprovalWaitingSimpleDto> response = clubApprovalService.getWaitingsResponse(member, cursor);

        return ApiResponse.of(ClubResponseType.APPROVAL_LIST_GET_SUCCEED, response);
    }

    @ApiOperation(value = "내가 호스트인 모임에 신청한 사람들 명단 리턴", notes = "" +
            "예시 출력 데이터\n" +
            "\"status\": 20109,\n" +
            "\"message\": \"성공\",\n" +
            "\"data\": [\n" +
            "(List<ClubWaitingMemberDto>값 예시)\n" +
            " {nickname\": \"String\",\n" +
            "\"profileImgUrl\": \"String\",\n" +
            " \"memberId\": Long }]")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20109, message = "승인 관련 리스트 리턴 성공"),
    })
    @GetMapping("/my-club/waiting/{clubId}")
    public ApiResponse<List<ClubWaitingMemberDto>> getWaitingPeople(@PathVariable Long clubId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        ClubMember clubMember = clubMemberService.findByClubAndMember(club, member);
        if(clubMember == null || !clubMember.getIsHost()){
            throw new BizException(ClubResponseType.NOT_HOST);
        }
        List<ClubWaitingMemberDto> response = clubApprovalService.getClubWaitingPeople(club);
        return ApiResponse.of(ClubResponseType.APPROVAL_LIST_GET_SUCCEED, response);
    }
}