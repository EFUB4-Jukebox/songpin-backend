package sws.songpin.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.alarm.dto.response.AlarmUnitDto;
import sws.songpin.domain.alarm.dto.ssedata.AlarmDefaultDataDto;
import sws.songpin.domain.alarm.dto.response.AlarmListResponseDto;
import sws.songpin.domain.alarm.entity.Alarm;
import sws.songpin.domain.alarm.entity.AlarmType;
import sws.songpin.domain.alarm.repository.AlarmRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final EmitterService emitterService;
    private final MemberService memberService;

    // 최근 알림 목록 읽어오기
    public AlarmListResponseDto getRecentAlarms(){
        Pageable pageable = PageRequest.of(0, 30);
        List<AlarmUnitDto> alarmUnitDtos = getAndReadAlarms(pageable);
        return AlarmListResponseDto.fromAlarmUnitDto(alarmUnitDtos);
    }

    // 일반 알림 생성 (message, sender는 필수 요소 x)
    public void createAlarm(AlarmType alarmType, String message, Member sender, Member receiver, Object data) {
        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .message(message) // nullable
                .sender(sender) // nullable
                .receiver(receiver)
                .isRead(false)
                .build();
        alarmRepository.save(alarm);
        emitterService.notify(sender.getMemberId(), data, "new alarm");
    }

    // 팔로우 알림 생성
    public void createFollowAlarm(Member follower, Member following) {
        String alarmMessage = MessageFormat.format(AlarmType.FOLLOW.getMessagePattern(), follower.getNickname(), follower.getHandle());
        createAlarm(AlarmType.FOLLOW, alarmMessage, follower, following, AlarmDefaultDataDto.from(true));
    }

    public void deleteAllAlarmsOfMember(Member member){
        alarmRepository.deleteAllBySender(member);
        alarmRepository.deleteAllByReceiver(member);
    }

    private List<AlarmUnitDto> getAndReadAlarms(Pageable pageable) {
        List<AlarmUnitDto> alarmList = new ArrayList<>();
        Member currentMember = memberService.getCurrentMember();
        Slice<Alarm> alarmSlice = alarmRepository.findByReceiverOrderByCreatedTimeDesc(currentMember, pageable);
        if (alarmSlice != null && alarmSlice.hasContent()) {
            for (Alarm alarm : alarmSlice) {
                alarmList.add(AlarmUnitDto.from(alarm));
                alarm.readAlarm();
            }
            alarmRepository.saveAll(alarmSlice);
        }
        return alarmList;
    }
}
