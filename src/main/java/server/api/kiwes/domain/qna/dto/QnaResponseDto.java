package server.api.kiwes.domain.qna.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaResponseDto {
    Boolean isHost; // 클라이언트에서 '답글 달기'에 쓰라고 주는 값\
    List<QnaDetailDto> qnas;
}
