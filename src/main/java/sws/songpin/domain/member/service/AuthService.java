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
import sws.songpin.global.auth.CustomUserDetailsService;
import sws.songpin.global.auth.JwtUtil;
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
    private final CustomUserDetailsService userDetailsService;

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

            TokenDto responseDto = new TokenDto(jwtUtil.generateAccessToken(authentication), jwtUtil.generateRefreshToken(authentication) );

            return responseDto;
        } catch (BadCredentialsException e){
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
    }

    @Transactional(readOnly = true)
    public boolean checkPassword(Member member, String password){
        return passwordEncoder.matches(password, member.getPassword());
    }

}
