package sws.songpin.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sws.songpin.domain.alarm.service.AlarmService;
import sws.songpin.domain.alarm.service.EmitterService;

@Tag(name = "Alarm", description = "Alarm 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {
    private final EmitterService emitterService;
    private final AlarmService alarmService;

    @Operation(summary = "알림 구독", description = "알림을 구독합니다.")
    @PostMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe() {
        return ResponseEntity.ok(emitterService.subscribe());
    }

    @Operation(summary = "안 읽은 알림 존재 여부 확인", description = "안 읽은 알림이 있는지 확인합니다.")
    @GetMapping("/check")
    public ResponseEntity<?> checkForUnreadAlarms() {
        return ResponseEntity.ok(alarmService.checkForUnreadAlarms());
    }

    @Operation(summary = "알림 목록 조회 및 읽음 처리", description = "알림 목록을 조회하고 읽음 처리합니다.")
    @PatchMapping
    public ResponseEntity<?> getRecentAlarms() {
        return ResponseEntity.ok(alarmService.getRecentAlarms());
    }
}
