package server.api.kiwes.domain.club.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClubArticleRequestDto {
    private String date;             // 만나는 날짜, YYYYMMDD
    private String dueTo;            // 모집 마감 날짜, YYYYMMDD
    private Integer cost;            // 인당 예상 비용
    private Integer maxPeople;       // 모집 정원
    private String gender;           // 모집 성별 (MALE, FEMALE, ALL)
    private String title;            // 제목
    private String content;          // 모임 소개
    private String locationsKeyword; // 위치 키워드
    private String location;         // 위도, 경도
    private List<String> languages;  // 언어
    private List<String> categories; // 카테고리
}
