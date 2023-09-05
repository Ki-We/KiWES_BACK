package server.api.kiwes.domain.club_member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club_member.entity.ClubMember;
import server.api.kiwes.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    Optional<ClubMember> findByClubAndMember(Club club, Member member);

    @Query("select cm.member from ClubMember cm " +
            "where cm.club = :club and cm.isApproved = :isApproved")
    List<Member> findClubMembersWaitingFrom(@Param("club") Club club, @Param("isApproved") Boolean isApproved);
    @Query("select cm from ClubMember cm " +
            "where cm.club = :club and cm.isHost = true")
    Optional<ClubMember> findByClubHost(@Param("club") Club club);

}
