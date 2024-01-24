package server.api.kiwes.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.club.dto.ClubApprovalWaitingSimpleInterface;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.review.dto.ReviewMineSimpleInterface;
import server.api.kiwes.domain.review.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Boolean existsByClubAndReviewer(Club club, Member member);

    @Modifying
    @Query(nativeQuery = true, value = "delete from review where review_id = :id")
    void deleteById(@Param("id") Long id);

    List<Review> findByClub(Club club);

    @Query(nativeQuery = true,
            value = "select r.review_id,r.review_content,r.review_date, c.club_id, c.title " +
                    "from review r " +
                    "inner join club_member cm on cm.member_id = :member and r.reviewer_id = cm.member_id " +
                    "and r.club_id = cm.club_id " +
                    "left join club c on c.club_id = cm.club_id " +
                    "order by r.review_id desc")
    List<ReviewMineSimpleInterface> findAllMyReview(@Param("member") Member member);

    @Query(nativeQuery = true,
            value = "select r.review_id,r.review_content,r.review_date, c.club_id, c.title " +
                    "from review r " +
                    "inner join club_member cm on cm.member_id = :member and cm.is_host = true " +
                    "and r.club_id = cm.club_id left join club c on c.club_id = cm.club_id " +
                    "order by r.review_id desc")
    List<ReviewMineSimpleInterface> findAllHostReview(@Param("member") Member member);
}
