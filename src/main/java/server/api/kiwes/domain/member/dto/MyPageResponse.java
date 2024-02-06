package server.api.kiwes.domain.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "마이페이지 조회 응답 객체")
public class MyPageResponse {
    // 프로필 사진, 닉네임, 국적, 나이, 성별, 소개
    @ApiModelProperty(notes = "id")
    private Long id;
    @ApiModelProperty(notes = "프로필 이미지")
    private String profileImage;
    @ApiModelProperty(notes = "닉네임")
    private String nickname;
    @ApiModelProperty(notes = "국적")
    private String Nationality;
    @ApiModelProperty(notes = "나이")
    private int age;
    @ApiModelProperty(notes = "성별")
    private String gender;
    @ApiModelProperty(notes = "자기소개")
    private String introduction;


}
