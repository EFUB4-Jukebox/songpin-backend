package sws.songpin.entity.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.entity.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
