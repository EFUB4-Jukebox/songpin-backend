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
    @GetMapping(value = "/subscribe/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE) // Content-Type 지정
    public ResponseEntity<SseEmitter> subscribe(@PathVariable("memberId") final Long memberId) {
        return ResponseEntity.ok(emitterService.subscribe(memberId));
    }

    @Operation(summary = "알림 목록 조회 및 읽음 처리", description = "알림 목록을 조회하고 읽음 처리합니다.")
    @PatchMapping
    public ResponseEntity<?> getRecentAlarms() {
        return ResponseEntity.ok(alarmService.getAndReadRecentAlarms());
    }
}
