package server.api.kiwes.domain.member.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "토큰 재발급을 위한 요청 객체")
public class RefreshTokenRequest {


    @ApiModelProperty(notes = "리프레시 토큰을 입력해주세요.")
    private String refreshToken;

    @ApiModelProperty(notes = "유저 id를 입력해 주세요.")
    private Long id;
}
