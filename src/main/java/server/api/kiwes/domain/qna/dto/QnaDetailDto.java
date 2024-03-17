package server.api.kiwes.domain.qna.dto;

import lombok.*;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.qna.constant.QnaAnsweredStatus;
import server.api.kiwes.domain.qna.constant.QnaDeletedStatus;
import server.api.kiwes.domain.qna.entity.Qna;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaDetailDto {
    Long qnaId;
    Long questionerId;
    String questionerProfileImg;
    String questionerNickname;
    String questionContent;
    String qDate;

    Long respondentId;
    String respondentProfileImg;
    String respondentNickname;
    String answerContent;
    String aDate;

    @Builder.Default
    Boolean isAuthorOfQuestion = false;

    @Builder.Default
    Boolean isAuthorOfAnswer = false;

    Boolean isModified;
    QnaDeletedStatus isDeleted;
    QnaAnsweredStatus isAnswered;

    public static QnaDetailDto of(Qna qna, Member member){
        if(qna.getRespondent() == null){
            return QnaDetailDto.builder()
                    .qnaId(qna.getId())
                    .questionerId(qna.getQuestioner().getId())
                    .questionerProfileImg("https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/profileimg/"+
                            qna.getQuestioner().getProfileImg()+".jpg")
                    .questionerNickname(qna.getQuestioner().getNickname())
                    .questionContent(qna.getQuestionContent())
                    .qDate(qna.getQDate())
                    .isDeleted(qna.getIsDeleted())
                    .isAnswered(qna.getIsAnswered())
                    .isAuthorOfQuestion(qna.getQuestioner().getId().equals(member.getId()))
                    .isModified(qna.getIsModified())
                    .build();
        }

        return QnaDetailDto.builder()
                .qnaId(qna.getId())
                .questionerId(qna.getQuestioner().getId())
                .questionerProfileImg("https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/profileimg/"+
                        qna.getQuestioner().getProfileImg()+".jpg")
                .questionerNickname(qna.getQuestioner().getNickname())
                .questionContent(qna.getQuestionContent())
                .qDate(qna.getQDate())
                .isDeleted(qna.getIsDeleted())
                .isAuthorOfQuestion(qna.getQuestioner().getId().equals(member.getId()))
                .respondentProfileImg("https://kiwes2-bucket.s3.ap-northeast-2.amazonaws.com/profileimg/"+
                        qna.getRespondent().getProfileImg()+".jpg")
                .respondentNickname(qna.getRespondent().getNickname())
                .answerContent(qna.getAnswerContent())
                .isAnswered(qna.getIsAnswered())
                .aDate(qna.getADate())
                .isAuthorOfAnswer(qna.getRespondent().getId().equals(member.getId()))
                .respondentId(qna.getRespondent().getId())
                .isModified(qna.getIsModified())
                .build();
    }
}
