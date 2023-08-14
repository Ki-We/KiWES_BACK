package server.api.kiwes.domain.search_count.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.search_count.entity.SearchCount;
import server.api.kiwes.domain.search_count.repository.SearchCountRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchCountScheduledService {
    private final SearchCountRepository searchCountRepository;

    /**
     * 매일 0시 3일 지난 검색기록 카운트 삭제
     */
    @Scheduled(cron = "0 0 0 * * ?") // 매일 0시
    public void removeOldSearchCountData(){
        for(SearchCount searchCount : searchCountRepository.findAllOrderByDate()){
            boolean isDifferenceMoreThan3Days = ChronoUnit.DAYS.between(searchCount.getDate(), LocalDate.now()) >= 3;

            if(isDifferenceMoreThan3Days){
                searchCountRepository.delete(searchCount);
            }else{
                break;
            }
        }
    }
}
