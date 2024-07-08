package sws.songpin.global.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.repository.MemberRepository;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws CustomException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        return new CustomUserDetails(member);
    }
}
