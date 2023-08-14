package server.api.kiwes.domain.club.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubApprovalRequestSimpleDto {
    Long clubId;
    String title;
    Integer currentPeople;
}
