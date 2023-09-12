package server.api.kiwes.domain.review.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.api.kiwes.domain.alarm.constant.AlarmContent;
import server.api.kiwes.domain.alarm.constant.AlarmType;
import server.api.kiwes.domain.alarm.service.AlarmService;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.service.ClubService;
import server.api.kiwes.domain.club_member.entity.ClubMember;
import server.api.kiwes.domain.club_member.service.ClubMemberService;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.service.MemberService;
import server.api.kiwes.domain.review.constant.ReviewResponseType;
import server.api.kiwes.domain.review.dto.ReviewEntireResponseDto;
import server.api.kiwes.domain.review.dto.ReviewRegisterDto;
import server.api.kiwes.domain.review.entity.Review;
import server.api.kiwes.domain.review.service.ReviewService;
import server.api.kiwes.response.ApiResponse;
import server.api.kiwes.response.BizException;

import java.util.Objects;

@Api(tags = "Club - Review")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final MemberService memberService;
    private final ReviewService reviewService;
    private final ClubMemberService clubMemberService;
    private final ClubService clubService;
    private final AlarmService alarmService;

    @ApiOperation(value = "후기 등록", notes = "AlarmContent.REVIEW" +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 21201,\n" +
            "  \"message\": \"후기 등록 완료\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21201, message = "후기 등록 완료"),
            @io.swagger.annotations.ApiResponse(code = 41201, message = "해당 모임에 승인된 멤버가 아님 (400)"),
            @io.swagger.annotations.ApiResponse(code = 41202, message = "사용자가 이미 후기 작성을 하였음 (400)"),
    })
    @PostMapping("/{clubId}")
    public ApiResponse<Object> postReview(@RequestBody ReviewRegisterDto registerDto, @PathVariable Long clubId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        Boolean isApproved = clubMemberService.getIsApproved(club, member);
        if(!isApproved){
            throw new BizException(ReviewResponseType.NOT_CLUB_MEMBER);
        }

        reviewService.postReview(club, member, registerDto);

        ClubMember host = clubMemberService.findByClubHost(club);
        String name = member.getNickname() == null ? "익명" : member.getNickname();
        alarmService.postAlarm(host.getMember(), club, AlarmType.CLUB, name + AlarmContent.REVIEW.getContent());

        return ApiResponse.of(ReviewResponseType.POST_SUCCESS);
    }

    @ApiOperation(value = "후기 수정", notes = "" +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 21202,\n" +
            "  \"message\": \"후기 수정 완료\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21202, message = "후기 수정 완료"),
            @io.swagger.annotations.ApiResponse(code = 41201, message = "해당 모임에 승인된 멤버가 아님 (400)"),
            @io.swagger.annotations.ApiResponse(code = 41203, message = "ID와 일치하는 후기 없음 (404)"),
            @io.swagger.annotations.ApiResponse(code = 41204, message = "후기의 작성자가 아님 (401)"),
            @io.swagger.annotations.ApiResponse(code = 41205, message = "해당 리뷰는 이 모임의 것이 아님 (400)"),
    })
    @PutMapping("/{clubId}/{reviewId}")
    public ApiResponse<Object> modifyReview(@RequestBody ReviewRegisterDto registerDto, @PathVariable Long clubId, @PathVariable Long reviewId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        Review review = reviewService.findById(reviewId);
        if(!club.getReviews().contains(review)){
            throw new BizException(ReviewResponseType.CHECK_PATH);
        }

        if(!Objects.equals(review.getReviewer().getId(), member.getId())){
            throw new BizException(ReviewResponseType.NOT_AUTHOR);
        }

        reviewService.modifyReview(review, registerDto);

        return ApiResponse.of(ReviewResponseType.MODIFY_SUCCESS);
    }

    @ApiOperation(value = "후기 삭제", notes = "" +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 21203,\n" +
            "  \"message\": \"후기 삭제 완료\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21203, message = "후기 삭제 완료"),
    })
    @DeleteMapping("/{clubId}/{reviewId}")
    public ApiResponse<Object> deleteReview( @PathVariable Long clubId, @PathVariable Long reviewId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        Review review = reviewService.findById(reviewId);
        if(!club.getReviews().contains(review)){
            throw new BizException(ReviewResponseType.CHECK_PATH);
        }

        if(!Objects.equals(review.getReviewer().getId(), member.getId())){
            throw new BizException(ReviewResponseType.NOT_AUTHOR);
        }

        reviewService.deleteReview(review);
        return ApiResponse.of(ReviewResponseType.DELETE_SUCCESS);
    }
    
    @ApiOperation(value = "후기 모두 보기", notes = "" +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 21105,\n" +
            "  \"message\": \"qna 리스트 응답 성공\",\n" +
            "  \"data\": isHost : Boolean ,\n" +
            " ReviewDetailDto : { \n" +
            "reviewerProfileImg : String\n" +
            "reviewerNickname : String\n" +
            "reviewContent : String\n" +
            "reviewDate : String\n" +
            "respondentProfileImg : String\n" +
            "respondentNickname : String\n" +
            "replyContent : String\n" +
            "replyDate : String\n" +
            "isAuthorOfReview : Boolean\n" +
            "isAuthorOfReply : Boolean\n" +
            "isModified : Boolean\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21204, message = "후기 모두 보기 성공"),
    })
    @GetMapping("/entire/{clubId}")
    public ApiResponse<ReviewEntireResponseDto> getEntireReview(@PathVariable Long clubId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);

        return ApiResponse.of(ReviewResponseType.ENTIRE_LIST, reviewService.getEntire(club, member));
    }

    @ApiOperation(value = "후기에 답글 달기", notes = "호스트만 달 수 있다." +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 21205,\n" +
            "  \"message\": \"후기 답글 등록 성공\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21205, message = "후기 답글 등록 성공"),
            @io.swagger.annotations.ApiResponse(code = 41206, message = "호스트가 아니므로 답글을 달 수 없음 (401)"),
    })
    @PostMapping("/reply/{clubId}/{reviewId}")
    public ApiResponse<Object> postReply(@PathVariable Long clubId, @PathVariable Long reviewId, @RequestBody ReviewRegisterDto registerDto){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        if(!clubMemberService.findByClubAndMember(club, member).getIsHost()){
            throw new BizException(ReviewResponseType.NOT_HOST);
        }

        Review review = reviewService.findById(reviewId);
        reviewService.postReply(member, review, registerDto);

        return ApiResponse.of(ReviewResponseType.REPLY_SUCCESS);
    }
}
