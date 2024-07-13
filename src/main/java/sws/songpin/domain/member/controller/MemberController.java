package sws.songpin.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


}
