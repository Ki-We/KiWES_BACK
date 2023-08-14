package server.api.kiwes.domain.club_category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.category.entity.Category;
import server.api.kiwes.domain.club.dto.ClubSortResponseDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club_category.entity.ClubCategory;
import server.api.kiwes.domain.heart.constant.HeartStatus;

import java.util.List;

public interface ClubCategoryRepository extends JpaRepository<ClubCategory, Long> {


    @Query("select distinct new server.api.kiwes.domain.club.dto.ClubSortResponseDto(c.club.id, c.club.title, c.club.thumbnailUrl, c.club.date, c.club.location) " +
            "from ClubCategory c where c.category.id IN :categoryIds")
    List<ClubSortResponseDto> findAllByCategoryIds(@Param("categoryIds") List<Long> categoryIds);


}


