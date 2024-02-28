package server.api.kiwes.domain.alarm.entity;

import lombok.*;
import server.api.kiwes.domain.BaseTimeEntity;
import server.api.kiwes.domain.alarm.constant.AlarmContent;
import server.api.kiwes.domain.alarm.constant.AlarmType;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Alarm extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALARM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "SENDER_ID")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AlarmContent content= AlarmContent.QUESTION;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AlarmType type = AlarmType.CLUB;

    private String name;
    private String imageUrl;
    private long noticeId;
    public void setType(AlarmType type){
        this.type = type;
    }
}
