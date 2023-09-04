package server.api.kiwes.domain.alarm.dto;

import lombok.*;
import server.api.kiwes.domain.alarm.constant.AlarmType;
import server.api.kiwes.domain.alarm.entity.Alarm;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.member.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmResponseDto {
    @Setter
    private AlarmType type;
    private String content;
    private Club club;
    private Member member;

    public static AlarmResponseDto of(Alarm alarm){
        return AlarmResponseDto.builder()
                .content(alarm.getContent())
                .type(alarm.getType())
                .club(alarm.getClub())
                .member(alarm.getMember())
                .build();
    }
}
