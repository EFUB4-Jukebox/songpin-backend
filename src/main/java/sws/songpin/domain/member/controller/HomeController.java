package sws.songpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sws.songpin.domain.member.service.HomeService;
import sws.songpin.domain.member.service.MemberService;

@Tag(name = "Home", description = "Home 기능 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;

    @Operation(summary = "홈 페이지", description = "(1) 인사말, (2) 최근 등록된 핀 3개, (3) 최근 등록된 장소 3개")
    @GetMapping
    public ResponseEntity<?> getHome(){
        return ResponseEntity.ok(homeService.getHome());
    }

}
