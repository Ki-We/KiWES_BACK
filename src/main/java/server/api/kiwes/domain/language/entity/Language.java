package server.api.kiwes.domain.language.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.api.kiwes.domain.club_language.entity.ClubLanguage;
import server.api.kiwes.domain.language.type.LanguageType;
import server.api.kiwes.domain.member_language.entity.MemberLanguage;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Language {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "LANGUAGE_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private LanguageType name;

    @OneToMany(mappedBy = "language")
    private List<ClubLanguage> clubLanguages;

    @OneToMany(mappedBy = "language")
    private List<MemberLanguage> memberLanguages;
}
