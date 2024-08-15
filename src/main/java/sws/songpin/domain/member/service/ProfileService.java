package sws.songpin.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.alarm.service.AlarmService;
import sws.songpin.domain.bookmark.service.BookmarkService;
import sws.songpin.domain.follow.service.FollowService;
import sws.songpin.domain.member.dto.request.PasswordUpdateRequestDto;
import sws.songpin.domain.member.dto.request.ProfileDeactivateRequestDto;
import sws.songpin.domain.member.dto.request.ProfileUpdateRequestDto;
import sws.songpin.domain.member.dto.response.MemberProfileResponseDto;
import sws.songpin.domain.member.dto.response.MyProfileResponseDto;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.ProfileImg;
import sws.songpin.domain.playlist.service.PlaylistService;
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
    private final AlarmService alarmService;
    private final BookmarkService bookmarkService;
    private final PlaylistService playlistService;

    @Transactional(readOnly = true)
    public MemberProfileResponseDto getMemberProfile(Long memberId){
        Member member = memberService.getActiveMemberById(memberId);
        Member currentMember = memberService.getCurrentMember();

        //조회하려는 회원이 본인인 경우 예외 처리
        if (member.equals(currentMember)){
            throw new CustomException(ErrorCode.MEMBER_BAD_REQUEST);
        }

        //팔로워 수
        long followerCount = followService.getFollowerCount(member);

        //팔로잉 수
        long followingCount = followService.getFollowingCount(member);

        //팔로우 여부
        Boolean isFollowing = followService.checkIfFollowing(member);

        return MemberProfileResponseDto.from(member, followerCount, followingCount, isFollowing);
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
        if (memberService.checkMemberExistsByHandle(requestDto.handle()) && !(member.getHandle().equals(requestDto.handle()))){
            throw new CustomException(ErrorCode.HANDLE_ALREADY_EXISTS);
        }
        member.modifyProfile(ProfileImg.from(requestDto.profileImg()), requestDto.nickname(), requestDto.handle());
        memberService.saveMember(member);
    }

    public void updatePassword(PasswordUpdateRequestDto requestDto){
        Member member = memberService.getCurrentMember();
        //비밀번호 일치 검사
        if (!requestDto.password().equals(requestDto.confirmPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
        member.modifyPassword(authService.encodePassword(requestDto.password()));
        memberService.saveMember(member);
    }

    public void deactivateProfile(ProfileDeactivateRequestDto requestDto){
        Member member = memberService.getCurrentMember();

        //패스워드 검사
        if (!(authService.checkPassword(member, requestDto.password()))){
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        //handle 랜덤값 생성
        String handle = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
        //Status, Nickname, Handle 변경
        member.deactivate(handle);
        memberService.saveMember(member);

        //follow 테이블 데이터 삭제
        followService.deleteAllFollowsOfMember(member);

        //bookmark 테이블 데이터 삭제
        bookmarkService.deleteAllBookmarksOfMember(member);

        //playlist 테이블 데이터 삭제
        playlistService.deleteAllPlaylistsOfMember(member);

        //alarm 테이블 데이터 삭제
        alarmService.deleteAllAlarmsOfMember(member);

        //Redis에서 Refresh Token 삭제
        redisService.deleteValues(member.getEmail());
    }

}
