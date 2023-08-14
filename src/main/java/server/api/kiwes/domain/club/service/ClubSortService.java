package server.api.kiwes.domain.club.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.category.entity.Category;
import server.api.kiwes.domain.category.repository.CategoryRepository;
import server.api.kiwes.domain.category.type.CategoryType;
import server.api.kiwes.domain.club.constant.ClubResponseType;
import server.api.kiwes.domain.club.dto.ClubSortResponseDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.repository.ClubRepository;
import server.api.kiwes.domain.club_category.entity.ClubCategory;
import server.api.kiwes.domain.club_category.repository.ClubCategoryRepository;
import server.api.kiwes.domain.club_language.entity.ClubLanguage;
import server.api.kiwes.domain.club_language.repository.ClubLanguageRepository;
import server.api.kiwes.domain.language.entity.Language;
import server.api.kiwes.domain.language.language.LanguageRepository;
import server.api.kiwes.domain.language.type.LanguageType;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member.service.MemberService;
import server.api.kiwes.response.BizException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubSortService {

    private final ClubRepository clubRepository;

    private final ClubCategoryRepository clubCategoryRepository;
    private final CategoryRepository categoryRepository;

    private final LanguageRepository languageRepository;
    private final ClubLanguageRepository clubLanguageRepository;
    private final MemberService memberService;

    /**
     * club id를 통해 club 정보 불러오기
     */
    public Club findById(Long id){
        return clubRepository.findById(id)
                .orElseThrow(() -> new BizException(ClubResponseType.CLUB_NOT_EXIST));
    }

    /**
     * 카테고리별 모임
     * @param categories
     */

    public List<ClubSortResponseDto> getClubByCategory(List<String> categories){


        // 카테고리 id list
        Member member = memberService.getLoggedInMember();
        List<Long> clubIds = new ArrayList<>();

        for (String categoryString : categories) {
            CategoryType type = CategoryType.valueOf(categoryString);
            Category category = categoryRepository.findByName(type);
            clubIds.add(category.getId());
        }

        List<ClubSortResponseDto> allByCategoryIds = clubCategoryRepository.findAllByCategoryIds(clubIds);
        for (ClubSortResponseDto allByCategoryId : allByCategoryIds) {
            Club club = findById(allByCategoryId.getClubId());

            allByCategoryId.setLanguages(club.getLanguages());

            if (club.getHearts().size() > 0) {
                club.getHearts().forEach(heartMember -> {
                    if (heartMember.getId().equals(member.getId())) {
                        allByCategoryId.setHeart(true);
                    }
                });
            } else{
                allByCategoryId.setHeart(false);
            }
        }

        return allByCategoryIds;


    }


    public List<ClubSortResponseDto> getClubByLanguages(List<String> languages){


        // 카테고리 id list
        Member member = memberService.getLoggedInMember();
        List<Long> clubIds = new ArrayList<>();

        for (String languageString : languages) {
            LanguageType type = LanguageType.valueOf(languageString);
            Language language = languageRepository.findByName(type);
            clubIds.add(language.getId());
        }

        List<ClubSortResponseDto> allByLanguageIds = clubLanguageRepository.findAllByCategoryIds(clubIds);
        for (ClubSortResponseDto allByLanguageId : allByLanguageIds) {
            Club club = findById(allByLanguageId.getClubId());

            allByLanguageId.setLanguages(club.getLanguages());

            if (club.getHearts().size() > 0) {
                club.getHearts().forEach(heartMember -> {
                    if (heartMember.getId().equals(member.getId())) {
                        allByLanguageId.setHeart(true);
                    }
                });
            } else{
                allByLanguageId.setHeart(false);
            }
        }

        return allByLanguageIds;


    }


}
