package server.api.kiwes.domain.heart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.entity.Heart;
import server.api.kiwes.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByClubAndMember(Club club, Member member);

    @Query(nativeQuery = true,
            value = "SELECT club_id FROM heart where status= 'YES' AND member_id = :memberId ORDER BY heart_id asc limit :cursor,7")
    List<Long> findAllHearted(@Param("memberId")Long memberId,@Param("cursor") int cursor );
}