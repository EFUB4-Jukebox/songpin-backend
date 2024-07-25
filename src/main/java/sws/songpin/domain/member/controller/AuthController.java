package sws.songpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.member.dto.request.LoginRequestDto;
import sws.songpin.domain.member.dto.request.PasswordResetRequestDto;
import sws.songpin.domain.member.dto.request.PasswordUpdateRequestDto;
import sws.songpin.domain.member.dto.request.SignUpRequestDto;
import sws.songpin.domain.member.dto.response.LoginResponseDto;
import sws.songpin.domain.member.dto.response.ReissueResponseDto;
import sws.songpin.domain.member.dto.response.TokenDto;
import sws.songpin.domain.member.service.AuthService;
import sws.songpin.global.auth.CookieUtil;
import sws.songpin.global.auth.RedisService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

@Tag(name = "Auth", description = "인증 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RedisService redisService;
    private final CookieUtil cookieUtil;

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

        cookieUtil.addCookie(response, "refreshToken", tokenDto.refreshToken(), tokenDto.refreshTokenMaxAge());

        return ResponseEntity.ok(new LoginResponseDto(tokenDto.accessToken()));
    }

    @Operation(summary = "로그아웃", description = "Redis와 쿠키에 저장되었던 회원의 Refresh Token을 삭제합니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "토큰 재발급", description = "Access Token 만료 시 Refresh Token 을 이용하여 Access Token 및 Refresh Token 재발급")
    @PostMapping("/token")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){

        //헤더에서 Access Token 추출
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken == null || !bearerToken.startsWith("Bearer ")){
            throw new CustomException(ErrorCode.NOT_AUTHENTICATED);
        }
        String accessToken = bearerToken.substring(7);

        //쿠키에서 Refresh Token 추출
        String refreshToken = cookieUtil.getCookieValue(request,"refreshToken");

        //토큰 재발급 서비스 로직
        TokenDto tokenDto = authService.reissueToken(accessToken,refreshToken);

        //새로 발급 받은 리프레시 토큰을 쿠키에 설정
        cookieUtil.addCookie(response, "refreshToken", tokenDto.refreshToken(), tokenDto.refreshTokenMaxAge());

        //새로 발급 받은 어세스 토큰을 Response Body를 통해 전달
        return ResponseEntity.ok(new ReissueResponseDto(tokenDto.accessToken()));
    }

    @Operation(summary = "비밀번호 재설정 링크를 통해 비밀번호 변경", description = "이메일로 전송된 비밀번호 재설정 링크를 통해 비밀번호 변경")
    @PatchMapping("/login/pw")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordResetRequestDto requestDto){
        authService.resetPassword(requestDto);
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
