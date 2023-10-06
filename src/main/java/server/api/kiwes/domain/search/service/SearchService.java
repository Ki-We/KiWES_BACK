package server.api.kiwes.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.category.entity.Category;
import server.api.kiwes.domain.club.repository.ClubRepository;
import server.api.kiwes.domain.language.entity.Language;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member_category.entity.MemberCategory;
import server.api.kiwes.domain.member_language.entity.MemberLanguage;
import server.api.kiwes.domain.search.dto.SearchResponseDto;
import server.api.kiwes.domain.search_count.dto.SearchCountResultDto;
import server.api.kiwes.domain.search_count.entity.SearchCount;
import server.api.kiwes.domain.search_count.repository.SearchCountRepository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {
    private final ClubRepository clubRepository;
    private final SearchCountRepository searchCountRepository;

    public List<SearchResponseDto> search(String keyword, Member member,int cursor) {
        List<SearchResponseDto> searchResults = clubRepository.findByTitlePage(keyword,cursor).stream().map(club -> SearchResponseDto.of(club, member)).collect(Collectors.toList());
        List<SearchResponseDto> responseDtos = new ArrayList<>();

        // 언어 일치하는게 있으면 삽입
        for(SearchResponseDto searchResult : searchResults){

            List<String> languageStrings = searchResult.getLanguages();

            // 사용자 언어중에 클럽 언어와 일치하는게 있으면 새 리스트 삽입
            for(MemberLanguage memberLanguage : member.getLanguages()){
                Language language = memberLanguage.getLanguage();
                if(languageStrings.contains(language.getName().getName())){
                    responseDtos.add(searchResult);
                    break;
                }
            }
        }

        // 카테고리 일치하는 게 있으면 삽입
        for(SearchResponseDto searchResult : searchResults){
            if(responseDtos.contains(searchResult)) continue;

            // 사용자 카테고리중에 클럽 카테고리와 일치하는게 있으면 새 리스트 삽입
            List<String> categoryStrings = searchResult.getCategories();
            for(MemberCategory memberCategory : member.getCategories()){
                Category category = memberCategory.getCategory();
                if(categoryStrings.contains(category.getName().getName())){
                    responseDtos.add(searchResult);
                    break;
                }
            }
        }

        for(SearchResponseDto searchResult : searchResults){
            if(responseDtos.contains(searchResult)) continue;

            responseDtos.add(searchResult);
        }

        saveSearchHistory(keyword);
        return responseDtos;
    }

    /**
     * 인기검색어를 위해 검색어 저장
     */
    private void saveSearchHistory(String keyword){
        Optional<SearchCount> search = searchCountRepository.findBySearchWordIgnoreCaseAndDate(keyword, LocalDate.now());

        if(search.isEmpty()){
            SearchCount searchCount = SearchCount.builder()
                    .date(LocalDate.now())
                    .searchWord(keyword)
                    .build();
            searchCountRepository.save(searchCount);
            return;
        }

        search.get().addCount();
    }

    /**
     * 인기검색어 5개 뽑아 리턴
     */
    public List<String> getPopularSearchKeyword() {

        return searchCountRepository.findAllGroupByKeyword()
                .stream()
                .limit(5)
                .map(SearchCountResultDto::getKeyword)
                .collect(Collectors.toList());
    }
}
