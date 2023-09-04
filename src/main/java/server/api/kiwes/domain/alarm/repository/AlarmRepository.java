package server.api.kiwes.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.api.kiwes.domain.alarm.entity.Alarm;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.member.entity.Member;

import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Optional<Alarm> findByClubAndMember(Club club, Member member);
}
