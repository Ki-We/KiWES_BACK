package server.api.kiwes.domain.review.dto;

import lombok.*;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.review.entity.Review;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDetailDto {
    String reviewerProfileImg;
    String reviewerNickname;
    String reviewContent;
    String reviewDate;

    String respondentProfileImg;
    String respondentNickname;
    String replyContent;
    String replyDate;

    @Builder.Default
    Boolean isAuthorOfReview = false;

    @Builder.Default
    Boolean isAuthorOfReply = false;

    Boolean isModified;

    public static ReviewDetailDto of(Review review, Member member){
        if(review.getRespondent() == null){
            return ReviewDetailDto.builder()
                    .reviewerProfileImg(review.getReviewer().getProfileImg())
                    .reviewerNickname(review.getReviewer().getNickname())
                    .reviewContent(review.getReviewContent())
                    .reviewDate(review.getModifiedDate().toString())
                    .isAuthorOfReview(review.getReviewer().getId().equals(member.getId()))
                    .isModified(review.getIsModified())
                    .build();
        }

        return ReviewDetailDto.builder()
                .reviewerProfileImg(review.getReviewer().getProfileImg())
                .reviewerNickname(review.getReviewer().getNickname())
                .reviewContent(review.getReviewContent())
                .reviewDate(review.getReviewDate())
                .isAuthorOfReview(review.getReviewer().getId().equals(member.getId()))
                .respondentProfileImg(review.getRespondent().getProfileImg())
                .respondentNickname(review.getRespondent().getNickname())
                .replyContent(review.getReplyContent())
                .replyDate(review.getReplyDate())
                .isAuthorOfReply(review.getRespondent().getId().equals(member.getId()))
                .isModified(review.getIsModified())
                .build();
    }
}
