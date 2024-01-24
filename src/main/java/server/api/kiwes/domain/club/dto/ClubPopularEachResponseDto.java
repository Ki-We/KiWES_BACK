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
    private Long clubId;
    @Setter
    private String hostProfileImg;
    private String title;
    private String thumbnailImage;
    private String date;
    private String location;
    private String locationKeyword;
    private List<String> languages;
    private String latitude; //위도
    private String longitude; //경도
    @Setter
    private HeartStatus isHeart;

    public static ClubPopularEachResponseDto of(Club club){
        return ClubPopularEachResponseDto.builder()
                .clubId(club.getId())
                .hostProfileImg(null)
                .title(club.getTitle())
                .thumbnailImage(club.getThumbnailUrl())
                .date(club.getDueTo())
                .location(club.getLocation())
                .latitude(club.getLatitude())
                .longitude(club.getLongitude())
                .locationKeyword(club.getLocationKeyword())
                .languages(club.getLanguages().stream().map(clubLanguage -> clubLanguage.getLanguage().getName().getName()).collect(Collectors.toList()))
                .isHeart(null)
                .build();
    }
}
