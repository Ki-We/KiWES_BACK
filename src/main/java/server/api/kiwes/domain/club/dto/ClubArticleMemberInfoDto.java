package server.api.kiwes.domain.club.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubArticleMemberInfoDto {
    Long hostId;
    String hostThumbnailImage;
    String hostNickname;
    Integer koreanCount;
    Integer foreignerCount;
    Integer maxPeople;
}
