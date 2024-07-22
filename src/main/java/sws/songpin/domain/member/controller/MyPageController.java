package sws.songpin.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.bookmark.service.BookmarkService;
import sws.songpin.domain.member.dto.request.ProfileDeactivateRequestDto;
import sws.songpin.domain.member.dto.request.ProfileUpdateRequestDto;
import sws.songpin.domain.member.service.ProfileService;
import sws.songpin.domain.pin.service.PinService;
import sws.songpin.domain.playlist.service.PlaylistService;

@Tag(name = "MyPage", description = "MyPage 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/me")
public class MyPageController {
    private final PlaylistService playlistService;
    private final BookmarkService bookmarkService;
    private final ProfileService profileService;
    private final PinService pinService;

    @Operation(summary = "내 플레이리스트 목록 조회", description = "마이페이지에서 내 플레이리스트 목록 조회")
    @GetMapping("/playlists")
    public ResponseEntity<?> getAllPlaylists(){
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @Operation(summary = "내 북마크 목록 조회", description = "마이페이지에서 북마크 목록 조회")
    @GetMapping("/bookmarks")
    public ResponseEntity<?> getAllBookmarks(){
        return ResponseEntity.ok(bookmarkService.getAllBookmarks());
    }

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 프로필 이미지, 닉네임, 아이디 정보 조회")
    @GetMapping
    public ResponseEntity<?> getMyProfile(){
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    @Operation(summary = "프로필 편집", description = "프로필 이미지, 닉네임, 핸들 변경")
    @PatchMapping
    public ResponseEntity<?> updateProfile(@RequestBody @Valid ProfileUpdateRequestDto requestDto){
        profileService.updateProfile(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 핀 피드 조회", description = "현재 사용자의 모든 핀 피드를 조회합니다.")
    @GetMapping("/pins")
    public ResponseEntity<?> getMyPinFeed() {
        return ResponseEntity.ok(pinService.getMyPinFeed());
    }

    @Operation(summary = "내 핀 피드 월별 조회", description = "현재 사용자의 핀 피드를 년/월별로 조회합니다.")
    @GetMapping("/calendar")
    public ResponseEntity<?> getMyFeedPinsByMonth(@RequestParam("year") int year, @RequestParam("month") int month) {
        return ResponseEntity.ok(pinService.getMyPinFeedForMonth(year, month));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 상태를 '탈퇴'로 변경하고 닉네임을 '알 수 없음'으로 변경합니다. Redis와 쿠키에 저장되었던 회원의 Refresh Token을 삭제합니다. 해당 회원이 등록했던 핀 등의 데이터는 남겨둡니다.")
    @PatchMapping("/status")
    public ResponseEntity<?> deactivate(@Valid @RequestBody ProfileDeactivateRequestDto requestDto, HttpServletResponse response){
        profileService.deactivateProfile(requestDto);

        //쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken",null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().build();
    }

}
