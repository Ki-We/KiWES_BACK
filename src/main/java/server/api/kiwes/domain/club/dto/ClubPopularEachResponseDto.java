package server.api.kiwes.domain.club.dto;

import lombok.*;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.constant.HeartStatus;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubPopularEachResponseDto {
    @Setter
    private String hostProfileImg;
    private String title;
    private String thumbnailImage;
    private String date;
    private String location;
    private String locationsKeyword;
    private List<String> languages;
    @Setter
    private HeartStatus isHeart;

    public static ClubPopularEachResponseDto of(Club club){
        return ClubPopularEachResponseDto.builder()
                .hostProfileImg(null)
                .title(club.getTitle())
                .thumbnailImage(club.getThumbnailUrl())
                .date(club.getDueTo())
                .location(club.getLocation())
                .locationsKeyword(club.getLocationsKeyword())
                .languages(club.getLanguages().stream().map(clubLanguage -> clubLanguage.getLanguage().getName().getName()).collect(Collectors.toList()))
                .isHeart(null)
                .build();
    }
}
