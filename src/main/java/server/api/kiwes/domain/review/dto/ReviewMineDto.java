package server.api.kiwes.domain.review.dto;

import lombok.*;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.review.entity.Review;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewMineDto {
    Long reviewId;

    String reviewContent;
    String reviewDate;

    Long clubId;
    String clubTitle;

    boolean isMine;
}
