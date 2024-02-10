package server.api.kiwes.domain.qna.controller;

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
import server.api.kiwes.domain.qna.constant.QnaResponseType;
import server.api.kiwes.domain.qna.dto.QnaRequestDto;
import server.api.kiwes.domain.qna.dto.QnaResponseDto;
import server.api.kiwes.domain.qna.entity.Qna;
import server.api.kiwes.domain.qna.service.QnaService;
import server.api.kiwes.domain.review.constant.ReviewResponseType;
import server.api.kiwes.domain.review.dto.ReviewRegisterDto;
import server.api.kiwes.domain.review.entity.Review;
import server.api.kiwes.response.ApiResponse;
import server.api.kiwes.response.BizException;

import java.util.Objects;

@Api(tags = "Club - Q&A")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qna")
public class QnaController {
    private final QnaService qnaService;
    private final MemberService memberService;
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private final AlarmService alarmService;
    
    @ApiOperation(value = "qna 질문 등록", notes = "AlarmContent.QUESTION" +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 21101,\n" +
            "  \"message\": \"qna 질문 등록 성공\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21101, message = "qna 질문 등록 성공"),
            @io.swagger.annotations.ApiResponse(code = 40101, message = "clubId와 일치하는 모임이 존재하지 않습니다. (404)"),
    })
    @PostMapping("/question/{clubId}")
    public ApiResponse<Object> postQuestion(@PathVariable Long clubId, @RequestBody QnaRequestDto requestDto){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        qnaService.postQuestion(club, member, requestDto);

        ClubMember host = clubMemberService.findByClubHost(club);
        if(host.getId()==member.getId()){
            throw new BizException(QnaResponseType.Q_NOT_HOST_POST);
        }
        String name = member.getNickname() == null ? "익명" : member.getNickname();
        alarmService.postAlarm(host.getMember(),member, club, AlarmType.CLUB, name + AlarmContent.QUESTION.getContent());

        return ApiResponse.of(QnaResponseType.Q_POST_SUCCESS);
    }

    @ApiOperation(value = "qna 답변 등록", notes = "AlarmContent.ANSWER" +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 21102,\n" +
            "  \"message\": \"qna 답변 등록 성공\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21102, message = "qna 답변 등록 성공"),
            @io.swagger.annotations.ApiResponse(code = 40101, message = "clubId와 일치하는 모임이 존재하지 않습니다. (404)"),
            @io.swagger.annotations.ApiResponse(code = 41101, message = "qnaID와 일치하는 QnA가 존재하지 않습니다. (404)"),
            @io.swagger.annotations.ApiResponse(code = 41102, message = "기답변된 QnA입니다. (400)"),
            @io.swagger.annotations.ApiResponse(code = 41103, message = "로그인한 사용자가 호스트가 아닙니다. (400)"),
            @io.swagger.annotations.ApiResponse(code = 41105, message = "삭제된 질문입니다. (400)"),
    })
    @PostMapping("/answer/{clubId}/{qnaId}")
    public ApiResponse<Object> postAnswer(@PathVariable Long clubId, @PathVariable Long qnaId, @RequestBody QnaRequestDto requestDto){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);

        Boolean isHost = clubMemberService.getIsHost(club, member);
        if(!isHost){
            throw new BizException(QnaResponseType.NOT_HOST);
        }

        Qna qna = qnaService.findById(qnaId);
        qnaService.postAnswer(member, qna, requestDto);

        alarmService.postAlarm(qna.getQuestioner(),member, club, AlarmType.CLUB, AlarmContent.ANSWER.getContent());

        return ApiResponse.of(QnaResponseType.A_POST_SUCCESS);
    }
    
    @ApiOperation(value = "qna 질문 삭제", notes = "삭제된 질문입니다 텍스트로 변환" +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 21103,\n" +
            "  \"message\": \"qna 질문 삭제 성공\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21103, message = "qna 질문 삭제 성공"),
            @io.swagger.annotations.ApiResponse(code = 41104, message = "작성자가 아닙니다. (401)"),
    })
    @DeleteMapping("/question/{clubId}/{qnaId}")
    public ApiResponse<Object> deleteQusetion(@PathVariable Long clubId, @PathVariable Long qnaId){
        Member member = memberService.getLoggedInMember();
        Qna qna = qnaService.findById(qnaId);

        qnaService.deleteQuestion(member, qna);
        return ApiResponse.of(QnaResponseType.Q_DELETE_SUCCESS);

    }

    @ApiOperation(value = "qna 답변 삭제", notes = "답변 삭제" +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 21104,\n" +
            "  \"message\": \"qna 답변 삭제 성공\",\n" +
            "  \"data\": null\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21104, message = "qna 답변 삭제 성공"),
            @io.swagger.annotations.ApiResponse(code = 41103, message = "로그인한 사용자가 호스트가 아닙니다. (400)"),
    })
    @DeleteMapping("/answer/{clubId}/{qnaId}")
    public ApiResponse<Object> deleteAnswer(@PathVariable Long clubId, @PathVariable Long qnaId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);

        Boolean isHost = clubMemberService.getIsHost(club, member);
        if(!isHost){
            throw new BizException(QnaResponseType.NOT_HOST);
        }

        Qna qna = qnaService.findById(qnaId);
        qnaService.deleteAnswer(qna);

        return ApiResponse.of(QnaResponseType.A_DELETE_SUCCESS);

    }
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21202, message = "후기 수정 완료"),
            @io.swagger.annotations.ApiResponse(code = 41201, message = "해당 모임에 승인된 멤버가 아님 (400)"),
            @io.swagger.annotations.ApiResponse(code = 41203, message = "ID와 일치하는 후기 없음 (404)"),
            @io.swagger.annotations.ApiResponse(code = 41204, message = "후기의 작성자가 아님 (401)"),
            @io.swagger.annotations.ApiResponse(code = 41205, message = "해당 리뷰는 이 모임의 것이 아님 (400)"),
    })
    @PutMapping("/{clubId}/{qnaId}")
    public ApiResponse<Object> modifyReview(@RequestBody ReviewRegisterDto registerDto, @PathVariable Long clubId, @PathVariable Long qnaId){
        Member member = memberService.getLoggedInMember();
        Club club = clubService.findById(clubId);
        Qna qna = qnaService.findById(qnaId);
        if(!club.getQnas().contains(qna)){
            throw new BizException(ReviewResponseType.CHECK_PATH);
        }

        if(!Objects.equals(qna.getQuestioner().getId(), member.getId())){
            throw new BizException(ReviewResponseType.NOT_AUTHOR);
        }

        qnaService.modifyQna(qna, registerDto);

        return ApiResponse.of(ReviewResponseType.MODIFY_SUCCESS);
    }
    @ApiOperation(value = "모임의 qna 전체 보기", notes = "" +
            "\n예시 출력 데이터" +
            "{\n" +
            "  \"status\": 21105,\n" +
            "  \"message\": \"qna 리스트 응답 성공\",\n" +
            "  \"data\": isHost : Boolean ,\n" +
            " QnaDetailDto : { \n" +
            "questionerProfileImg : String\n" +
            "questionerNickname : String\n" +
            "questionContent : String\n" +
            "qDate : String\n" +
            "respondentProfileImg : String\n" +
            "respondentNickname : String\n" +
            "answerContent : String\n" +
            "aDate : String\n" +
            "isAuthorOfQuestion : false\n" +
            "isAuthorOfAnswer : false\n" +
            "}")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21105, message = "qna 리스트 응답 성공\n 답변이 달리지 않은 질문은 답변 관련 정보가 전부 null로 응답됩니다."),
    })
    @GetMapping("/entire/{clubId}")
    public ApiResponse<QnaResponseDto> getEntireQna(@PathVariable Long clubId, @RequestParam int cursor){
        return ApiResponse.of(QnaResponseType.GET_ENTIRE_LIST, qnaService.getEntireQna(clubService.findById(clubId), memberService.getLoggedInMember(),cursor));
    }

}
