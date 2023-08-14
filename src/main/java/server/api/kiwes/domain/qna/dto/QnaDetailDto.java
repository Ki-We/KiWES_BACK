package server.api.kiwes.domain.qna.dto;

import lombok.*;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.qna.entity.Qna;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaDetailDto {
    String questionerProfileImg;
    String questionerNickname;
    String questionContent;
    String qDate;

    String respondentProfileImg;
    String respondentNickname;
    String answerContent;
    String aDate;

    @Builder.Default
    Boolean isAuthorOfQuestion = false;

    @Builder.Default
    Boolean isAuthorOfAnswer = false;

    public static QnaDetailDto of(Qna qna, Member member){
        if(qna.getRespondent() == null){
            return QnaDetailDto.builder()
                    .questionerProfileImg(qna.getQuestioner().getProfileImg())
                    .questionerNickname(qna.getQuestioner().getNickname())
                    .questionContent(qna.getQuestionContent())
                    .qDate(qna.getQDate())
                    .isAuthorOfQuestion(qna.getQuestioner().getId().equals(member.getId()))
                    .build();
        }

        return QnaDetailDto.builder()
                .questionerProfileImg(qna.getQuestioner().getProfileImg())
                .questionerNickname(qna.getQuestioner().getNickname())
                .questionContent(qna.getQuestionContent())
                .qDate(qna.getQDate())
                .isAuthorOfQuestion(qna.getQuestioner().getId().equals(member.getId()))
                .respondentProfileImg(qna.getRespondent().getProfileImg())
                .respondentNickname(qna.getRespondent().getNickname())
                .answerContent(qna.getAnswerContent())
                .aDate(qna.getADate())
                .isAuthorOfAnswer(qna.getRespondent().getId().equals(member.getId()))
                .build();
    }
}
