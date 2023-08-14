package server.api.kiwes.domain.club.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ClubStatus {
    YES("YES"),
    NO("NO");

    private final String status;
}
