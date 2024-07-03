package sws.songpin.entity.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sws.songpin.entity.member.domain.Member;
import sws.songpin.entity.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //개발 진행을 위한 임시 메서드
    public Member getCurrentMember(){
        return memberRepository.findById((long)1).get();
    }
    public Member getMemberById(Long memberId){
        return memberRepository.findById(memberId).get();
    }


}
