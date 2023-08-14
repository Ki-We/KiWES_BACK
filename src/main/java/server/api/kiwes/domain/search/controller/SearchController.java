package server.api.kiwes.domain.search.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.api.kiwes.domain.member.service.MemberService;
import server.api.kiwes.domain.search.dto.SearchResponseDto;
import server.api.kiwes.domain.search.constant.SearchResponseType;
import server.api.kiwes.domain.search.service.SearchService;
import server.api.kiwes.response.ApiResponse;

import java.util.List;

@Api(tags = "Search - 검색")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SearchController {
    private final SearchService searchService;
    private final MemberService memberService;

    @ApiOperation(value = "검색")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21301, message = "검색 결과 응답 성공 (200)"),
            @io.swagger.annotations.ApiResponse(code = 41301, message = "검색 결과가 없습니다. (200)"),
    })
    @GetMapping("/search")
    public ApiResponse<List<SearchResponseDto>> search(@RequestParam String keyword){
        List<SearchResponseDto> response = searchService.search(keyword.trim(), memberService.getLoggedInMember());
        if(response.isEmpty()){
            return ApiResponse.of(SearchResponseType.NO_RESULT);
        }

        return ApiResponse.of(SearchResponseType.SEARCH_SUCCESS, response);
    }
    
    @ApiOperation(value = "인기 검색어", notes = "3일간 TOP 5, 배열인데 앞에서부터 1위")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 21302, message = "인기 검색어 응답 성공"),
    })
    @GetMapping("/search/popular")
    public ApiResponse<List<String>> getPopularSearchKeyword(){
        return ApiResponse.of(SearchResponseType.POPULAR_SEARCH_KEYWORD, searchService.getPopularSearchKeyword());
    }
}
