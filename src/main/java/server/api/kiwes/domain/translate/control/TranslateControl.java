package server.api.kiwes.domain.translate.control;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.api.kiwes.domain.translate.constant.TranslateResponseType;
import server.api.kiwes.domain.translate.dto.TranslateRequsetDto;
import server.api.kiwes.domain.translate.service.TranslateService;
import server.api.kiwes.response.ApiResponse;

@Api(tags = "Translate")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TranslateControl {
    private final TranslateService translateService;

    @ApiOperation(value = "translate 위한 API")
    @PostMapping("/translate")
    public ApiResponse<String> translate(@RequestBody TranslateRequsetDto requset){
        return ApiResponse.of(TranslateResponseType.TRANSLATE_SUCCESS,
                translateService.translate(requset));
    }
}
