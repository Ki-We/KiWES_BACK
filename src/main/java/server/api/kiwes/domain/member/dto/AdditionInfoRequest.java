package server.api.kiwes.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdditionInfoRequest {

    private String accessToken;

    private String nickName;
    private String birth;
    private String introduction;
    private String nationality;
    private List<String> languages;
    private List<String> categories;


}
