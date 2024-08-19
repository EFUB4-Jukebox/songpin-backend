package sws.songpin.domain.email.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.email.dto.EmailRequestDto;
import sws.songpin.domain.email.dto.ReportRequestDto;
import sws.songpin.domain.email.service.EmailService;

@Tag(name = "Email", description = "Email 관련 API입니다.")
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "비밀번호 재설정 이메일 전송", description = "요청받은 이메일로 비밀번호 재설정 링크 전달")
    @PostMapping("/pw")
    public ResponseEntity<?> sendPasswordEmail(@RequestBody @Valid EmailRequestDto requestDto){
        emailService.sendPasswordEmail(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 신고 메일 전송", description = "유저 신고 내역을 담은 보고서를 운영자 메일로 전송")
    @PostMapping("/report")
    public ResponseEntity<?> sendReportEmail(@RequestBody @Valid ReportRequestDto requestDto){
        emailService.sendReportEmail(requestDto);
        return ResponseEntity.ok().build();
    }
}
