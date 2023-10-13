package server.api.kiwes.domain.club.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubMemberInfoDto {
    Long hostId;
    String hostThumbnailImage;
    String hostNickname;
    Integer currentPeople;
    String title;
    List<ClubMembersInfoDto> Members;
}
