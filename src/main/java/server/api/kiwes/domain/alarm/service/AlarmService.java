package server.api.kiwes.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.alarm.constant.AlarmType;
import server.api.kiwes.domain.alarm.dto.AlarmResponseDto;
import server.api.kiwes.domain.alarm.entity.Alarm;
import server.api.kiwes.domain.alarm.repository.AlarmRepository;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    public List<AlarmResponseDto> getAlarmAll(Member member, int cursor) {
        List<AlarmResponseDto> response = new ArrayList<>();
        List<Alarm> alarms = alarmRepository.findByMemberIdAndType((member.getId()),cursor);

        for(Alarm alarm : alarms){
            response.add(AlarmResponseDto.of(alarm));
            member.setChecked(LocalDateTime.now());
            memberRepository.save(member);
        }
        return response;
    }

    public void postAlarm(Member member, Club club, AlarmType type, String content) {
        Alarm alarm = Alarm.builder().club(club).member(member).type(type).content(content).build();
        alarmRepository.save(alarm);
    }
    public void deleteOldAlarm() {
        List<Alarm> allAlarms = alarmRepository.findAll();
        for ( Alarm alarm : allAlarms ){
            if ( alarm.getCreatedDate().plusDays(11).isBefore(LocalDateTime.now())){
                alarmRepository.delete(alarm);
            }
        }
    }

    public boolean isAnyCheckedAlarm(Member member) {
        List<Alarm> alarms = alarmRepository.findByMemberIdAndType((member.getId()));
        if(alarms.isEmpty()) return false;
        for(Alarm alarm : alarms){
            if(alarm.getCreatedDate().isBefore(member.getChecked())){
                return false;
            }
        }
        return true;
    }
}
