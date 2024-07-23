package sws.songpin.domain.alarm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.alarm.entity.Alarm;
import sws.songpin.domain.member.entity.Member;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByReceiver(Member receiver);
    List<Alarm> findByAlarmIdAndReceiverIdGreaterThan(Long alarmId, Long receiverId);
    List<Alarm> findByReceiverOrderByCreatedAtDesc(Member receiver, Pageable pageable);
    Boolean existsByReceiverAndIsCheckedFalse(Member member);
}