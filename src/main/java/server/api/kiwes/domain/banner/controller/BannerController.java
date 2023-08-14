package server.api.kiwes.domain.banner.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping()
    public ApiResponse<Object> getBanners() {

        return ApiResponse.of(BannerResponseType.BANNER_LOAD_SUCCESS, bannerService.getBanners());

    }
}

