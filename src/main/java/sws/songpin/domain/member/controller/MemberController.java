package sws.songpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.member.service.MemberService;

@Tag(name = "Member", description = "Member 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "유저 검색", description = "유저를 검색합니다.")
    @GetMapping
    public ResponseEntity<?> searchMembers(@RequestParam final String keyword,
                                           @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok().body(memberService.searchMembers(keyword, pageable));
    }
}
