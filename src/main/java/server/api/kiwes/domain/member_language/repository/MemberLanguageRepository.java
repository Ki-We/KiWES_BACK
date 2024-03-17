package server.api.kiwes.domain.member_language.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.member.entity.Member;
import server.api.kiwes.domain.member_language.entity.MemberLanguage;

import java.util.List;

public interface MemberLanguageRepository extends JpaRepository<MemberLanguage, Long>{
    @Query("SELECT ml.language.id FROM MemberLanguage ml WHERE ml.member.id = :memberId")
    List<Long> findLanguageIdsByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM MemberLanguage ml WHERE ml.member.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
