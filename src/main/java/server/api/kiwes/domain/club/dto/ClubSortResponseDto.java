package server.api.kiwes.domain.club.dto;

import lombok.*;
import server.api.kiwes.domain.club_language.entity.ClubLanguage;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.language.type.LanguageType;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubSortResponseDto {

    Long clubId;
    String title;
    String thumbnailImage;
    String date;
    String location;
    List<String> languages;
    HeartStatus isHeart;
    String latitude; //위도
    String longitude; //경도

    public ClubSortResponseDto(Long club_id, String title, String thumbnailImage, String date, String location, String latitude, String longitude, HeartStatus status) {
        this.clubId = club_id;
        this.title = title;
        this.thumbnailImage = "https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/clubThumbnail/"+
                thumbnailImage;
        this.date = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isHeart = status;

    }

    public void setLanguages(List<ClubLanguage> languages) {
        languages.forEach(languageType -> {
            if (this.languages == null) {
                this.languages = new ArrayList<>();
            }
            this.languages.add(languageType.getLanguage().getName().toString());
        });
    }

    public void setHeart(Boolean b) {
        if (b) {
            this.isHeart = HeartStatus.YES;
        } else {
            this.isHeart = HeartStatus.NO;
        }
    }
}



