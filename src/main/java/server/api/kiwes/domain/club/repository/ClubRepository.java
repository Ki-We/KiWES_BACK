package server.api.kiwes.domain.club.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.club.constant.ClubStatus;
import server.api.kiwes.domain.club.dto.ClubApprovalRequestSimpleDto;
import server.api.kiwes.domain.club.dto.ClubApprovalRequestSimpleInterface;
import server.api.kiwes.domain.club.dto.ClubApprovalWaitingSimpleDto;
import server.api.kiwes.domain.club.dto.ClubApprovalWaitingSimpleInterface;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.member.entity.Member;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query(nativeQuery = true,
            value = "select c.club_id, c.title, c.current_people " +
                    "from club c " +
                    "inner join club_member cm " +
                    "on c.club_id = cm.club_id and cm.member_id = :member and cm.is_host = :isHost " +
                    "order by c.club_id asc limit :cursor,7")
    List<ClubApprovalRequestSimpleInterface> findApprovalRequestSimple(@Param("member") Member member,
                                                                 @Param("isHost") Boolean isHost,
                                                                 @Param("cursor") int cursor);
    @Query(nativeQuery = true,
            value = "select c.club_id, c.title, c.current_people " +
                    "from club c " +
                    "inner join club_member cm " +
                    "on c.club_id = cm.club_id and cm.member_id = :member and cm.is_host = :isHost " +
                    "order by c.club_id asc limit 2")
    List<ClubApprovalRequestSimpleInterface> findApprovalRequestSimpleLimit2(@Param("member") Member member,
                                                                             @Param("isHost") Boolean isHost);
    @Query(nativeQuery = true,
            value = "select c.club_id, c.title, c.thumbnail_url, c.date, c.locations_keyword, h.status " +
                    "from club c inner join club_member cm on c.club_id = cm.club_id and cm.member_id = :member and cm.is_host = :isHost and cm.is_approved = :isApproved " +
                    "left join heart h on h.member_id = :member and h.club_id = c.club_id order by c.club_id asc limit 2")
    List<ClubApprovalWaitingSimpleInterface> findApprovalWaitingSimplelimit2(@Param("member") Member member,
                                                                             @Param("isHost") Boolean isHost,
                                                                             @Param("isApproved") Boolean isApproved);
    @Query(nativeQuery = true,
            value = "select c.club_id, c.title, c.thumbnail_url, c.date, c.locations_keyword, h.status " +
                    "from club c inner join club_member cm on c.club_id = cm.club_id and cm.member_id = :member and cm.is_host = :isHost and cm.is_approved = :isApproved " +
                    "left join heart h on h.member_id = :member and h.club_id = c.club_id order by c.club_id asc limit :cursor,7")
    List<ClubApprovalWaitingSimpleInterface> findApprovalWaitingSimple(@Param("member") Member member,
                                                                 @Param("isHost") Boolean isHost,
                                                                 @Param("isApproved") Boolean isApproved,
                                                                 @Param("cursor") int cursor);

    @Query("select c from Club c where c.isActivated = :status order by c.dueTo ")
    List<Club> findActivatedClubsOrderByDueTo(@Param("status") ClubStatus status);
    @Query( nativeQuery = true,
            value = "SELECT * FROM club where club_id > 0  AND content LIKE CONCAT('%',:keyword,'%') ORDER BY club_id DESC limit :cursor,7")
    List<Club> findByTitlePage(@Param("keyword") String keyword, @Param("cursor")int cursor);
    @Query(nativeQuery = true,
    value = "select * from club c where club_id >0 order by c.heart_cnt desc limit 5")
    List<Club> findAllOrderByHeartCnt();

    @Query(nativeQuery = true,
            value = "select * from club c where club_id >0 order by c.heart_cnt desc,RAND() limit 3")
    List<Club> findOrderByHeartCntRandom();
    @Query(nativeQuery = true,
            value = "SELECT * FROM club where club_id > 0 ORDER BY club_id DESC LIMIT :cursor,7")
    List<Club> findAllbyCursor(@Param("cursor") int cursor);
}
