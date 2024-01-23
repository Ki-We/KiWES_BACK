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
    Long reviewId;

    Long reviewerId;
    String reviewerProfileImg;
    String reviewerNickname;
    String reviewContent;
    String reviewDate;

    Long respondentId;
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
                    .reviewId(review.getId())
                    .reviewerId(review.getReviewer().getId())
                    .reviewerProfileImg(review.getReviewer().getProfileImg())
                    .reviewerNickname(review.getReviewer().getNickname())
                    .reviewContent(review.getReviewContent())
                    .reviewDate(review.getReviewDate())
                    .isAuthorOfReview(review.getReviewer().getId().equals(member.getId()))
                    .isModified(review.getIsModified())
                    .build();
        }

        return ReviewDetailDto.builder()
                .reviewId(review.getId())
                .reviewerId(review.getReviewer().getId())
                .reviewerProfileImg(review.getReviewer().getProfileImg())
                .reviewerNickname(review.getReviewer().getNickname())
                .reviewContent(review.getReviewContent())
                .reviewDate(review.getReviewDate())
                .isAuthorOfReview(review.getReviewer().getId().equals(member.getId()))
                .respondentId(review.getRespondent().getId())
                .respondentProfileImg(review.getRespondent().getProfileImg())
                .respondentNickname(review.getRespondent().getNickname())
                .replyContent(review.getReplyContent())
                .replyDate(review.getReplyDate())
                .isAuthorOfReply(review.getRespondent().getId().equals(member.getId()))
                .isModified(review.getIsModified())
                .build();
    }
}
