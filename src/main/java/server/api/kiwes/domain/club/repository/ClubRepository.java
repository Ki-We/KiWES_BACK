package server.api.kiwes.domain.club.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.club.constant.ClubStatus;
import server.api.kiwes.domain.club.dto.*;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.member.entity.Member;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query(nativeQuery = true,
            value = "select c.club_id, c.title, (SELECT COUNT(*)  FROM club_member cm2 " +
                    "WHERE cm2.club_id = c.club_id AND cm2.is_approved = false) AS approval_count " +
                    "from club c " +
                    "inner join club_member cm " +
                    "on c.club_id = cm.club_id and cm.member_id = :member and cm.is_host = true " +
                    "order by c.club_id asc limit :cursor,7")
    List<ClubApprovalRequestSimpleInterface> findApprovalRequestSimple(@Param("member") Member member,
                                                                 @Param("cursor") int cursor);
    @Query(nativeQuery = true,
            value = "select c.club_id, c.title, c.current_people " +
                    "from club c " +
                    "inner join club_member cm " +
                    "on c.club_id = cm.club_id and cm.member_id = :member and cm.is_approved = true " +
                    "order by c.club_id")
    List<MyClubSimpleInterface> findAllMyClub(@Param("member") Member member);
    @Query(nativeQuery = true,
            value = "select c.club_id, c.thumbnail_url " +
                    "from club c " +
                    "inner join club_member cm " +
                    "on c.club_id = cm.club_id and cm.member_id = :member and cm.is_approved = true " +
                    "left join heart h on h.member_id = :member and h.club_id = c.club_id "+
                    "order by c.club_id asc")
    List<ClubMineImageInterface> findAllMyClubImage(@Param("member") Member member);

    @Query(nativeQuery = true,
            value = "select c.club_id, c.title, c.thumbnail_url, c.date, c.Location_keyword, h.status " +
                    "from club c " +
                    "inner join club_member cm " +
                    "on c.club_id = cm.club_id and cm.member_id = :member and cm.is_host=true " +
                    "left join heart h on h.member_id = :member and h.club_id = c.club_id "+
                    "order by c.club_id asc limit :cursor,7")
    List<ClubApprovalWaitingSimpleInterface> findAllHostClubDetail(@Param("member") Member member,
                                                                 @Param("cursor") int cursor);
    @Query(nativeQuery = true,
            value = "select c.club_id, c.title, (SELECT COUNT(*)  FROM club_member cm2 " +
                    "WHERE cm2.club_id = c.club_id AND cm2.is_approved = false) AS approval_count " +
                    "from club c " +
                    "inner join club_member cm " +
                    "on c.club_id = cm.club_id and cm.member_id = :member and cm.is_host = true " +
                    "order by c.club_id asc limit 2")
    List<ClubApprovalRequestSimpleInterface> findApprovalRequestSimpleLimit2(@Param("member") Member member);
    @Query(nativeQuery = true,
            value = "select c.club_id, c.title, c.thumbnail_url, c.date, c.Location_keyword, h.status " +
                    "from club c inner join club_member cm on c.club_id = cm.club_id and cm.member_id = :member and cm.is_host = :isHost and cm.is_approved = :isApproved " +
                    "left join heart h on h.member_id = :member and h.club_id = c.club_id order by c.club_id asc limit 2")
    List<ClubApprovalWaitingSimpleInterface> findApprovalWaitingSimplelimit2(@Param("member") Member member,
                                                                             @Param("isHost") Boolean isHost,
                                                                             @Param("isApproved") Boolean isApproved);
    @Query(nativeQuery = true,
            value = "select c.club_id, c.title, c.thumbnail_url, c.date, c.Location_keyword, h.status " +
                    "from club c inner join club_member cm on c.club_id = cm.club_id and cm.member_id = :member and cm.is_host = :isHost and cm.is_approved = :isApproved " +
                    "left join heart h on h.member_id = :member and h.club_id = c.club_id order by c.club_id asc limit :cursor,7")
    List<ClubApprovalWaitingSimpleInterface> findApprovalWaitingSimple(@Param("member") Member member,
                                                                 @Param("isHost") Boolean isHost,
                                                                 @Param("isApproved") Boolean isApproved,
                                                                 @Param("cursor") int cursor);

    @Query("select c from Club c where c.isActivated = :status order by c.dueTo ")
    List<Club> findActivatedClubsOrderByDueTo(@Param("status") ClubStatus status);

    @Query("SELECT c FROM Club c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Club> findByTitleOrContentContaining(@Param("keyword") String keyword);
    @Query(nativeQuery = true,
    value = "select * from club c where club_id >0 AND c.due_to > CURDATE() order by c.heart_cnt desc limit 5")
    List<Club> findAllOrderByHeartCnt();

    @Query(nativeQuery = true,
            value = "select * from club c where club_id >0 AND c.due_to > CURDATE() order by club_id desc,RAND() limit 3")
    List<Club> findOrderByHeartCntRandom();

    @Query(nativeQuery = true,
            value = "select * from club c left join club_language cl on c.club_id = cl.club_id " +
                    "where c.club_id >0 AND c.due_to > CURDATE() AND cl.language_id IN (:languageIds) order by c.club_id desc,RAND() limit 5")
    List<Club> findOrderByLanguages(@Param("languageIds") List<Long> languageIds);



    @Query(nativeQuery = true,
            value = "SELECT * FROM club where club_id > 0 ORDER BY club_id DESC LIMIT :cursor,7")
    List<Club> findAllbyCursor(@Param("cursor") int cursor);


    @Modifying
    @Query("update Club c set c.heartCnt = c.heartCnt + 1 where c.id = :id")
    int increaseHeartCnt(@Param("id") Long id);

    @Modifying
    @Query("update Club c set c.heartCnt = c.heartCnt - 1 where c.id = :id")
    int decreaseHeartCnt(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Club c SET c.isActivated = 'NO' WHERE c.id IN :clubIds")
    void setUnActiveClubs(@Param("clubIds") List<Long> clubIds);
}
