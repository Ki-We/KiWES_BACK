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
    List<String> tags;
    String date;
    String dueTo;
    Integer cost;
    String gender;
    String locationsKeyword;
    String content;
    String location;
}
