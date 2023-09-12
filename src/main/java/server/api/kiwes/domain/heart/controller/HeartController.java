package server.api.kiwes.domain.heart.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.service.ClubService;
import server.api.kiwes.domain.heart.constant.HeartResponseType;
import server.api.kiwes.domain.heart.service.HeartService;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.service.MemberService;
import server.api.kiwes.response.ApiResponse;

@Api(tags = "Heart - 모임 찜하기")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/heart")
public class HeartController {
    private final MemberService memberService;
    private final ClubService clubService;
    private final HeartService heartService;

    @ApiOperation(value = "모임 찜하기", notes = "" +
            "예시 출력 데이터" +
            "{\n" +
            "  \"status\": 20201,\n" +
            "  \"message\": \"찜하기 성공\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20201, message = "찜하기 성공"),
    })
    @PutMapping("/{clubId}")
    public ApiResponse<Object> heartClub(@PathVariable Long clubId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        heartService.heart(member, club);

        return ApiResponse.of(HeartResponseType.HEART_SUCCEED);
    }

    @ApiOperation(value = "모임 찜하기 취소", notes = "예시 출력 데이터" +
            "{\n" +
            "  \"status\": 20202,\n" +
            "  \"message\": \"찜하기 취소 성공\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20202, message = "찜하기 취소 성공"),
    })
    @DeleteMapping("/{clubId}")
    public ApiResponse<Object> unheartClub(@PathVariable Long clubId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        heartService.unheart(member, club);

        return ApiResponse.of(HeartResponseType.UNHEART_SUCCEED);
    }
}
