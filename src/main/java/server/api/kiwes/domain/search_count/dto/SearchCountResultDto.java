package server.api.kiwes.domain.search_count.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCountResultDto {
    private String keyword;
    private Long count;
}
