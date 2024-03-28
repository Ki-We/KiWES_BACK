package server.api.kiwes.domain.club_member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.club.constant.ClubResponseType;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.repository.ClubRepository;
import server.api.kiwes.domain.club_member.entity.ClubMember;
import server.api.kiwes.domain.club_member.repository.ClubMemberRepository;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.response.BizException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ClubMemberService {
    private final ClubMemberRepository clubMemberRepository;
    private final ClubRepository clubRepository;
    /**
     * 모임과 멤버를 통해 ClubMember 객체 반환. 없으면 null 반환
     */
    public ClubMember findByClubAndMember(Club club, Member member){
        return clubMemberRepository.findByClubAndMember(club, member)
                .orElse(ClubMember.builder().member(null).isHost(false).build());
    }

    /**
     * 모임과 멤버 정보로, 해당 사용자가 호스트인지 여부를 반환
     */
    public Boolean getIsHost(Club club, Member member){
        ClubMember clubMember = findByClubAndMember(club, member);
        if (clubMember == null) return false;

        return clubMember.getIsHost();
    }

    /**
     * 모임의 정보로, 해당 모임의 호스트 ClubMember 객체 반환
     */
    public ClubMember findByClubHost(Club club) {
        ClubMember clubMember = clubMemberRepository.findByClubHost(club).orElse(null);
        return clubMember;
    }

    /**
     * 모임과 멤버 정보로, 해당 사용자가 모임에 승인된 멤버인지 여부를 반환
     */
    public Boolean getIsApproved(Club club, Member member){
        ClubMember clubMember = findByClubAndMember(club, member);
        if(clubMember == null) return false;

        return clubMember.getIsApproved();
    }

    public void quit(List<ClubMember> clubMemberList) {
        List<Long> clubIds = clubMemberList.stream()
                .map(clubMember -> clubMember.getClub().getId())
                .distinct()
                .collect(Collectors.toList());
        clubRepository.setUnActiveClubs(clubIds);
    }

    public void cancelApplication(Long clubId, Member member) {
        Club club = clubRepository.findById(clubId).get();
        ClubMember clubMember = this.findByClubAndMember(club, member);
        if(clubMember.getMember() == null){
            throw new BizException(ClubResponseType.NOT_APPLIED);
        }

        if(clubMember.getIsHost()){
            throw new BizException(ClubResponseType.HOST_CANNOT_CANCEL);
        }
        if(clubMember.getIsApproved()){
            club.subCurrentPeople();
        }

        clubMemberRepository.delete(clubMember);
    }
}
