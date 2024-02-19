package server.api.kiwes.domain.club_member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.club.dto.ClubMembersInfoDto;
import server.api.kiwes.domain.club.dto.ClubWaitingMemberDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club_member.entity.ClubMember;
import server.api.kiwes.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    Optional<ClubMember> findByClubAndMember(Club club, Member member);
    @Query("select distinct new server.api.kiwes.domain.club.dto.ClubMembersInfoDto(cm.member.id,cm.member.nickname,cm.member.profileImg) " +
            "from ClubMember cm " +
            "where cm.club = :club and cm.isHost = false")
    List<ClubMembersInfoDto> findAllMembersInClub(@Param("club")Club club);
    @Query("select distinct new server.api.kiwes.domain.club.dto.ClubWaitingMemberDto(cm.member.id, cm.member.nickname, cm.member.profileImg)" +
            " from ClubMember cm " +
            "where cm.club = :club and cm.isApproved = false")
    List<ClubWaitingMemberDto> findClubMembersWaitingFrom(@Param("club") Club club);
    @Query("select cm from ClubMember cm " +
            "where cm.club = :club and cm.isHost = true")
    Optional<ClubMember> findByClubHost(@Param("club") Club club);

    @Query("select cm from ClubMember cm " +
            "where cm.member = :member and cm.isHost = true")
    List<ClubMember> findByMemberHost(@Param("member") Member member);

    ClubMember findByClub(Club club);
}
