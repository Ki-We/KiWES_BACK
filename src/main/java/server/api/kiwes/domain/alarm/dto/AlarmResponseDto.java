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
    private Long clubId;
    private String createAfterHour;
    private String createAfterDay;
    private Long memberId;
    private String imageUrl;
    private Long noticeId;

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
                .clubId(alarm.getClub().getId())
                .createAfterHour(hours)
                .createAfterDay(createAfterDay)
                .memberId(alarm.getMember().getId())
                .noticeId(alarm.getNoticeId())
                .imageUrl(alarm.getImageUrl())
                .build();
    }


}
