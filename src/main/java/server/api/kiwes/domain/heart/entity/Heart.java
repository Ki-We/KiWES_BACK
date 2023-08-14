package server.api.kiwes.domain.heart.entity;

import lombok.*;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Heart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HEART_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private HeartStatus status = HeartStatus.YES;

    public void setStatus(HeartStatus status){
        this.status = status;
    }
}
