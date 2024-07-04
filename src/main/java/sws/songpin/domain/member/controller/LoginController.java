package sws.songpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.member.dto.request.SignUpRequestDto;
import sws.songpin.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원 가입을 통해 유저 생성")
    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@Valid @RequestBody SignUpRequestDto requestDto){
        memberService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
