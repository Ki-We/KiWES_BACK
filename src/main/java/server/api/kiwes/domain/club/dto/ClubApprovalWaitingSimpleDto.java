package server.api.kiwes.domain.club.dto;

import lombok.*;
import server.api.kiwes.domain.heart.constant.HeartStatus;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubApprovalWaitingSimpleDto {
    Long clubId;
    String title;
    String thumbnailImage;
    String date;
    String location;
    List<String> languages;
    HeartStatus isHeart;

    public ClubApprovalWaitingSimpleDto(Long clubId, String title, String thumbnailImage, String date, String location, HeartStatus isHeart) {
        this.clubId = clubId;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
        this.date = date;
        this.location = location;
        this.isHeart = isHeart;
    }
}
