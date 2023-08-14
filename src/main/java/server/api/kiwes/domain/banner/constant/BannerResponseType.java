package server.api.kiwes.domain.banner.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import server.api.kiwes.response.BaseResponseType;

@Getter
@AllArgsConstructor
public enum BannerResponseType implements BaseResponseType {

    BANNER_LOAD_SUCCESS(20201, "배너 로딩 성공", HttpStatus.OK);


    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
