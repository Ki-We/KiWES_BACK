package server.api.kiwes.domain.club.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClubSortRequestDto {
    private List<String> sortedBy;
}
