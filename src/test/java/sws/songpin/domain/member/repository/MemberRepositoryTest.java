package sws.songpin.domain.member.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sws.songpin.domain.member.entity.Member;
import java.util.stream.IntStream;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testInsertMember(){
        IntStream.rangeClosed(1,10).forEach(i-> {
            Member member = Member.builder()
                    .memberId((long) i)
                    .email("test"+i+"@test.com")
                    .nickname("test"+i)
                    .password("testpw"+i)
                    .handle("testhandle"+i)
                    .build();
            memberRepository.save(member);
        });
    }

}