package sws.songpin.domain.follow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.follow.dto.request.FollowAddRequestDto;
import sws.songpin.domain.follow.dto.response.FollowAddResponseDto;
import sws.songpin.domain.follow.dto.response.FollowDto;
import sws.songpin.domain.follow.dto.response.FollowerListResponseDto;
import sws.songpin.domain.follow.dto.response.FollowingListResponseDto;
import sws.songpin.domain.follow.entity.Follow;
import sws.songpin.domain.follow.repository.FollowRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowSerivce {
    private final FollowRepository followRepository;
    private final MemberService memberService;

    // 팔로우 추가
    public FollowAddResponseDto addFollow(FollowAddRequestDto followAddRequestDto){
        Member follower = memberService.getMemberById(followAddRequestDto.followerId());
        Member following = memberService.getMemberById(followAddRequestDto.followingId());

        if (!follower.equals(memberService.getCurrentMember())){ // follower가 자신이어야 함
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        } else if (follower.equals(following)){ // follower가 following과 동일하면 팔로우 불가
            throw new CustomException(ErrorCode.FOLLOW_BAD_REQUEST);
        } else if (checkFollowExists(follower, following)){ // follow가 이미 존재하면 팔로우 불가
            throw new CustomException(ErrorCode.FOLLOW_ALREADY_EXISTS);
        }
        Follow follow = followRepository.save(FollowAddRequestDto.toEntity(follower, following));
        return FollowAddResponseDto.fromEntity(follow);
    }

    // 팔로우 삭제
    public void deleteFollow(Long followId) {
        Follow follow = findFollowById(followId);
        Member currentMember = memberService.getCurrentMember();
        boolean isFollowerOrFollowing = currentMember.equals(follow.getFollower()) || currentMember.equals(follow.getFollowing());
        if (!isFollowerOrFollowing) { // 자신이 follower거나 following이 아니면 follow 삭제 불가능
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        followRepository.delete(follow);
    }

    // 특정 사용자의 팔로워 목록 조회
    public FollowerListResponseDto getFollowerList(Long memberId) {
        Member targetMember = memberService.getMemberById(memberId);
        List<Follow> followerList = findAllFollowersOfMember(targetMember);
        Member currentMember = memberService.getCurrentMember();
        Map<Member, Long> currentMemberFollowingCache = getMemberFollowingCache(currentMember);

        List<FollowDto> followDtoList = followerList.stream()
                .map(follow -> {
                    Member follower = follow.getFollower();
                    return new FollowDto(
                            follower.getMemberId(),
                            follower.getProfileImg(),
                            follower.getNickname(),
                            follower.getHandle(),
                            currentMemberFollowingCache.get(follower) // currentMember가 팔로잉하는 경우 currentMember와의 followId 삽입
                    );
                })
                .collect(Collectors.toList());
        return FollowerListResponseDto.fromEntity(targetMember.equals(currentMember), targetMember.getHandle(), followDtoList);
    }

    // 특정 사용자의 팔로잉 목록 조회
    public FollowingListResponseDto getFollowingList(Long memberId) {
        Member targetMember = memberService.getMemberById(memberId);
        List<Follow> followingList = findAllFollowingOfMember(targetMember);
        Member currentMember = memberService.getCurrentMember();
        Map<Member, Long> currentMemberFollowingCache = getMemberFollowingCache(currentMember);

        List<FollowDto> followDtoList = followingList.stream()
                .map(follow -> {
                    Member following = follow.getFollowing();
                    return new FollowDto(
                            following.getMemberId(),
                            following.getProfileImg(),
                            following.getNickname(),
                            following.getHandle(),
                            currentMemberFollowingCache.get(following) // currentMember가 팔로잉하는 경우 currentMember와의 followId 삽입
                    );
                })
                .collect(Collectors.toList());
        return FollowingListResponseDto.fromEntity(targetMember.equals(currentMember), targetMember.getHandle(), followDtoList);
    }

    // Member가 팔로잉중인지 확인하고 followId를 가져오기 위한 캐시를 생성해 반환 (팔로워 목록 조회, 팔로잉 목록 조회 시 사용)
    public Map<Member, Long> getMemberFollowingCache(Member member) {
        List<Follow> followingList = findAllFollowingOfMember(member);
        return followingList.stream()
                .collect(Collectors.toMap(Follow::getFollower, Follow::getFollowId));
    }

    @Transactional(readOnly = true)
    public Follow findFollowById(Long followId){
        return followRepository.findById(followId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOLLOW_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public boolean checkFollowExists(Member follower, Member following) {
        return followRepository.existsByFollowerAndFollowing(follower, following)
                .isPresent();
    }

    @Transactional(readOnly = true)
    public List<Follow> findAllFollowersOfMember(Member member){
        return followRepository.findAllByFollowing(member);
    }

    @Transactional(readOnly = true)
    public List<Follow> findAllFollowingOfMember(Member member){
        return followRepository.findAllByFollower(member);
    }
}
