package server.api.kiwes.domain.club.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubApprovalResponseDto {
    List<ClubApprovalRequestSimpleDto> requests;
    List<ClubApprovalWaitingSimpleDto> waitings;
}
