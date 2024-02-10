package server.api.kiwes.domain.club.dto;

import lombok.*;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.qna.entity.Qna;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubArticleQnaDto {
    Long qnaId;
    Long questionerId;
    String questionerImageUrl;
    String questionerNickname;
    String questionContent;
    String questionDate;

    public static ClubArticleQnaDto of(Qna qna){
        Member questioner = qna.getQuestioner();
        return ClubArticleQnaDto.builder()
                .qnaId(qna.getId())
                .questionerId(questioner.getId())
                .questionerImageUrl("https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/profileimg/"+
                        questioner.getProfileImg()+".jpg")
                .questionerNickname(questioner.getNickname())
                .questionContent(qna.getQuestionContent())
                .questionDate(qna.getQDate())
                .build();
    }
}
