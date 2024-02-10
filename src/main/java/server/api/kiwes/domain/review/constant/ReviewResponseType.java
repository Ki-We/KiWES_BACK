package server.api.kiwes.domain.review.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.kiwes.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum ReviewResponseType implements BaseResponseType {
    POST_SUCCESS(21201, "후기 등록 완료", HttpStatus.OK),
    MODIFY_SUCCESS(21202, "후기 수정 완료", HttpStatus.OK),
    DELETE_SUCCESS(21203, "후기 삭제 완료", HttpStatus.OK),
    ENTIRE_LIST(21204, "후기 모두 보기", HttpStatus.OK),
    REPLY_SUCCESS(21205, "후기 답글 등록 성공", HttpStatus.OK),

    NOT_CLUB_MEMBER(41201, "모임 멤버가 아님", HttpStatus.BAD_REQUEST),
    ALREADY_POSTED(41202, "사용자가 이미 후기 작성을 하였음", HttpStatus.BAD_REQUEST),
    NOT_EXIST(41203, "ID와 일치하는 후기가 없음", HttpStatus.NOT_FOUND),
    NOT_AUTHOR(41204, "작성자가 아님", HttpStatus.UNAUTHORIZED),
    CHECK_PATH(41205, "해당 리뷰는 이 모임의 것이 아님", HttpStatus.BAD_REQUEST),
    NOT_HOST(41206, "호스트가 아니므로 답글을 달 수 없음", HttpStatus.UNAUTHORIZED),
    NOT_WRITTER_HOST(41207, "호스트는 후기 달 수 없음", HttpStatus.BAD_REQUEST),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
