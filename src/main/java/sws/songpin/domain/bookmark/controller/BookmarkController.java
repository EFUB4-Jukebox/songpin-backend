package sws.songpin.domain.bookmark.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sws.songpin.domain.bookmark.dto.request.BookmarkAddRequestDto;
import sws.songpin.domain.bookmark.service.BookmarkService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Operation(summary = "북마크 생성", description = "플레이리스트에 북마크 생성")
    @PostMapping
    public ResponseEntity<?> createBookmark(@RequestBody @Valid BookmarkAddRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookmarkService.createBookmark(requestDto));
    }

    @Operation(summary = "북마크 삭제", description = "플레이리스트의 북마크 제거")
    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<?> deleteBookmark(@PathVariable("bookmarkId") final Long bookmarkId){
        bookmarkService.deleteBookmark(bookmarkId);
        return ResponseEntity.ok().build();
    }


}
