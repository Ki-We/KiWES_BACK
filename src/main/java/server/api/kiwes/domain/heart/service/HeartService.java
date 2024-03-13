package server.api.kiwes.domain.heart.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.club.dto.ClubSortResponseDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.repository.ClubRepository;
import server.api.kiwes.domain.heart.constant.HeartStatus;
import server.api.kiwes.domain.heart.dto.HeartSortResponseDto;
import server.api.kiwes.domain.heart.entity.Heart;
import server.api.kiwes.domain.heart.repository.HeartRepository;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.global.security.util.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final ClubRepository clubRepository;
    /**
     * 클럽, 멤버를 가지고 heart 여부 리턴
     */
    public Boolean getHearted(Club club, Member member){
        Heart heart = heartRepository.findByClubAndMember(club, member)
                .orElse(null);

        if (heart == null) return false;
        return !heart.getStatus().equals(HeartStatus.NO);
    }

    /**
     * 모임 찜하기
     */
    public void heart(Member member, Club club) {
        Heart heart = heartRepository.findByClubAndMember(club, member)
                .orElse(null);

        if (heart == null){
            Heart newHeart = Heart.builder()
                    .club(club)
                    .member(member)
                    .build();

            heartRepository.save(newHeart);
            return;
        }
        heart.setStatus(HeartStatus.YES);
        clubRepository.increaseHeartCnt(club.getId());
    }

    /**
     * 모임 찜하기 취소
     */
    public void unheart(Member member, Club club) {
        Heart heart = heartRepository.findByClubAndMember(club, member)
                .orElse(null);

        if(heart == null) return;

        heart.setStatus(HeartStatus.NO);
        if(club.getHeartCnt()>0){
            clubRepository.decreaseHeartCnt(club.getId());
        }
    }

    public List<HeartSortResponseDto> getHeartedAll(int cursor) {
        Long memberId = SecurityUtils.getLoggedInUser().getId();
        List<Long> clubList = heartRepository.findAllHearted(memberId,cursor*7);

        List<HeartSortResponseDto> heartListDTO =  new ArrayList<>();
        for (Long club_id : clubList) {
            Club club = clubRepository.findById(club_id).get();
            HeartSortResponseDto heartSortResponseDto = HeartSortResponseDto.builder()
                    .clubId(club.getId())
                    .title(club.getTitle())
                    .thumbnailImage("https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/clubThumbnail/"+
                            club.getThumbnailUrl())
                    .locationKeyword(club.getLocationKeyword())
                    .date(club.getDueTo())
                    .build();
            heartSortResponseDto.setLanguages(club.getLanguages());
            heartListDTO.add(heartSortResponseDto);
        }
        return heartListDTO;
    }
}