package server.api.kiwes.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.api.kiwes.domain.alarm.dto.AlarmResponseDto;
import server.api.kiwes.domain.alarm.entity.Alarm;
import server.api.kiwes.domain.club.constant.ClubStatus;
import server.api.kiwes.domain.club.dto.ClubApprovalRequestSimpleDto;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query (
            value = "select * from alarm where alarm.member_id = :memberId or ( type = 'NOTICE' or type = 'EVENT' )",
            nativeQuery = true
    )
    List<Alarm> findByMemberIdAndType(@Param("memberId") Long memberId);
}
