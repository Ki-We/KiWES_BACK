package server.api.kiwes.domain.club.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.api.kiwes.domain.club.constant.ClubResponseType;
import server.api.kiwes.domain.club.dto.ClubArticleMemberInfoDto;
import server.api.kiwes.domain.club.dto.ClubArticleResponseDto;
import server.api.kiwes.domain.club.dto.ClubMemberInfoDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.service.ClubDetailService;
import server.api.kiwes.domain.club.service.ClubService;
import server.api.kiwes.domain.club_member.service.ClubMemberService;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.service.MemberService;
import server.api.kiwes.response.ApiResponse;

@Api(tags = "Club - 디테일 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/club/info")
public class ClubDetailController {
    private final ClubService clubService;
    private final MemberService memberService;
    private final ClubDetailService clubDetailService;
    
    @ApiOperation(value = "모임 상세 정보 불러오기", notes = "모임 정보 페이지에서 활용\n, qna, review는 최대 2개까지"+
            "\n예시 출력 데이터\n" +
            "\"status\": 20108,\n" +
            "  \"message\": \"모임 정보 불러오기 성공\",\n" +
            "  \"data\": {\n" +
            "    \"baseInfo\": {\n" +
            "      \"clubId\": 1,\n" +
            "      \"title\": \"첫번째 모임 제목입니다.\",\n" +
            "      \"thumbnailImageUrl\": \"https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlXdug%2Fbtsl9NySlW1%2FrxaVigq7yGuv7JDhlk5kH0%2Fimg.png\",\n" +
            "      \"heartCount\": 0,\n" +
            "      \"tags\": [\n" +
            "        \"CRAFT\",\n" +
            "        \"CAFE\",\n" +
            "        \"KO\",\n" +
            "        \"EN\"\n" +
            "      ],\n" +
            "      \"date\": \"Oct 30\",\n" +
            "      \"dueTo\": \"Sep 30\",\n" +
            "      \"cost\": 1000,\n" +
            "      \"gender\": \"MALE\",\n" +
            "      \"locationsKeyword\": \"신촌\",\n" +
            "      \"content\": \"첫번째 모임입니다.\",\n" +
            "      \"location\": \"신촌\"\n" +
            "    },\n" +
            "    \"memberInfo\": {\n" +
            "      \"hostThumbnailImage\": \"http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg\",\n" +
            "      \"hostNickname\": \"규리\",\n" +
            "      \"koreanCount\": 0,\n" +
            "      \"foreignerCount\": 0,\n" +
            "      \"maxPeople\": 10\n" +
            "    },\n" +
            "    \"qnas\": [],\n" +
            "    \"reviews\": [],\n" +
            "    \"isHost\": false,\n" +
            "    \"isHeart\": false,\n" +
            "    \"isActivated\": \"YES\"\n" +
            "  }"+
            "]")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20108, message = "모임 정보 불러오기 성공")
    })
    @GetMapping("/detail/{clubId}")
    public ApiResponse<ClubArticleResponseDto> getClubDetail(@PathVariable Long clubId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        ClubArticleResponseDto response = clubDetailService.getClubDetail(member, club);

        return ApiResponse.of(ClubResponseType.GET_INFO_SUCCESS, response);
    }

    @ApiOperation(value = "모임 정보 불러오기", notes = "특정 모임에 참여하고 있는 사람들의 리스트 + 조회한 사람이 호스트 인지도 + 인원 수 까지")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20108, message = "모임 정보 불러오기 성공")
    })
    @GetMapping("/simple/{clubId}")
    public ApiResponse<ClubMemberInfoDto> getClubSimple(@PathVariable Long clubId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        ClubMemberInfoDto response = clubDetailService.getClubSimple(member, club);

        return ApiResponse.of(ClubResponseType.GET_INFO_SUCCESS, response);
    }
}
