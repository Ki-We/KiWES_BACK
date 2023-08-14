package server.api.kiwes.domain.club.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.club.dto.ClubApprovalRequestSimpleDto;
import server.api.kiwes.domain.club.dto.ClubApprovalResponseDto;
import server.api.kiwes.domain.club.dto.ClubApprovalWaitingSimpleDto;
import server.api.kiwes.domain.club.dto.ClubWaitingMemberDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.repository.ClubRepository;
import server.api.kiwes.domain.club_language.entity.ClubLanguage;
import server.api.kiwes.domain.club_language.repository.ClubLanguageRepository;
import server.api.kiwes.domain.club_member.repository.ClubMemberRepository;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.language.entity.Language;
import server.api.kiwes.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ClubApprovalService {
    private final ClubRepository clubRepository;
    private final ClubLanguageRepository clubLanguageRepository;
    private final ClubMemberRepository clubMemberRepository;

    /**
     * 승인 페이지에서 보여질 승인요청, 승인대기 Top 2개 정보 리턴
     */
    public ClubApprovalResponseDto getSimpleResponse(Member member) {
        List<ClubApprovalRequestSimpleDto> requests;
        List<ClubApprovalWaitingSimpleDto> waitings;

        requests = clubRepository.findApprovalRequestSimple(member, true);
        if(requests.size() > 2){
            requests.subList(2, requests.size()).clear();
        }

        waitings = clubRepository.findApprovalWaitingSimple(member, false, false);
        if(waitings.size() > 2){
            waitings.subList(2, waitings.size()).clear();
        }

        for(ClubApprovalWaitingSimpleDto waitingSimpleDto : waitings){
            if(waitingSimpleDto.getIsHeart() == null){
                waitingSimpleDto.setIsHeart(HeartStatus.NO);
            }

            List<String> languages = new ArrayList<>();
            for(ClubLanguage clubLanguage : clubLanguageRepository.findByClubId(waitingSimpleDto.getClubId())){
                Language language = clubLanguage.getLanguage();
                languages.add(language.getName().getName());
            }
            waitingSimpleDto.setLanguages(languages);

        }

        return ClubApprovalResponseDto.builder()
                .requests(requests)
                .waitings(waitings)
                .build();
    }

    /**
     * 내가 호스트인 모임 전체 리스트
     */
    public List<ClubApprovalRequestSimpleDto> getRequestsResponse(Member member) {

        return clubRepository.findApprovalRequestSimple(member, true);
    }

    public List<ClubApprovalWaitingSimpleDto> getWaitingsResponse(Member member) {
        List<ClubApprovalWaitingSimpleDto> waitings = clubRepository.findApprovalWaitingSimple(member, false, false);
        for(ClubApprovalWaitingSimpleDto waitingSimpleDto : waitings){
            if(waitingSimpleDto.getIsHeart() == null){
                waitingSimpleDto.setIsHeart(HeartStatus.NO);
            }

            List<String> languages = new ArrayList<>();
            for(ClubLanguage clubLanguage : clubLanguageRepository.findByClubId(waitingSimpleDto.getClubId())){
                Language language = clubLanguage.getLanguage();
                languages.add(language.getName().getName());
            }
            waitingSimpleDto.setLanguages(languages);
        }

        return waitings;
    }

    /**
     * 해당 모임에서 승인 대기중인 사용자들 정보 리턴
     */
    public List<ClubWaitingMemberDto> getClubWaitingPeople(Club club) {

        return clubMemberRepository.findClubMembersWaitingFrom(club, false).stream()
                .map(ClubWaitingMemberDto::of)
                .collect(Collectors.toList());
    }
}
