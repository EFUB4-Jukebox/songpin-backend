package sws.songpin.domain.alarm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.alarm.entity.Alarm;
import sws.songpin.domain.member.entity.Member;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Slice<Alarm> findByReceiverOrderByCreatedTimeDesc(Member receiver, Pageable pageable);
    Boolean existsByReceiverAndIsReadFalse(Member member);
}