package server.api.kiwes.domain.club.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.api.kiwes.domain.club.constant.ClubStatus;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club.repository.ClubRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubScheduledService {
    private final ClubRepository clubRepository;

    /**
     * 매일 0시 모든 클럽을 스캔하여 마감기한이 지난 클럽은 비활성화
     */
    @Scheduled(cron = "0 0 0 * * ?") // 매일 0시
    public void deactivateClubAfterDueTo(){
        List<Club> allClubs = clubRepository.findActivatedClubsOrderByDueTo(ClubStatus.YES);
        for(Club club : allClubs){
            if(LocalDate.now().compareTo(convertToLocalDate(club.getDueTo())) <= 0) {
                return;
            }

            club.setIsActivated(ClubStatus.NO);
        }
    }

    private LocalDate convertToLocalDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
