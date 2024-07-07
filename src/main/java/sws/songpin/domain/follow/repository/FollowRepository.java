package sws.songpin.domain.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.follow.entity.Follow;
import sws.songpin.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> existsByFollowerAndFollowing(Member follower, Member following);
    List<Follow> findAllByFollowing(Member following);
    List<Follow> findAllByFollower(Member follower);
}