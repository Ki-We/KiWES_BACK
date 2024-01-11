package server.api.kiwes.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.kiwes.domain.member.entity.MemberDeleted;

public interface MemberDeletedRepository extends JpaRepository<MemberDeleted, Long> {
    boolean existsByEmail(String email);
}
