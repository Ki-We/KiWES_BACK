package server.api.kiwes.domain.search_count.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SearchCount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEARCH_COUNT_ID")
    private Long id;

    private LocalDate date;
    private String searchWord;

    @Builder.Default
    private Integer count = 1;

    public void addCount(){
        this.count++;
    }

    // Scheduler를 통해 일정 기간이 지난 이후에는 데이터를 삭제하도록 하기
}
