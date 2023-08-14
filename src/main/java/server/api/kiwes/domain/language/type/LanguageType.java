package server.api.kiwes.domain.language.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LanguageType {
    KO("KO"),
    EN("EN"),
    JP("JP"),
    CH1("CH1"),
    CH2("CH2"),
    FR("FR"),
    ES("ES"),
    DE("DE"),
    RU("RU"),
    VN("VN"),
    OTHER("OTHER"),
    ;

    public final String name;
}
