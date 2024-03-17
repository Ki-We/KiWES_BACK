package server.api.kiwes.domain.club_language.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.club.dto.ClubSortInterface;
import server.api.kiwes.domain.club.dto.ClubSortResponseDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club_language.entity.ClubLanguage;

import java.util.List;

public interface ClubLanguageRepository extends JpaRepository<ClubLanguage, Long> {

    @Query("select cl from ClubLanguage cl " +
            "where cl.club.id = :clubId")
    List<ClubLanguage> findByClubId(@Param("clubId")Long clubId);

    @Query(nativeQuery = true,
            value = "SELECT distinct c.club_id, c.title, c.thumbnail_url, c.date, c.location , c.latitude, c.longitude, h.status " +
                    "FROM club_language cl " +
                    "LEFT OUTER JOIN heart h ON h.club_id = cl.club_id AND h.member_id = :memberId " +
                    "INNER JOIN club c ON c.club_id = cl.club_id " +
                    "WHERE cl.language_id IN (:languageIds) " +
                    "ORDER BY cl.club_id DESC LIMIT :cursor,7")
    List<ClubSortInterface> findAllByTypeIds(@Param("languageIds") List<Long> languageIds,
                                             @Param("memberId") Long memberId, @Param("cursor") int cursor);

    void deleteAllByClubId(Long id);
}
