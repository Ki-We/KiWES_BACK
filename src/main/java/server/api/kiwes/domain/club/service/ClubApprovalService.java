package server.api.kiwes.domain.club.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.club.dto.*;
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
    public List<ClubApprovalRequestSimpleDto> getSimpleApproval(Member member) {
        List<ClubApprovalRequestSimpleInterface> requests;
        requests = clubRepository.findApprovalRequestSimpleLimit2(member, true);
        List<ClubApprovalRequestSimpleDto> requestDTOs =  new ArrayList<>();
        for (ClubApprovalRequestSimpleInterface c : requests) {
            requestDTOs.add(
                    new ClubApprovalRequestSimpleDto(c.getClub_id(),c.getTitle(),c.getCurrent_people()));
        }
        return requestDTOs;
    }
    public List<ClubApprovalWaitingSimpleDto> getSimpleWating(Member member) {
        List<ClubApprovalWaitingSimpleInterface> waitings;
        waitings = clubRepository.findApprovalWaitingSimplelimit2(member, false, false);
        List<ClubApprovalWaitingSimpleDto> waitingDTOs =  new ArrayList<>();
        for (ClubApprovalWaitingSimpleInterface c : waitings) {
            waitingDTOs.add(
                    new ClubApprovalWaitingSimpleDto(c.getClub_id(),c.getTitle(),c.getThumbnail_url(),c.getDate(),c.getLocation_keyword(),c.getStatus()));
        }
        getWaitingSimpleDto(waitingDTOs);
        return waitingDTOs;
    }

    /**
     * 내가 호스트인 모임 전체 리스트
     */
    public List<ClubApprovalRequestSimpleDto> getRequestsResponse(Member member,int cursor) {
        List<ClubApprovalRequestSimpleInterface> requests;
        requests = clubRepository.findApprovalRequestSimple(member, true,cursor*7);
        List<ClubApprovalRequestSimpleDto> requestDTOs =  new ArrayList<>();
        for (ClubApprovalRequestSimpleInterface c : requests) {
            requestDTOs.add(
                    new ClubApprovalRequestSimpleDto(c.getClub_id(),c.getTitle(),c.getCurrent_people()));
        }
        return requestDTOs;
    }

    public List<ClubApprovalRequestSimpleDto> getAllMyClub(Member member){
        List<ClubApprovalRequestSimpleInterface> requests;
        requests = clubRepository.findAllMyClub(member);
        List<ClubApprovalRequestSimpleDto> requestDTOs =  new ArrayList<>();
        for (ClubApprovalRequestSimpleInterface c : requests) {
            requestDTOs.add(
                    new ClubApprovalRequestSimpleDto(c.getClub_id(),c.getTitle(),c.getCurrent_people()));
        }
        return requestDTOs;
    }
    public List<ClubMineImageDto> getAllMyClubImage(Member member) {
        List<ClubMineImageInterface> requests = clubRepository.findAllMyClubImage(member);
        List<ClubMineImageDto> requestDTOs =  new ArrayList<>();
        for (ClubMineImageInterface c : requests) {
            requestDTOs.add(
                    new ClubMineImageDto(c.getClub_id(),
                            "https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/clubThumbnail/"+
                                    c.getThumbnail_url()));
        }
        return requestDTOs;
    }
    public List<ClubApprovalWaitingSimpleDto> getAllMyOwnClubDetail(Member member, int cursor) {
        List<ClubApprovalWaitingSimpleInterface> waitings = clubRepository.findAllHostClubDetail(member, cursor*7);
        List<ClubApprovalWaitingSimpleDto> waitingDTOs =  new ArrayList<>();
        for (ClubApprovalWaitingSimpleInterface c : waitings) {
            waitingDTOs.add(
                    new ClubApprovalWaitingSimpleDto(c.getClub_id(),c.getTitle(),c.getThumbnail_url(),c.getDate(),c.getLocation_keyword(),c.getStatus()));
        }
        getWaitingSimpleDto(waitingDTOs);

        return waitingDTOs;
    }
    public List<ClubApprovalWaitingSimpleDto> getWaitingsResponse(Member member, int cursor) {
        List<ClubApprovalWaitingSimpleInterface> waitings = clubRepository.findApprovalWaitingSimple(member, false, false,cursor*7);
        List<ClubApprovalWaitingSimpleDto> waitingDTOs =  new ArrayList<>();
        for (ClubApprovalWaitingSimpleInterface c : waitings) {
            waitingDTOs.add(
                    new ClubApprovalWaitingSimpleDto(c.getClub_id(),c.getTitle(),c.getThumbnail_url(),c.getDate(),c.getLocation_keyword(),c.getStatus()));
        }
        getWaitingSimpleDto(waitingDTOs);

        return waitingDTOs;
    }

    private void getWaitingSimpleDto(List<ClubApprovalWaitingSimpleDto> waitings) {
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
    }

    /**
     * 해당 모임에서 승인 대기중인 사용자들 정보 리턴
     */
    public List<ClubWaitingMemberDto> getClubWaitingPeople(Club club) {

        return clubMemberRepository.findClubMembersWaitingFrom(club);
    }
}
