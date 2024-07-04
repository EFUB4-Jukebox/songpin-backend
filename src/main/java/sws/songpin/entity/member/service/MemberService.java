package sws.songpin.entity.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sws.songpin.entity.member.domain.Member;
import sws.songpin.entity.member.dto.request.SignUpRequestDto;
import sws.songpin.entity.member.repository.MemberRepository;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //개발 진행을 위한 임시 메서드
    public Member getCurrentMember(){
        return memberRepository.findById((long)1).get();
    }
    public Member getMemberById(Long memberId){
        return memberRepository.findById(memberId).get();
    }

    public void signUp(SignUpRequestDto requestDto) {

        //이메일 중복 검사
        if (memberRepository.findByEmail(requestDto.email()).isPresent()) {
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

}
