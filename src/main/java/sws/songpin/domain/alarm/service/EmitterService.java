package sws.songpin.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sws.songpin.domain.alarm.dto.ssedata.AlarmDefaultDataDto;
import sws.songpin.domain.alarm.repository.AlarmRepository;
import sws.songpin.domain.alarm.repository.EmitterRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.global.auth.CustomUserDetails;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmitterService {
    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;

    private static final Long DEFAULT_TIMEOUT = 1L * 100;  // 0.1초

    public SseEmitter subscribe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        SseEmitter emitter = registerEmitter(member);
        sendToClientIfNewAlarmExists(member);
        return emitter;
    }

    public void notify(Long memberId, Object data, String comment) {
        sendToClient(memberId, data, comment);
    }

    private SseEmitter registerEmitter(Member member) {
        Long memberId = member.getMemberId();
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(memberId, emitter);

        emitter.onCompletion(() -> emitterRepository.delete(memberId));
        emitter.onTimeout(() -> emitterRepository.delete(memberId));

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
