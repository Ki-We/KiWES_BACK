package server.api.kiwes.global.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("MALE","male"),
    FEMALE("FEMALE","female"),
    ALL("ALL","all"),
    NOTAGREE("NOTAGREE","notagree"),
    ;

    private final String name;
    private final String kakao;
}
