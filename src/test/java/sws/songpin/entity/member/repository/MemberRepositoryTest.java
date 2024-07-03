package sws.songpin.entity.member.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sws.songpin.entity.member.domain.Member;
import sws.songpin.entity.member.domain.ProfileImg;
import sws.songpin.entity.member.domain.Status;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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
                    .nickname("testnickname"+i)
                    .password("testpw"+i)
                    .handle("testhandle"+i)
                    .profileImg(ProfileImg.DANCE)
                    .status(Status.ACTIVE)
                    .isNewAlarm(false)
                    .build();
            memberRepository.save(member);
        });
    }

}