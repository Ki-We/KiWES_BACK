package server.api.kiwes.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.alarm.dto.AlarmResponseDto;
import server.api.kiwes.domain.alarm.entity.Alarm;
import server.api.kiwes.domain.alarm.repository.AlarmRepository;
import server.api.kiwes.domain.club.dto.ClubPopularEachResponseDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.heart.entity.Heart;
import server.api.kiwes.domain.heart.repository.HeartRepository;
import server.api.kiwes.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;


    public List<AlarmResponseDto> getAlarmAll() {
        List<AlarmResponseDto> response = new ArrayList<>();

        for (Alarm alarm : alarmRepository.findAll()) {
            AlarmResponseDto each = AlarmResponseDto.of(alarm);
            response.add(each);
        }
        return response;
    }
}
