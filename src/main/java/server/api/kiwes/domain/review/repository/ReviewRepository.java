package server.api.kiwes.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.review.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Boolean existsByClubAndReviewer(Club club, Member member);

    @Modifying
    @Query(nativeQuery = true, value = "delete from review where review_id = :id")
    void deleteById(@Param("id") Long id);

    List<Review> findByClub(Club club);
}
