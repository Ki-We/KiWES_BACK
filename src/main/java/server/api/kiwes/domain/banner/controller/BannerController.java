package server.api.kiwes.domain.banner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.api.kiwes.domain.banner.constant.BannerResponseType;
import server.api.kiwes.domain.banner.service.BannerService;
import server.api.kiwes.response.ApiResponse;

@Api(tags = "Banner - 홈화면 배너", value = "홈화면(배너) 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/banner")
public class BannerController {

    private final BannerService bannerService;

    @ApiOperation(value = "배너 가져오기", notes =
            "예시 출력 데이터\n" +
                    "\"status\": 20401,\n" +
                    "\"message\": \"성공\",\n" +
                    "\"data\": [\n" +
                    " {type\": \"String\",\n" +
                    "\"imageUrl\": \"String\",\n" +
                    "\"Url\": \"String\",\n" +
                    " \"id\": 0 }"
    )
    @GetMapping()
    public ApiResponse<Object> getBanners() {

        return ApiResponse.of(BannerResponseType.BANNER_LOAD_SUCCESS, bannerService.getBanners());

    }

    @ApiOperation(value = "배너 하나 찾아오기", notes =
            "예시 출력 데이터\n" +
                    "\"status\": 20401,\n" +
                    "\"message\": \"성공\",\n" +
                    "\"data\": [\n" +
                    " {type\": \"String\",\n" +
                    "\"imageUrl\": \"String\",\n" +
                    "\"Url\": \"String\",\n" +
                    " \"id\": 0 }"
    )
    @GetMapping("/{bannerId}")
    public ApiResponse<Object> getBanner(@PathVariable Long bannerId) {

        return ApiResponse.of(BannerResponseType.BANNER_LOAD_SUCCESS, bannerService.getBanner(bannerId));

    }
}

