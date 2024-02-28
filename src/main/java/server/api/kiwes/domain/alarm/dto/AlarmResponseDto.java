package server.api.kiwes.domain.alarm.dto;

import lombok.*;
import org.springframework.security.core.parameters.P;
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
    @Setter
    private String content;
    private Long clubId;
    private String createAfterHour;
    private String createAfterDay;
    private Long memberId;
    private Long senderId;
    private String imageUrl;
    private Long noticeId;

    public static AlarmResponseDto of(Alarm alarm,String lang){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdDate = LocalDateTime.parse(alarm.getCreatedDate().format(formatter), formatter);

        String hours = String.valueOf(ChronoUnit.HOURS.between(createdDate, now));
        long days = ChronoUnit.DAYS.between(createdDate, now);
        String createAfterDay;
        if (days == 0) {
            createAfterDay = "오늘";
            hours=hours+" Hours";
        } else if (days == 1) {
            createAfterDay = "어제";
            hours=days+" Days";

        } else if (days <= 7) {
            createAfterDay = "이번 주";
            hours=days+" Days";
        } else {
            createAfterDay = "이전 활동";
            hours=days+" Days";
        }
        AlarmResponseDto alarmResponseDto=AlarmResponseDto.builder()
                .content(alarm.getContent().getContent(lang))
                .type(alarm.getType())
                .clubId(alarm.getClub().getId())
                .createAfterHour(hours)
                .createAfterDay(createAfterDay)
                .memberId(alarm.getMember().getId())
                .senderId(alarm.getSender().getId())
                .noticeId(alarm.getNoticeId())
                .imageUrl(alarm.getImageUrl())
                .build();
        if(!alarm.getName().isEmpty()){
            alarmResponseDto.setContent(alarm.getName()+alarmResponseDto.content);
        }
        return alarmResponseDto;
    }
}
