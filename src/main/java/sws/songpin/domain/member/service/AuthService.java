package sws.songpin.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.member.dto.request.LoginRequestDto;
import sws.songpin.domain.member.dto.request.SignUpRequestDto;
import sws.songpin.domain.member.dto.response.TokenDto;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.Status;
import sws.songpin.domain.member.repository.MemberRepository;
import sws.songpin.global.auth.JwtUtil;
import sws.songpin.global.auth.RedisService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public void signUp(SignUpRequestDto requestDto) {

        Optional<Member> memberOptional = memberRepository.findByEmail(requestDto.email());

        //이메일 중복 검사
        if (memberOptional.isPresent()) {

            if(memberOptional.get().getStatus().equals(Status.DELETED)){
                throw new CustomException(ErrorCode.ALREADY_DELETED_MEMBER);
            }

            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        //비밀번호 일치 검사
        if (!requestDto.password().equals(requestDto.confirmPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        //handle 랜덤값 생성
        String handle = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);

        //Member 객체 생성 후 저장
        Member member = requestDto.toEntity(handle, passwordEncoder.encode(requestDto.password()));
        memberRepository.save(member);
    }

    public TokenDto login(LoginRequestDto requestDto){

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.email(), requestDto.password()
                    )
            );

            return new TokenDto(jwtUtil.generateAccessToken(authentication), jwtUtil.generateRefreshToken(authentication) );
        } catch (BadCredentialsException e){
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
    }

    public TokenDto reissueToken(String accessToken, String refreshToken){

        //Access Token 이 아직 만료되지 않은 경우 예외 처리
        if(!jwtUtil.isTokenExpired(accessToken)){
            throw new CustomException(ErrorCode.ACCESS_TOKEN_NOT_EXPIRED);
        }

        //쿠키의 Refresh Token 유효성 검사
        jwtUtil.validateRefreshToken(refreshToken);

        //Access Token 에 담긴 Authentication 과 매치되는 Refresh Token 을 Redis 에서 조회
        Authentication authentication = jwtUtil.getAuthentication(accessToken);
        String storedRefreshToken = (String) redisService.getValues(authentication.getName());

        //쿠키의 Refresh Token 과 Redis의 Refresh Token 비교
        if(!(refreshToken.equals(storedRefreshToken))){
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        //Access Token 및 Refresh Token 재발급
        return new TokenDto(jwtUtil.generateAccessToken(authentication), jwtUtil.generateRefreshToken(authentication) );

    }

    @Transactional(readOnly = true)
    public boolean checkPassword(Member member, String password){
        return passwordEncoder.matches(password, member.getPassword());
    }

}
