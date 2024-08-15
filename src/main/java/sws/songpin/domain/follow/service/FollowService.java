package sws.songpin.domain.follow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.alarm.service.AlarmService;
import sws.songpin.domain.follow.dto.request.FollowRequestDto;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberService memberService;
    private final AlarmService alarmService;

    public boolean createOrDeleteFollow(FollowRequestDto requestDto) {
        Member follower = memberService.getCurrentMember();
        Member following = memberService.getActiveMemberById(requestDto.targetMemberId());

        if (!follower.equals(memberService.getCurrentMember())){ // follower가 자신이어야 함
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        } else if (follower.equals(following)) { // follower가 following과 동일하면 팔로우 불가
            throw new CustomException(ErrorCode.FOLLOW_BAD_REQUEST);
        }

        Optional<Follow> followOptional = followRepository.findByFollowerAndFollowing(follower, following);
        if (followOptional.isPresent()) { // 팔로우가 이미 존재하면 삭제
            followRepository.delete(followOptional.get());
            return false;
        } else { // 팔로우 추가
            followRepository.save(FollowRequestDto.toEntity(follower, following));
            alarmService.createFollowAlarm(follower, following);
            return true;
        }
    }

    public Boolean checkIfFollowing(Member following){
        Member follower = memberService.getCurrentMember();
        if (follower.equals(following)){
            return null;
        }
        Optional<Follow> followOptional = followRepository.findByFollowerAndFollowing(follower, following);
        if (followOptional.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    // 특정 사용자의 팔로잉/팔로워 목록 조회
    public FollowListResponseDto getFollowList(Long memberId, boolean isFollowingList) {
        Member targetMember = memberService.getActiveMemberById(memberId);
        Member currentMember = memberService.getCurrentMember();
        Map<Member, Long> currentMemberFollowingCache = getMemberFollowingCache(currentMember);
        List<Follow> followList = isFollowingList ? findAllFollowingsOfMember(targetMember) : findAllFollowersOfMember(targetMember);

        List<FollowDto> followDtoList = followList.stream()
                .map(follow -> {
                    Member member = isFollowingList ? follow.getFollowing() : follow.getFollower();
                    // isFollowing: 로그인한 사용자의 member 팔로잉 여부 (null: 자신)
                    Boolean isFollowing = member.equals(currentMember) ? null : currentMemberFollowingCache.get(member) != null;
                    return FollowDto.from(member, isFollowing);
                })
                // 우선순위대로 정렬 (null > true > false)
                .sorted(Comparator.comparing(FollowDto::isFollowing, Comparator.nullsFirst(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        return FollowListResponseDto.from(targetMember.equals(currentMember), targetMember.getHandle(), followDtoList);
    }

    // member의 팔로잉을 key로 followId를 가져오기 위한 캐시를 생성 (팔로잉/팔로워 목록 조회 시 사용)
    public Map<Member, Long> getMemberFollowingCache(Member member) {
        List<Follow> followingList = findAllFollowingsOfMember(member);
        return followingList.stream()
                .collect(Collectors.toMap(Follow::getFollowing, Follow::getFollowId));
    }

    // member의 팔로워들
    @Transactional(readOnly = true)
    public List<Follow> findAllFollowersOfMember(Member member){
        return followRepository.findAllByFollowing(member);
    }

    // member의 팔로잉들
    @Transactional(readOnly = true)
    public List<Follow> findAllFollowingsOfMember(Member member){
        return followRepository.findAllByFollower(member);
    }

    @Transactional(readOnly = true)
    public long getFollowerCount(Member member){
        return followRepository.countByFollowing(member);
    }

    @Transactional(readOnly = true)
    public long getFollowingCount(Member member){
        return followRepository.countByFollower(member);
    }

    @Transactional(readOnly = true)
    public Long getFollowId(Member follower, Member following){
        return followRepository.findByFollowerAndFollowing(follower,following)
                .orElseThrow(()-> new CustomException(ErrorCode.FOLLOW_NOT_FOUND)).getFollowId();
    }

    //회원의 팔로워 및 팔로잉 모두 삭제
    public void deleteAllFollowsOfMember(Member member){
        followRepository.deleteAllByFollower(member);
        followRepository.deleteAllByFollowing(member);
    }
}
