package server.api.kiwes.domain.club.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.kiwes.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum ClubResponseType implements BaseResponseType {
    POST_SUCCESS(20101, "모임 모집 글 업로드 성공", HttpStatus.OK),
    DELETE_SUCCESS(20102, "모임 모집 글 삭제 성공", HttpStatus.OK),
    APPLICATION_SUCCESS(20103, "참여 신청 성공", HttpStatus.OK),
    WITHDRAWAL_SUCCESS(20104, "참여 취소 성공", HttpStatus.OK),
    APPROVE_SUCCESS(20105, "멤버 승인 성공", HttpStatus.OK),
    DENY_SUCCESS(20106, "멤버 거절 성공", HttpStatus.OK),
    KICK_OUT_SUCCESS(20107, "멤버 강퇴 성공", HttpStatus.OK),
    GET_INFO_SUCCESS(20108, "모임 정보 불러오기 성공", HttpStatus.OK),
    APPROVAL_LIST_GET_SUCCEED(20109, "승인관련 리스트 리턴 성공", HttpStatus.OK),
    CLUB_SORT_BY_CATEGORY_SUCCESS(20110, "카테고리별 모임 조회 성공", HttpStatus.OK),
    CLUB_SORT_BY_LANGUAGE_SUCCESS(20111, "언어별 모임 조회 성공", HttpStatus.OK),
    POPULAR_CLUBS(20112, "인기 모임 조회", HttpStatus.OK),
    CLUB_THUMBNAIL_IMG_PRESIGNED_URL(20113, "모임 썸네일 이미지 업로드 프리사인url 리턴 성공", HttpStatus.OK),
    CLUB_ALL_SUCCESS(20114, "모임 전체 조회 성공", HttpStatus.OK),

    CLUB_NOT_EXIST(40101, "clubId와 일치하는 모임이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ALREADY_APPLIED(40102, "호스트이거나 이미 참여신청함.", HttpStatus.BAD_REQUEST),
    NOT_HOST(40103, "호스트가 아님", HttpStatus.UNAUTHORIZED),
    NOT_APPLIED(40104, "모임의 지원한 사용자가 아님", HttpStatus.BAD_REQUEST),
    OVER_THE_LIMIT(40105, "정원이 초과되었습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_APPROVED(40106, "이미 승인된 사용자입니다.", HttpStatus.BAD_REQUEST),
    HOST_CANNOT_CANCEL(40107, "호스트는 참여 취소를 할 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_DATE_FORMAT(40108, "날짜 요청 형식이 잘못 되었습니다.", HttpStatus.BAD_REQUEST),

    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
