package server.api.kiwes.domain.club.dto;

import lombok.*;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.review.entity.Review;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubArticleReviewDto {
    Long reviewId;
    String reviewerImageUrl;
    String reviewerNickname;
    String reviewContent;
    String reviewDate;

    String respondentImageUrl;
    String respondentNickname;
    String replyContent;
    String replyDate;

    Boolean isModified;

    public static ClubArticleReviewDto of(Review review){
        Member reviewer = review.getReviewer();
        Member respondent = review.getRespondent();

        return ClubArticleReviewDto.builder()
                .reviewId(review.getId())
                .reviewerImageUrl(reviewer.getProfileImg())
                .reviewerNickname(reviewer.getNickname())
                .reviewContent(review.getReviewContent())
                .reviewDate(review.getModifiedDate().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm")))

                .respondentImageUrl(respondent.getProfileImg())
                .respondentNickname(respondent.getNickname())
                .replyContent(review.getReplyContent())
                .replyDate(review.getReplyDate())

                .isModified(review.getIsModified())
                .build();
    }

}
