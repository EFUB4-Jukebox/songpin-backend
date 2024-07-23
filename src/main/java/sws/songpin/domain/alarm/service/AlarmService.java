package sws.songpin.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.alarm.dto.AlarmListResponseDto;
import sws.songpin.domain.alarm.entity.Alarm;
import sws.songpin.domain.alarm.entity.AlarmType;
import sws.songpin.domain.alarm.repository.AlarmRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final EmitterService emitterService;
    private final MemberService memberService;

    // 알람 생성 (message, sender는 필수 요소 x)
    public void createAlarm(AlarmType alarmType, String message, Member sender, Member receiver) {
        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .message(message) // nullable
                .sender(sender) // nullable
                .receiver(receiver)
                .isRead(false)
                .build();
        alarmRepository.save(alarm);
        emitterService.notify(sender.getMemberId(), null, "new follow alarm");
    }

    // 최근 알림 목록 읽어오기
    public AlarmListResponseDto getAlarmList(){
        List<Alarm> alarmList = getAndReadAlarms();
        return AlarmListResponseDto.from(alarmList);
    }

    private List<Alarm> getAndReadAlarms() {
        Member member = memberService.getCurrentMember();
        Pageable pageable = PageRequest.of(0, 30);
        List<Alarm> alarmList = alarmRepository.findByReceiverOrderByCreatedAtDesc(member, pageable);
        for (Alarm alarm : alarmList) {
            alarm.readAlarm();
        }
        return alarmRepository.saveAll(alarmList);
    }
}
