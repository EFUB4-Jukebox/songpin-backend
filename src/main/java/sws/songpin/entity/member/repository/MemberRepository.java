package sws.songpin.entity.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.entity.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
}
