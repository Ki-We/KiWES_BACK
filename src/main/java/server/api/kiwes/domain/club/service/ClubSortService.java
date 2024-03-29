package server.api.kiwes.domain.club.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.category.entity.Category;
import server.api.kiwes.domain.category.repository.CategoryRepository;
import server.api.kiwes.domain.category.type.CategoryType;
import server.api.kiwes.domain.club.constant.ClubResponseType;
import server.api.kiwes.domain.club.dto.ClubSortInterface;
import server.api.kiwes.domain.club.dto.ClubSortResponseDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.repository.ClubRepository;
import server.api.kiwes.domain.club_category.entity.ClubCategory;
import server.api.kiwes.domain.club_category.repository.ClubCategoryRepository;
import server.api.kiwes.domain.club_language.entity.ClubLanguage;
import server.api.kiwes.domain.club_language.repository.ClubLanguageRepository;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.heart.entity.Heart;
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


    public List<ClubSortResponseDto> getClubsByCursor(int cursor, Long memberId){
        List<Club> clubByPage = clubRepository.findAllbyCursor(cursor);
        List<ClubSortResponseDto> clubsbyPageDTO = new ArrayList<>();
        for (Club club : clubByPage) {
            List<String> languages= new ArrayList<>();
            List<ClubLanguage> clubLanguageList = club.getLanguages();
            for (ClubLanguage cl : clubLanguageList) {
                languages.add(cl.getLanguage().getName().getName());
            }
            List<Heart> heartLIst = club.getHearts();
            HeartStatus isHeart = HeartStatus.NO;
            for (Heart heart : heartLIst) {
                if(heart.getMember().getId() == memberId){
                    isHeart = heart.getStatus();
                }
            }
            clubsbyPageDTO.add(
                    ClubSortResponseDto.builder()
                            .clubId(club.getId())
                            .title(club.getTitle())
                            .thumbnailImage("https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/clubThumbnail/"+
                                    club.getThumbnailUrl())
                            .date(club.getDate())
                            .location(club.getLocation())
                            .latitude(club.getLatitude())
                            .longitude(club.getLongitude())
                            .languages(languages)
                            .isHeart(isHeart)
                            .build());
        }
        return clubsbyPageDTO;
    }
    /**
     * 카테고리별 모임
     * @param categories
     */
    public List<ClubSortResponseDto> getClubByCategory(List<String> categories, int cursor){
        Member member = memberService.getLoggedInMember();
        List<Long> clubIds = new ArrayList<>();

        for (String categoryString : categories) {
            CategoryType type = CategoryType.valueOf(categoryString);
            Category category = categoryRepository.findByName(type);
            clubIds.add(category.getId());
        }
        return getClubSortResponseDtosAllByTypeIds(member,clubCategoryRepository.findAllByTypeIds(clubIds,member.getId(),cursor));


    }
    public List<ClubSortResponseDto> getClubByLanguages(List<String> languages, int cursor){
        Member member = memberService.getLoggedInMember();
        List<Long> clubIds = new ArrayList<>();

        for (String languageString : languages) {
            LanguageType type = LanguageType.valueOf(languageString);
            Language language = languageRepository.findByName(type);
            clubIds.add(language.getId());
        }
        return getClubSortResponseDtosAllByTypeIds(member, clubLanguageRepository.findAllByTypeIds(clubIds,member.getId(),cursor));
    }
    @NotNull
    private List<ClubSortResponseDto> getClubSortResponseDtosAllByTypeIds(Member member,List<ClubSortInterface> allByTypeIds) {
        List<ClubSortResponseDto> clubsbyPageDTOs =  new ArrayList<>();
        for (ClubSortInterface c : allByTypeIds) {
            clubsbyPageDTOs.add(
                    new ClubSortResponseDto(c.getClub_id(),c.getTitle(),c.getThumbnail_url(),c.getDate(),
                            c.getLocation(),c.getLatitude(),c.getLongitude(),c.getStatus()));
        }
        for (ClubSortResponseDto clubsbyPageDTO : clubsbyPageDTOs) {
            Club club = findById(clubsbyPageDTO.getClubId());

            clubsbyPageDTO.setLanguages(club.getLanguages());

            if (club.getHearts().size() > 0) {
                club.getHearts().forEach(heartMember -> {
                    if (heartMember.getId().equals(member.getId())) {
                        clubsbyPageDTO.setHeart(true);
                    }
                });
            } else{
                clubsbyPageDTO.setHeart(false);
            }
        }
        return clubsbyPageDTOs;
    }
}
