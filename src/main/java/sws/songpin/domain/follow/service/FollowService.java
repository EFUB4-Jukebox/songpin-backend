package sws.songpin.domain.follow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.follow.dto.request.FollowAddRequestDto;
import sws.songpin.domain.follow.dto.response.FollowAddResponseDto;
import sws.songpin.domain.follow.dto.response.FollowDto;
import sws.songpin.domain.follow.dto.response.FollowListResponseDto;
import sws.songpin.domain.follow.entity.Follow;
import sws.songpin.domain.follow.repository.FollowRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {
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
        return FollowAddResponseDto.from(follow);
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

    // 특정 사용자의 팔로잉/팔로워 목록 조회
    public FollowListResponseDto getFollowList(Long memberId, Boolean isFollowingList) {
        Member targetMember = memberService.getMemberById(memberId);
        Member currentMember = memberService.getCurrentMember();
        Map<Member, Long> currentMemberFollowingCache = getMemberFollowingCache(currentMember);
        List<Follow> followList = isFollowingList ? findAllFollowingOfMember(targetMember) : findAllFollowersOfMember(targetMember);

        List<FollowDto> followDtoList = followList.stream()
                .map(follow -> {
                    Member member = isFollowingList ? follow.getFollowing() : follow.getFollower();
                    Long followId = currentMemberFollowingCache.get(member);
                    // 내가 해당 member를 팔로잉 중인지 여부 (null: 자신)
                    Boolean isFollowing = member.equals(currentMember) ? null : followId != null;
                    return new FollowDto(
                            member.getMemberId(),
                            member.getProfileImg(),
                            member.getNickname(),
                            member.getHandle(),
                            isFollowing,
                            followId // currentMember가 팔로잉하는 경우 currentMember와의 followId 삽입
                    );
                })
                // 우선순위대로 정렬 (1차: null > true > false, 2차: followId 높은 것부터)
                .sorted(Comparator.comparing(FollowDto::isFollowing, Comparator.nullsFirst(Comparator.reverseOrder()))
                        .thenComparing(FollowDto::followId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        return FollowListResponseDto.from(targetMember.equals(currentMember), targetMember.getHandle(), followDtoList);
    }

    // member의 팔로잉을 key로 followId를 가져오기 위한 캐시를 생성 (팔로잉/팔로워 목록 조회 시 사용)
    public Map<Member, Long> getMemberFollowingCache(Member member) {
        List<Follow> followingList = findAllFollowingOfMember(member);
        return followingList.stream()
                .collect(Collectors.toMap(Follow::getFollowing, Follow::getFollowId));
    }

    @Transactional(readOnly = true)
    public Follow findFollowById(Long followId){
        return followRepository.findById(followId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOLLOW_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public boolean checkFollowExists(Member follower, Member following) {
        return followRepository.existsByFollowerAndFollowing(follower, following).booleanValue();
    }

    // member의 팔로워들
    @Transactional(readOnly = true)
    public List<Follow> findAllFollowersOfMember(Member member){
        return followRepository.findAllByFollowing(member);
    }

    // member의 팔로잉들
    @Transactional(readOnly = true)
    public List<Follow> findAllFollowingOfMember(Member member){
        return followRepository.findAllByFollower(member);
    }
}
