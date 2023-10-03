package server.api.kiwes.domain.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.club.constant.ClubStatus;
import server.api.kiwes.domain.club.dto.ClubApprovalRequestSimpleDto;
import server.api.kiwes.domain.club.dto.ClubApprovalWaitingSimpleDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.member.entity.Member;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {

    @Query("select new server.api.kiwes.domain.club.dto.ClubApprovalRequestSimpleDto(c.id, c.title, c.currentPeople) " +
            "from Club c " +
            "inner join ClubMember cm " +
            "on cm.club = c " +
            "where cm.member = :member and cm.isHost = :isHost " +
            "order by function('RAND') ")
    List<ClubApprovalRequestSimpleDto> findApprovalRequestSimple(@Param("member") Member member, @Param("isHost") Boolean isHost);

    @Query("select new server.api.kiwes.domain.club.dto.ClubApprovalWaitingSimpleDto(c.id, c.title, c.thumbnailUrl, c.date, c.locationsKeyword, h.status) " +
            "from Club c " +
            "inner join ClubMember cm " +
            "on c.id = cm.club.id and cm.member = :member and cm.isHost = :isHost and cm.isApproved = :isApproved " +
            "left join Heart h " +
            "on h.member = :member and h.club = c ")
    List<ClubApprovalWaitingSimpleDto> findApprovalWaitingSimple(@Param("member") Member member, @Param("isHost") Boolean isHost, @Param("isApproved") Boolean isApproved);

    @Query("select c from Club c where c.isActivated = :status order by c.dueTo ")
    List<Club> findActivatedClubsOrderByDueTo(@Param("status") ClubStatus status);

    List<Club> findByTitleContaining(String keyword);

    @Query(nativeQuery = true,
    value = "select * from club c where club_id >0 order by c.heart_cnt desc limit 5")
    List<Club> findAllOrderByHeartCnt();

    @Query(nativeQuery = true,
            value = "select * from club c where club_id >0 order by c.heart_cnt desc,RAND() limit 3")
    List<Club> findOrderByHeartCntRandom();
    @Query(nativeQuery = true,
            value = "SELECT * FROM club c where club_id > 0 ORDER BY c.id DESC LIMIT :cursor,7")
    List<Club> findAllbyCursor(@Param("cursor") int cursor);
}
