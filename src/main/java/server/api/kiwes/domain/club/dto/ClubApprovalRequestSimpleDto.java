package server.api.kiwes.domain.club.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ClubApprovalRequestSimpleDto {
    Long clubId;
    String title;
    Integer currentPeople;


    public ClubApprovalRequestSimpleDto(Long clubId, String title,  Integer currentPeople) {
        this.clubId = clubId;
        this.title = title;
        this.currentPeople = currentPeople;
    }
}
