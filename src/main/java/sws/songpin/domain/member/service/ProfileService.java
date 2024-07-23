package sws.songpin.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.follow.service.FollowService;
import sws.songpin.domain.member.dto.request.ProfileDeactivateRequestDto;
import sws.songpin.domain.member.dto.request.ProfileUpdateRequestDto;
import sws.songpin.domain.member.dto.response.MemberProfileResponseDto;
import sws.songpin.domain.member.dto.response.MyProfileResponseDto;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.ProfileImg;
import sws.songpin.global.auth.RedisService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {
    private final MemberService memberService;
    private final FollowService followService;
    private final AuthService authService;
    private final RedisService redisService;

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public MyProfileResponseDto getMyProfile(){
        Member member = memberService.getCurrentMember();

        //팔로워 수
        long followerCount = followService.getFollowerCount(member);

        //팔로잉 수
        long followingCount = followService.getFollowingCount(member);

        return MyProfileResponseDto.from(member, followerCount, followingCount);
    }

    public void updateProfile(ProfileUpdateRequestDto requestDto){

        Member member = memberService.getCurrentMember();

        //핸들 중복 검사
        if(memberService.checkMemberExistsByHandle(requestDto.handle()) && !(member.getHandle().equals(requestDto.handle()))){
            throw new CustomException(ErrorCode.HANDLE_ALREADY_EXISTS);
        }

        member.modifyProfile(ProfileImg.from(requestDto.profileImg()), requestDto.nickname(), requestDto.handle());

        memberService.saveMember(member);

    }

    public void deactivateProfile(ProfileDeactivateRequestDto requestDto){

        Member member = memberService.getCurrentMember();

        //패스워드 검사
        if(!(authService.checkPassword(member, requestDto.password()))){
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        //handle 랜덤값 생성
        String handle = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
        //Status, Nickname, Handle 변경
        member.deactivate(handle);
        memberService.saveMember(member);

        //Redis에서 Refresh Token 삭제
        redisService.deleteValues(member.getEmail());
    }

}
