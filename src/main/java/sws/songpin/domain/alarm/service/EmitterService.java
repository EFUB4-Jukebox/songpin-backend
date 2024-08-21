package sws.songpin.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sws.songpin.domain.alarm.dto.ssedata.AlarmDefaultDataDto;
import sws.songpin.domain.alarm.repository.AlarmRepository;
import sws.songpin.domain.alarm.repository.EmitterRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmitterService {
    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final MemberService memberService;

    private static final Long DEFAULT_TIMEOUT = 5L * 60 * 1000;  // 5분

    public SseEmitter subscribe() {
        Member member = memberService.getCurrentMember();
        Long memberId = member.getMemberId();

        // 이미 존재하는 Emitter가 있는지 확인
        SseEmitter emitter = Optional.ofNullable(emitterRepository.get(memberId))
                .orElseGet(() -> registerEmitter(memberId));

        sendToClientIfNewAlarmExists(member);
        return emitter;
    }

    private void sendToClientIfNewAlarmExists(Member member) {
        Boolean isMissedAlarms = alarmRepository.existsByReceiverAndIsReadFalse(member);
        if (isMissedAlarms.equals(true)) {
            sendToClient(member.getMemberId(), AlarmDefaultDataDto.from(true), "new sse alarm exists");
        } else {
            sendToClient(member.getMemberId(), AlarmDefaultDataDto.from(false), "new sse alarm doesn't exists");
        }
    }

    private SseEmitter registerEmitter(Long memberId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(memberId, emitter);

        emitter.onCompletion(() -> emitterRepository.delete(memberId));
        emitter.onTimeout(() -> emitterRepository.delete(memberId));

        return emitter;
    }

    public void notify(Long memberId, Object data, String comment) {
        sendToClient(memberId, data, comment);
    }

    private <T> void sendToClient(Long memberId, Object data, String comment) {
        SseEmitter emitter = emitterRepository.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(memberId))
                        .name("sse-alarm")
                        .data(data)
                        .comment(comment));
            } catch (IOException e) {
                emitterRepository.delete(memberId);
                emitter.completeWithError(e);
            }
        }
    }
}
