package sws.songpin.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.member.dto.response.MemberSearchResponseDto;
import sws.songpin.domain.member.dto.response.MemberUnitDto;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.Status;
import sws.songpin.domain.member.repository.MemberRepository;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 유저 검색
    @Transactional(readOnly = true)
    public MemberSearchResponseDto searchMembers(String keyword, Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAllByHandleContainingOrNicknameContaining(keyword, pageable);
        Long currentMemberId = getCurrentMember().getMemberId();

        // Page<Member>를 Page<MemberUnitDto>로 변환
        Page<MemberUnitDto> memberUnitDtoPage = memberPage.map(member ->
                MemberUnitDto.from(member, member.getMemberId().equals(currentMemberId))
        );
        // MemberSearchResponseDto를 반환
        return MemberSearchResponseDto.from(memberUnitDtoPage);
    }

    @Transactional(readOnly = true)
    public Member getCurrentMember() throws CustomException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_AUTHENTICATED));
        if (member.getStatus().equals(Status.DELETED)) {
            throw new CustomException(ErrorCode.MEMBER_STATUS_DELETED);
        }
        return member;
    }

    @Transactional(readOnly = true)
    public Member getCurrentMemberOrNull() {
        try {
            return getCurrentMember();
        } catch (CustomException e) { // 로그인하지 않은 경우
            return null;
        }
    }

    @Transactional(readOnly = true)
    public Member getMemberById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member getActiveMemberById(Long memberId){
        Member member = getMemberById(memberId);
        if (member.getStatus().equals(Status.DELETED)) {
            throw new CustomException(ErrorCode.MEMBER_STATUS_DELETED);
        }
        return member;
    }

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member getActiveMemberByEmail(String email) {
        Member member = getMemberByEmail(email);
        if (member.getStatus().equals(Status.DELETED)) {
            throw new CustomException(ErrorCode.MEMBER_STATUS_DELETED);
        }
        return member;
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMemberOptionalByEmail(String email){
        return memberRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkMemberExistsByHandle(String handle){
        return memberRepository.existsByHandle(handle);
    }

    public Member saveMember(Member member){
        return memberRepository.save(member);
    }
}
