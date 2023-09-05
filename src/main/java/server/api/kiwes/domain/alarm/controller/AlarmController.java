package server.api.kiwes.domain.alarm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import server.api.kiwes.domain.alarm.constant.AlarmResponseType;
import server.api.kiwes.domain.alarm.service.AlarmService;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.service.ClubService;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.service.MemberService;
import server.api.kiwes.response.ApiResponse;

@Api(tags = "Alarm - 알림")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmService alarmService;
    private final MemberService memberService;

    @ApiOperation(value = "알림 가져오기", notes = "")
    @GetMapping("/")
    public ApiResponse<Object> alarmList(){
        Member member = memberService.getLoggedInMember();
        return ApiResponse.of(AlarmResponseType.ALARMS, alarmService.getAlarmAll(member));
    }

}
