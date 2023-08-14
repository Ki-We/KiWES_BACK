package server.api.kiwes.domain.club.entity;

import lombok.*;
import server.api.kiwes.domain.BaseTimeEntity;
import server.api.kiwes.domain.club.constant.ClubStatus;
import server.api.kiwes.domain.club_category.entity.ClubCategory;
import server.api.kiwes.domain.club_language.entity.ClubLanguage;
import server.api.kiwes.domain.club_member.entity.ClubMember;
import server.api.kiwes.domain.heart.entity.Heart;
import server.api.kiwes.domain.qna.entity.Qna;
import server.api.kiwes.domain.review.entity.Review;
import server.api.kiwes.global.entity.Gender;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class Club extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "CLUB_ID")
    private Long id;

    private String date;             // 만나는 날짜
    private String dueTo;            // 모집 마감 날짜
    private Integer cost;            // 인당 예상 비용
    private Integer maxPeople;       // 모집 정원

    @Builder.Default
    private Integer currentPeople = 1;   // 현재 인원, 호스트가 무조건 있으므로 기본값 1

    @Enumerated(EnumType.STRING)
    private Gender gender;           // 모집 성별 (남, 여, 누구나)
    private String title;            // 제목

    @Builder.Default
    @Column(length = 1000)
    private String thumbnailUrl = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlXdug%2Fbtsl9NySlW1%2FrxaVigq7yGuv7JDhlk5kH0%2Fimg.png";     // 썸네일 이미지 주소

    @Column(length = 1000)
    private String content;          // 모임 소개
    private String locationsKeyword; // 위치 키워드
    private String location;         // 위도, 경도

    @Setter
    @Builder.Default
    private Integer heartCnt = 0;

    @Builder.Default
    private String uuid = UUID.randomUUID().toString();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ClubStatus isActivated = ClubStatus.YES;     // 활성화, 비활성화 여부

    @OneToMany(mappedBy = "club",  fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private List<ClubLanguage> languages;

    @OneToMany(mappedBy = "club",  fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClubMember> members;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Heart> hearts;

    @OneToMany(mappedBy = "club",  fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "club",  fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Qna> qnas;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ClubCategory> categories;

    public void addCurrentPeople(){
        this.currentPeople++;
    }

    public void subCurrentPeople(){
        this.currentPeople--;
    }
}
