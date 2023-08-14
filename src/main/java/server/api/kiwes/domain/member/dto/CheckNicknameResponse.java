package server.api.kiwes.domain.member.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "닉네임 중복 검사를 위한 응답 객체")
public class CheckNicknameResponse {
    private String result;
}