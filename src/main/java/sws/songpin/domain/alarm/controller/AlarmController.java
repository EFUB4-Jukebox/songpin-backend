package sws.songpin.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sws.songpin.domain.alarm.service.AlarmService;
import sws.songpin.domain.alarm.service.EmitterService;

@Slf4j
@Tag(name = "Alarm", description = "Alarm 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {
    private final EmitterService emitterService;
    private final AlarmService alarmService;

    @Operation(summary = "알림 구독", description = "알림을 구독합니다.")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe() {
        return ResponseEntity.ok(emitterService.subscribe());
    }

    // 신규 알림 목록 읽어오기 API
    @Operation(summary = "알림 목록 조회", description = "알림 목록을 조회합니다.")
    @PatchMapping("/list")
    public ResponseEntity<?> getUnreadAlarms() {
        return ResponseEntity.ok(alarmService.getAlarmList());
    }
}
