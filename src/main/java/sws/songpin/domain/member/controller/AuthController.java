package sws.songpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.member.dto.request.LoginRequestDto;
import sws.songpin.domain.member.dto.request.SignUpRequestDto;
import sws.songpin.domain.member.dto.response.LoginResponseDto;
import sws.songpin.domain.member.dto.response.TokenDto;
import sws.songpin.domain.member.service.AuthService;
import sws.songpin.global.auth.RedisService;

@Tag(name = "Auth", description = "인증 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RedisService redisService;

    @Operation(summary = "회원가입", description = "회원 가입을 통해 유저 생성")
    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@Valid @RequestBody SignUpRequestDto requestDto){
        authService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그인", description = "로그인 결과를 반환합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        TokenDto tokenDto = authService.login(requestDto);

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenDto.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(tokenDto.refreshTokenMaxAge());

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new LoginResponseDto(tokenDto.accessToken()));
    }

    @Operation(summary = "로그아웃", description = "Redis와 쿠키에 저장되었던 회원의 Refresh Token을 삭제합니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "refresh 토큰 저장 테스트", description = "로그인 후 refresh Token이 Redis에 잘 저장되었는지 확인 후 refresh token 반환")
    @GetMapping("/redisTest")
    public String redisTest(Authentication authentication){
        return (String) redisService.getValues(authentication.getName());
    }

    @Operation(summary = "토큰 검증 테스트")
    @GetMapping("/authTest")
    public String test(Authentication authentication){
        return authentication.getName();
    }

}
