package server.api.kiwes.domain.alarm.dto;

import lombok.*;
import server.api.kiwes.domain.alarm.constant.AlarmType;
import server.api.kiwes.domain.alarm.entity.Alarm;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private String createTime;

    public static AlarmResponseDto of(Alarm alarm){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return AlarmResponseDto.builder()
                .content(alarm.getContent())
                .type(alarm.getType())
                .club_id(alarm.getClub().getId())
                .createTime(alarm.getCreatedDate().format(formatter))
                .build();
    }

}
