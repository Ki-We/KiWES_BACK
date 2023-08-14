package server.api.kiwes.domain.heart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.heart.entity.Heart;
import server.api.kiwes.domain.member.entity.Member;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByClubAndMember(Club club, Member member);
}
