package server.api.kiwes.domain.club.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubArticleBaseInfoDto {
    Long clubId;
    String title;
    String thumbnailImageUrl;
    Integer heartCount;
    Integer maxPeople;
    List<String> tags;
    String date;
    String dueTo;
    Integer cost;
    String gender;
    String locationKeyword;
    String content;
    String location;
    String latitude; //위도
    String longitude; //경도
    String[] dateInfo;
}
