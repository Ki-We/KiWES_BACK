package server.api.kiwes.domain.club_language.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.club.dto.ClubSortResponseDto;
import server.api.kiwes.domain.club_language.entity.ClubLanguage;

import java.util.List;

public interface ClubLanguageRepository extends JpaRepository<ClubLanguage, Long> {

    @Query("select cl from ClubLanguage cl " +
            "where cl.club.id = :clubId")
    List<ClubLanguage> findByClubId(@Param("clubId")Long clubId);

    @Query("select distinct new server.api.kiwes.domain.club.dto.ClubSortResponseDto(c.club.id, c.club.title, c.club.thumbnailUrl, c.club.date, c.club.location) " +
            "from ClubLanguage c where c.language.id IN :languageIds")
    List<ClubSortResponseDto> findAllByCategoryIds(@Param("languageIds") List<Long> languageIds);

}
