package server.api.kiwes.domain.alarm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import server.api.kiwes.domain.alarm.constant.AlarmResponseType;
import server.api.kiwes.domain.alarm.service.AlarmService;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.service.ClubService;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.service.MemberService;
import server.api.kiwes.domain.search_count.entity.SearchCount;
import server.api.kiwes.response.ApiResponse;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Api(tags = "Alarm - 알림")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmService alarmService;
    private final MemberService memberService;

    @ApiOperation(value = "알림 가져오기", notes =
            "예시 출력 데이터\n" +
            "\"status\": 20401,\n" +
            "\"message\": \"성공\",\n" +
            "\"data\": [\n" +
            " {type\": \"NOTICE\",\n" +
            "\"content\": \"새로운 공지가 등록되었습니다.\",\n" +
            " \"club_id\": 0 }"
    )
    @GetMapping("/")
    public ApiResponse<Object> alarmList(@RequestParam String lang){
        Member member = memberService.getLoggedInMember();
        return ApiResponse.of(AlarmResponseType.ALARMS, alarmService.getAlarmAll(member,lang));
    }


    /**
     * 매일 0시 3일 지난 알람 삭제
     */
    @Scheduled(cron = "0 0 0 * * ?") // 매일 0시
    public void removeOldAlarm(){
        alarmService.deleteOldAlarm();
    }


    @ApiOperation(value = "알림 로깅", notes ="")
    @GetMapping("/log")
    public ApiResponse<Object> getloging(){
        Member member = memberService.getLoggedInMember();
        if(alarmService.isAnyCheckedAlarm(member)){
            return ApiResponse.of(AlarmResponseType.UNCHECKED);
        }else{
            return ApiResponse.of(AlarmResponseType.CHECKED);
        }

    }
}
