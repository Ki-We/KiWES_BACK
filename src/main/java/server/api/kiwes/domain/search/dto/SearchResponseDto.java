package server.api.kiwes.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.heart.entity.Heart;
import server.api.kiwes.domain.member.entity.Member;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResponseDto {
    private Long clubId;
    private String title;
    private String thumbnailImage;
    private String date;
    private String locationKeyword;
    private List<String> languages;
    private HeartStatus isHeart;

    private String category;

    public static SearchResponseDto of(Club club, Member member){
        // 클럽의 하트 수가 그렇게 많지 않을 것이라 판단.
        // DB 요청을 통해 불러오는 것보다 그냥 여기서 반복문 돌리는 게 더 효율적일 수도 있다고 판단하여 로직을 이렇게 짰는데,
        // 추후 사용자가 늘어나 클럽당 하트 수가 많아지면, DB 요청을 통해서 불러오도록 수정하는게 좋아 보입니다.
        HeartStatus isHeart = HeartStatus.NO;
        for(Heart heart : club.getHearts()){
            if(heart.getMember().equals(member)){
                isHeart = heart.getStatus();
                break;
            }
        }

        return SearchResponseDto.builder()
                .clubId(club.getId())
                .thumbnailImage(club.getThumbnailUrl())
                .title(club.getTitle())
                .date(club.getDueTo())
                .locationKeyword(club.getLocationKeyword())
                .languages(club.getLanguages()
                        .stream()
                        .map(clubLanguage -> clubLanguage.getLanguage().getName().getName())
                        .collect(Collectors.toList()))
                .isHeart(isHeart)
                .build();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResponseDto that = (SearchResponseDto) o;
        return Objects.equals(clubId, that.clubId) &&
                Objects.equals(thumbnailImage, that.thumbnailImage) &&
                Objects.equals(title, that.title) &&
                Objects.equals(date, that.date) &&
                Objects.equals(locationKeyword, that.locationKeyword) &&
                Objects.equals(languages, that.languages) &&
                isHeart == that.isHeart;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clubId, thumbnailImage, title, date, locationKeyword, languages, isHeart);
    }
}
