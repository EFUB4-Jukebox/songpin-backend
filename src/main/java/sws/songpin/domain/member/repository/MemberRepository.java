package sws.songpin.domain.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByHandle(String handle);
    boolean existsByHandle(String handle);

    // 유저 검색
    @Query("SELECT m FROM Member m WHERE (m.handle LIKE %:keyword% ESCAPE '\\' OR m.nickname LIKE %:keyword% ESCAPE '\\') AND m.status <> 'DELETED'")
    Page<Member> findAllByHandleContainingOrNicknameContaining(@Param("keyword") String keyword, Pageable pageable);
}