package server.api.kiwes.domain.club.dto;

import lombok.*;
import server.api.kiwes.domain.heart.constant.HeartStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubMineImageDto {
    Long clubId;
    String thumbnailImage;
}
