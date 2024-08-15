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

    // 북마크 상태 변경
    public boolean changeBookmark(BookmarkRequestDto requestDto) {
        Member member = memberService.getCurrentMember();
        Playlist playlist = playlistService.findPlaylistById(requestDto.playlistId());
        if (!member.equals(playlist.getCreator()) && playlist.getVisibility() == Visibility.PRIVATE) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        Optional<Bookmark> bookmark = getBookmarkByPlaylistAndMember(playlist, member);
        if(bookmark.isPresent()){
            bookmark.ifPresent(bookmarkRepository::delete);
            return false;
        }
        else{
            Bookmark newBookmark = Bookmark.builder()
                    .member(member)
                    .playlist(playlist)
                    .build();
            bookmarkRepository.save(newBookmark);
            return true;
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

    public void deleteAllBookmarksOfMember(Member member){
        bookmarkRepository.deleteAllByMember(member);
    }
}