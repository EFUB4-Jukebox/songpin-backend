package sws.songpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.member.dto.response.MemberResponseDto;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.member.service.ProfileService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "Member", description = "Member 관련 API입니다.")
public class MemberController {
    private final MemberService memberService;
    private final ProfileService profileService;

    @Operation(summary = "타 유저 정보 조회", description = "memberId으로 해당 유저의 프로필 이미지, 닉네임, 아이디 정보 조회")
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> memberDetails(@PathVariable("memberId") final Long memberId){
        MemberResponseDto responseDto = profileService.getMemberProfile(memberId);
        return ResponseEntity.ok(responseDto);
    }

}
