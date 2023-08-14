package server.api.kiwes.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Nationality {
    KOREA("KOREA"),
    FOREIGN("FOREIGN"),
    ;

    private final String name;
}
