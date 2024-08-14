package sws.songpin.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.bookmark.dto.request.BookmarkRequestDto;
import sws.songpin.domain.bookmark.dto.response.BookmarkChangeResponseDto;
import sws.songpin.domain.bookmark.entity.Bookmark;
import sws.songpin.domain.bookmark.repository.BookmarkRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.bookmark.dto.response.BookmarkListResponseDto;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.playlist.dto.response.PlaylistUnitDto;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlist.service.PlaylistService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final MemberService memberService;
    private final PlaylistService playlistService;

//    // 북마크 생성
//    public BookmarkAddResponseDto createBookmark(BookmarkAddRequestDto requestDto) {
//        Member member = memberService.getCurrentMember();
//        Playlist playlist = playlistService.findPlaylistById(requestDto.playlistId());
//        if (!member.equals(playlist.getCreator()) && playlist.getVisibility() == Visibility.PRIVATE) {
//            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
//        }
//        Bookmark bookmark = requestDto.toEntity(member, playlist);
//        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
//        return BookmarkAddResponseDto.from(savedBookmark);
//    }
//
//    // 북마크 삭제
//    public void deleteBookmark(Long bookmarkId) {
//        Bookmark bookmark = findBookmarkById(bookmarkId);
//        Member currentMember = memberService.getCurrentMember();
//        if (!bookmark.getMember().equals(currentMember)) {
//            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
//        }
//        bookmarkRepository.delete(bookmark);
//    }

    // 북마크 상태 변경
    public BookmarkChangeResponseDto changeBookmark(BookmarkRequestDto requestDto) {
        Member member = memberService.getCurrentMember();
        Playlist playlist = playlistService.findPlaylistById(requestDto.playlistId());
        if (!member.equals(playlist.getCreator()) && playlist.getVisibility() == Visibility.PRIVATE) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        Optional<Bookmark> bookmark = getBookmarkByPlaylistAndMember(playlist, member);
        if(bookmark.isPresent()){
            bookmarkRepository.delete(bookmark.get());
            return BookmarkChangeResponseDto.from(true, null);
        }
        else{
            Bookmark newbookmark = requestDto.toEntity(member, playlist);
            Bookmark savedBookmark = bookmarkRepository.save(newbookmark);
            return BookmarkChangeResponseDto.from(false, savedBookmark);
        }
    }

    // 내 모든 북마크된 플레이리스트 조회
    @Transactional(readOnly = true)
    public BookmarkListResponseDto getAllBookmarks() {
        Member currentMember = memberService.getCurrentMember();
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMember(currentMember);
        List<PlaylistUnitDto> bookmarkList = bookmarks.stream()
                .map(bookmark -> bookmark.getPlaylist())
                .filter(playlist -> playlist.getVisibility() == Visibility.PUBLIC || playlist.getCreator().equals(currentMember))
                .map(playlist -> playlistService.convertToPlaylistUnitDto(playlist, currentMember))
                .collect(Collectors.toList());
        return BookmarkListResponseDto.from(bookmarkList);
    }

    @Transactional(readOnly = true)
    public Optional<Bookmark> getBookmarkByPlaylistAndMember(Playlist playlist, Member member) {
        return bookmarkRepository.findByPlaylistAndMember(playlist, member);
    }

    @Transactional(readOnly = true)
    public Bookmark findBookmarkById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));
    }

    public void deleteAllBookmarksOfMember(Member member){
        bookmarkRepository.deleteAllByMember(member);
    }
}