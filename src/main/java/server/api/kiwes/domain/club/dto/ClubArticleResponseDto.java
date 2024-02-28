package server.api.kiwes.domain.club.dto;

import lombok.*;
import server.api.kiwes.domain.club.constant.ClubStatus;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubArticleResponseDto {
    ClubArticleBaseInfoDto baseInfo;
    ClubArticleMemberInfoDto memberInfo;
    List<ClubArticleQnaDto> qnas;
    List<ClubArticleReviewDto> reviews;

    @Builder.Default
    Boolean isHost = false;
    @Builder.Default
    Boolean isApproval = false;
    Boolean isHeart;
    ClubStatus isActivated;

}
