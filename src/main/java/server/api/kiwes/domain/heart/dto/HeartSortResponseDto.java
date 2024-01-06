package server.api.kiwes.domain.heart.dto;

import lombok.*;
import server.api.kiwes.domain.club_language.entity.ClubLanguage;
import server.api.kiwes.domain.heart.constant.HeartStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeartSortResponseDto {
    Long clubId;
    String title;
    String thumbnailImage;
    String date;
    String locationKeyword;
    List<String> languages;
    @Builder.Default()
    HeartStatus isHeart = HeartStatus.YES;

    public HeartSortResponseDto(Long club_id, String title, String thumbnailImage, String date, String LocationsKeyword) {
        this.clubId = club_id;
        this.title = title;
        this.thumbnailImage = thumbnailImage;
        this.date = date;
        this.locationKeyword = LocationsKeyword;

    }

    public void setLanguages(List<ClubLanguage> languages) {
        languages.forEach(languageType -> {
            if (this.languages == null) {
                this.languages = new ArrayList<>();
            }
            this.languages.add(languageType.getLanguage().getName().toString());
        });
    }
}
