package sws.songpin.domain.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sws.songpin.domain.alarm.dto.ssedata.AlarmDefaultDataDto;
import sws.songpin.domain.alarm.repository.AlarmRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmitterService {
    private final MemberService memberService;
    private final AlarmRepository alarmRepository;
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    private static final Long DEFAULT_TIMEOUT = 3L * 60 * 1000; // 3분
    private static final long RECONNECTION_TIMEOUT = 1000L;

    public SseEmitter subscribe() {
        Member member = memberService.getCurrentMember();
        Boolean isNewAlarm = alarmRepository.existsByReceiverAndIsReadFalse(member);
        return createAndRegisterEmitter(member.getMemberId().toString(), isNewAlarm);
    }

    private SseEmitter createAndRegisterEmitter(String memberId, Boolean isNewAlarm) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        // 타임아웃 핸들러 등록
        emitter.onTimeout(() -> {
            log.info("server sent event timed out : memberId={}", memberId);
            emitter.complete();
        });

        //에러 핸들러 등록
        emitter.onError(e -> {
            log.info("server sent event error occurred : memberId={}, message={}", memberId, e.getMessage());
            //onCompletion 핸들러 호출
            emitter.complete();
        });

        //SSE complete 핸들러 등록
        emitter.onCompletion(() -> {
            if (emitterMap.remove(memberId) != null) {
                log.info("server sent event removed in emitter cache: memberId={}", memberId);
            }
            log.info("disconnected by completed server sent event: memberId={}", memberId);
        });
        emitterMap.put(memberId, emitter);

        //초기 연결시에 응답 데이터 전송
        try {
            SseEmitter.SseEventBuilder event = sendSubcribeEvent(isNewAlarm);
            emitter.send(event);
        } catch (IOException e) {
            log.error("failure send media position data, memberId={}, {}", memberId, e.getMessage());
        }
        return emitter;
    }

    private SseEmitter.SseEventBuilder sendSubcribeEvent(Boolean isNewAlarm) throws IOException {
        String comment = isNewAlarm ? "new sse alarm exists" : "new sse alarm doesn't exists";
         return SseEmitter.event()
                .name("sse-alarm")
                .id("id-1") //재연결시 클라이언트에서 `Last-Event-ID` 헤더에 마지막 event id 를 설정
                .data(AlarmDefaultDataDto.from(isNewAlarm))
                .comment(comment)
                .reconnectTime(RECONNECTION_TIMEOUT);
    }

    public void notify(Long memberId, Object data, String comment) {
        try {
            SseEmitter emitter = emitterMap.get(memberId);
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .name("sse-alarm")
                    .data(data)
                    .comment(comment)
                    .reconnectTime(RECONNECTION_TIMEOUT);
            log.info("sended notification, memberId={}", memberId);
            emitter.send(event);
        } catch (IOException e) {
            log.error("failure send media position data, memberId={}, {}", memberId, e.getMessage());
        }
    }
}