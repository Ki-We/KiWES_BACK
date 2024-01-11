package server.api.kiwes.domain.alarm.dto;

import lombok.*;
import server.api.kiwes.domain.alarm.constant.AlarmType;
import server.api.kiwes.domain.alarm.entity.Alarm;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    private String createAfterHour;
    private String createAfterDay;
    private Long member_id;
    private String imageUrl;
    public static AlarmResponseDto of(Alarm alarm){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdDate = LocalDateTime.parse(alarm.getCreatedDate().format(formatter), formatter);

        String hours = String.valueOf(ChronoUnit.HOURS.between(createdDate, now));
        long days = ChronoUnit.DAYS.between(createdDate, now);
        String createAfterDay;
        if (days == 0) {
            createAfterDay = "오늘";
            hours=hours+"시간";
        } else if (days == 1) {
            createAfterDay = "어제";
            hours=days+"일";

        } else if (days <= 7) {
            createAfterDay = "이번 주";
            hours=days+"일";
        } else {
            createAfterDay = "이전 활동";
            hours=days+"일";

        }
        return AlarmResponseDto.builder()
                .content(alarm.getContent())
                .type(alarm.getType())
                .club_id(alarm.getClub().getId())
                .createAfterHour(hours)
                .createAfterDay(createAfterDay)
                .member_id(alarm.getMember().getId())
                .imageUrl(alarm.getImageUrl())
                .build();
    }


}
