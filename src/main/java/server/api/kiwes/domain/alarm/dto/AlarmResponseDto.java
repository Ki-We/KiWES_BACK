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
    private Long club_id;

    public static AlarmResponseDto of(Alarm alarm){
        return AlarmResponseDto.builder()
                .content(alarm.getContent())
                .type(alarm.getType())
                .club_id(alarm.getClub().getId())
                .build();
    }
}
