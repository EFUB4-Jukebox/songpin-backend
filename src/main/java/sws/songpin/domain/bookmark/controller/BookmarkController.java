package sws.songpin.domain.bookmark.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.bookmark.dto.request.BookmarkRequestDto;
import sws.songpin.domain.bookmark.dto.response.BookmarkChangeResponseDto;
import sws.songpin.domain.bookmark.service.BookmarkService;

@Tag(name = "Bookmark", description = "Bookmark 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

//    @Operation(summary = "북마크 생성", description = "플레이리스트에 북마크 생성")
//    @PostMapping
//    public ResponseEntity<?> createBookmark(@RequestBody @Valid BookmarkRequestDto requestDto){
//        return ResponseEntity.status(HttpStatus.CREATED).body(bookmarkService.createBookmark(requestDto));
//    }
//
//    @Operation(summary = "북마크 삭제", description = "플레이리스트의 북마크 제거")
//    @DeleteMapping("/{bookmarkId}")
//    public ResponseEntity<?> deleteBookmark(@PathVariable("bookmarkId") final Long bookmarkId){
//        bookmarkService.deleteBookmark(bookmarkId);
//        return ResponseEntity.ok().build();
//    }

    @Operation(summary = "북마크 상태 변경", description = "북마크가 없으면 생성하고, 있으면 삭제")
    @PutMapping
    public ResponseEntity<?> changeBookmark(@RequestBody @Valid BookmarkRequestDto requestDto){
        BookmarkChangeResponseDto responseDto = bookmarkService.changeBookmark(requestDto);
        if (responseDto.isBookmarked()) {
            // 북마크 삭제
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } else {
            // 북마크 생성
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }
    }
}
