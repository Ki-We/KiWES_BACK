package server.api.kiwes.domain.category.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryType {
    KPOP("KPOP"),                        // K-POP
    KOREAN_CULTURE("KOREAN_CULTURE"),    // 한국 문화
    CAFE("CAFE"),                        // 맛집, 카페
    SPORTS("SPORTS"),                    // 스포츠
    CULTURE("CULTURE"),                  // 문화, 전시, 공연
    TRAVEL("TRAVEL"),                   // 여행
    STUDY("STUDY"),                     // 스터디
    GAME("GAME"),                       // 게임, 보드게임
    PARTY("PARTY"),                     // 파티, 클럽
    DRINK("DRINK"),                     // 술
    MOVIE("MOVIE"),                     // 영화, 드라마, 애니
    CRAFT("CRAFT"),                     // 공예, 그림
    VOLUNTEER("VOLUNTEER"),             // 봉사활동
    OTHER("OTHER"),                     // 기타
    ;
    private final String name;
}
