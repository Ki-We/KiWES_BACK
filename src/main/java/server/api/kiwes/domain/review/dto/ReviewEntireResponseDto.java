package server.api.kiwes.domain.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntireResponseDto {
    Boolean isHost;
    Long userId;
    List<ReviewDetailDto> reviews;
}
