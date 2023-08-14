package server.api.kiwes.domain.search_count.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import server.api.kiwes.domain.search_count.dto.SearchCountResultDto;
import server.api.kiwes.domain.search_count.entity.SearchCount;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SearchCountRepository extends JpaRepository<SearchCount, Long> {
    Optional<SearchCount> findBySearchWordIgnoreCaseAndDate(String keyword, LocalDate date);

    @Query("select sc from SearchCount sc order by sc.date ")
    List<SearchCount> findAllOrderByDate();

    @Query("select new server.api.kiwes.domain.search_count.dto.SearchCountResultDto(sc.searchWord, sum(sc.count)) " +
            "from SearchCount sc " +
            "group by sc.searchWord " +
            "order by sum(sc.count) desc")
    List<SearchCountResultDto> findAllGroupByKeyword();

}
