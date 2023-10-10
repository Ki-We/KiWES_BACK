package server.api.kiwes.domain.club.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubMembersInfoDto {
    Long id;
    String Nickname;
    String thumbnail;
}
