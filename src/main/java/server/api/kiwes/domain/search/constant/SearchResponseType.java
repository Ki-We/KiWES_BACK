package server.api.kiwes.domain.search.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import server.api.kiwes.response.BaseResponseType;

@Getter
@RequiredArgsConstructor
public enum SearchResponseType implements BaseResponseType {
    SEARCH_SUCCESS(21301, "검색 결과 응답 성공", HttpStatus.OK),
    POPULAR_SEARCH_KEYWORD(21302, "인기 검색어 응답 성공", HttpStatus.OK),

    NO_RESULT(41301, "검색 결과가 없습니다.", HttpStatus.OK),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
