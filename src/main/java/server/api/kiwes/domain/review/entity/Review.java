package server.api.kiwes.domain.review.entity;

import lombok.*;
import server.api.kiwes.domain.BaseTimeEntity;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.member.entity.Member;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Review extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "REVIEW_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REVIEWER_ID")
    private Member reviewer;

    @ManyToOne
    @JoinColumn(name = "RESPONDENT_ID")
    private Member respondent;

    @ManyToOne
    @JoinColumn(name = "CLUB_ID")
    private Club club;

    @Column(length = 1000)
    private String reviewContent;         // 후기 내용

    @Column(length = 1000)
    private String replyContent;         // 후기 답글 내용

    private String reviewDate;          // 후기 등록 시각
    private String replyDate;           // 답글 등록 시각

    @Builder.Default
    private Boolean isModified = false;

    public void modifyReview(String reviewContent, String now){
        this.reviewContent = reviewContent;
        this.isModified = true;
        this.reviewDate = now;
    }

    public void setReply(Member member, String content, String replyDate){
        this.respondent = member;
        this.replyContent = content;
        this.replyDate = replyDate;
    }

    public void deleteReply(){
        this.reviewer = null;
    }
}
