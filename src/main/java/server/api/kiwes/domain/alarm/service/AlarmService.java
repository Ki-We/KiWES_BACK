package server.api.kiwes.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.alarm.constant.AlarmType;
import server.api.kiwes.domain.alarm.dto.AlarmResponseDto;
import server.api.kiwes.domain.alarm.entity.Alarm;
import server.api.kiwes.domain.alarm.repository.AlarmRepository;
import server.api.kiwes.domain.club.constant.ClubStatus;
import server.api.kiwes.domain.club.dto.ClubArticleReviewDto;
import server.api.kiwes.domain.club.dto.ClubPopularEachResponseDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.heart.entity.Heart;
import server.api.kiwes.domain.heart.repository.HeartRepository;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.review.entity.Review;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;


    public List<AlarmResponseDto> getAlarmAll(Member member) {
        List<AlarmResponseDto> response = new ArrayList<>();
        List<Alarm> alarms = alarmRepository.findByMemberIdAndType((member.getId()));

        for(Alarm alarm : alarms){
            response.add(AlarmResponseDto.of(alarm));
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
            if ( alarm.getCreatedDate().plusDays(3).isBefore(LocalDateTime.now())){
                alarmRepository.delete(alarm);
            }
        }
    }
}
