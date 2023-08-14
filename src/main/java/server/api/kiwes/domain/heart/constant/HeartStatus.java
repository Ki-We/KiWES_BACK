package server.api.kiwes.domain.heart.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HeartStatus {
    YES("YES"),
    NO("NO"),
    ;

    private final String status;
}
