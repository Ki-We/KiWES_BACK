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
    String locationKeyword;
    List<String> languages;
    HeartStatus isHeart;

    public ClubApprovalWaitingSimpleDto(Long clubId, String title, String thumbnailImage, String date, String Location_keyword, HeartStatus isHeart) {
        this.clubId = clubId;
        this.title = title;
        this.thumbnailImage = "https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/clubThumbnail/"+
                thumbnailImage;
        this.date = date;
        this.locationKeyword = Location_keyword;
        this.isHeart = isHeart;
    }
}
