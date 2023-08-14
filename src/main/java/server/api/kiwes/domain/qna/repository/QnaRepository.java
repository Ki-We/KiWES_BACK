package server.api.kiwes.domain.qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.kiwes.domain.qna.entity.Qna;

public interface QnaRepository extends JpaRepository<Qna, Long> {
}
