package server.api.kiwes.domain.member_language.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.kiwes.domain.member_language.entity.MemberLanguage;

public interface MemberLanguageRepository extends JpaRepository<MemberLanguage, Long>{
}
