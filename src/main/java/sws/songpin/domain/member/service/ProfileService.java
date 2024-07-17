package sws.songpin.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sws.songpin.domain.follow.service.FollowService;
import sws.songpin.domain.member.dto.response.MemberProfileResponseDto;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberService memberService;
    private final FollowService followService;

    public MemberProfileResponseDto getMemberProfile(Long memberId){
        Member member = memberService.getMemberById(memberId);
        Member currentMember = memberService.getCurrentMember();

        if(member.equals(currentMember)){
            throw new CustomException(ErrorCode.MEMBER_BAD_REQUEST);
        }

        //팔로워 수
        long followerCount = followService.getFollowerCount(member);

        //팔로잉 수
        long followingCount = followService.getFollowingCount(member);

        //팔로우 여부 (팔로우ID)
        Long followId = null;
        if(followService.checkFollowExists(currentMember,member)){
            followId = followService.getFollowId(currentMember,member);
        }

        return MemberProfileResponseDto.from(member, followerCount, followingCount, followId);

    }
}
