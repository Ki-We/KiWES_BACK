package server.api.kiwes.domain.club.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubJoinedResponseDto {
    Long clubId;
    String clubTitle;
    Long participantId;
    String participantNickname;
}
