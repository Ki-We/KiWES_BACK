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

    long reviewerId;
    String reviewerImageUrl;
    String reviewerNickname;
    String reviewContent;
    String reviewDate;

    long respondentId;
    String respondentImageUrl;
    String respondentNickname;
    String replyContent;
    String replyDate;

    Boolean isModified;

    public static ClubArticleReviewDto of(Review review){
        Member reviewer = review.getReviewer();
        Member respondent = review.getRespondent();

        ClubArticleReviewDtoBuilder builder = ClubArticleReviewDto.builder()
                .reviewId(review.getId())
                .reviewerId(reviewer.getId())
                .reviewerImageUrl(reviewer.getProfileImg())
                .reviewerNickname(reviewer.getNickname())
                .reviewContent(review.getReviewContent())
                .reviewDate(review.getModifiedDate().format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm")))
                .isModified(review.getIsModified());

        if (respondent != null) {
            builder = builder.respondentImageUrl(respondent.getProfileImg())
                    .respondentNickname(respondent.getNickname())
                    .respondentId(respondent.getId());
        }

        return builder.replyContent(review.getReplyContent())
                .replyDate(review.getReplyDate())
                .build();
    }

}
