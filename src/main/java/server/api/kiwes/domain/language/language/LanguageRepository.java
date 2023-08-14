package server.api.kiwes.domain.language.language;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.kiwes.domain.language.entity.Language;
import server.api.kiwes.domain.language.type.LanguageType;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language findByName(LanguageType type);
}
