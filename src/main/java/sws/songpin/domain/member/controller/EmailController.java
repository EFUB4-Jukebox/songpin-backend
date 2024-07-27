package sws.songpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.member.dto.request.EmailRequestDto;
import sws.songpin.domain.member.service.EmailService;

@Tag(name = "Email", description = "Email 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "비밀번호 재설정 이메일 전송", description = "요청받은 이메일로 비밀번호 재설정 링크 전달")
    @PostMapping("/mail/pw")
    public ResponseEntity<?> sendPasswordEmail(@RequestBody @Valid EmailRequestDto requestDto){
        emailService.sendPasswordEmail(requestDto);
        return ResponseEntity.ok().build();

    }
}
