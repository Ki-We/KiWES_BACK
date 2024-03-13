package server.api.kiwes.domain.club_category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.category.entity.Category;
import server.api.kiwes.domain.club.dto.ClubSortInterface;
import server.api.kiwes.domain.club.dto.ClubSortResponseDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club_category.entity.ClubCategory;
import server.api.kiwes.domain.heart.constant.HeartStatus;

import java.util.List;

public interface ClubCategoryRepository extends JpaRepository<ClubCategory, Long> {


    @Query(nativeQuery = true,
            value = "SELECT distinct c.club_id, c.title, c.thumbnail_url, c.date, c.location, c.latitude, c.longitude " +
                    "FROM club_category cc " +
                    "INNER JOIN club c ON cc.club_id = c.club_id " +
                    "WHERE cc.category_id IN (:categoryIds) " +
                    "ORDER BY c.club_id DESC LIMIT :cursor,7")
    List<ClubSortInterface> findAllByTypeIds(@Param("categoryIds") List<Long> categoryIds
            , @Param("cursor") int cursor);

    ClubCategory findByClubId(Long id);
}


