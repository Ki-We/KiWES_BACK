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

    public ClubSortResponseDto(Long clubId, String title, String thumbnailImage, String date, String location) {
        this.clubId = clubId;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
        this.date = date;
        this.location = location;

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



