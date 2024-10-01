package sws.songpin.domain.member.service;

import lombok.RequiredArgsConstructor;
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

import static sws.songpin.global.common.EscapeSpecialCharactersService.escapeSpecialCharacters;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 유저 검색
    public MemberSearchResponseDto searchMembers(String keyword, Pageable pageable) {
        // 키워드의 이스케이프 처리
        String escapedWord = escapeSpecialCharacters(keyword);
        Page<Member> memberPage = getSearchedMemberPage(escapedWord, pageable);
        Long currentMemberId = getCurrentMember().getMemberId();

        // Page<Member>를 Page<MemberUnitDto>로 변환
        Page<MemberUnitDto> memberUnitDtoPage = memberPage.map(member ->
                MemberUnitDto.from(member, member.getMemberId().equals(currentMemberId))
        );
        // MemberSearchResponseDto를 반환
        return MemberSearchResponseDto.from(memberUnitDtoPage);
    }

    @Transactional(readOnly = true)
    public Page<Member> getSearchedMemberPage(String escapedWord, Pageable pageable) {
        return memberRepository.findAllByHandleContainingOrNicknameContaining(escapedWord, pageable);
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
    public Member getActiveMemberById(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (member.getStatus().equals(Status.DELETED)) {
            throw new CustomException(ErrorCode.MEMBER_STATUS_DELETED);
        }
        return member;
    }

    @Transactional(readOnly = true)
    public Member getActiveMemberByHandle(String handle){
        Member member = memberRepository.findByHandle(handle)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (member.getStatus().equals(Status.DELETED)) {
            throw new CustomException(ErrorCode.MEMBER_STATUS_DELETED);
        }
        return member;
    }

    @Transactional(readOnly = true)
    public Member getActiveMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
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

    @Transactional
    public Member saveMember(Member member){
        return memberRepository.save(member);
    }
}
