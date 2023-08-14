package server.api.kiwes.domain.club.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubCreatedResponseDto {
    Long clubId;
    String clubTitle;
    Long hostId;
    String hostNickname;
    Integer clubMaxPeople;
}
