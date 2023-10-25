package server.api.kiwes.domain.translate.dto;

import lombok.Getter;

@Getter
public class TranslateRequsetDto {
    String text;
    String source;
    String target;
}
