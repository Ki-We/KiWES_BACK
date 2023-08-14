package server.api.kiwes.domain.qna.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QnaAnsweredStatus {
    YES("YES"),
    NO("NO"),
    ;

    private final String status;
}
