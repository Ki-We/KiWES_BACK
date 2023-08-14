package server.api.kiwes.domain.member_language.entity;

import lombok.*;
import server.api.kiwes.domain.language.entity.Language;
import server.api.kiwes.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberLanguage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_LANGUAGE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "LANGAUGE_ID")
    private Language language;

    public MemberLanguage(Member member, Language language) {
        this.member = member;
        this.language = language;
    }
}
