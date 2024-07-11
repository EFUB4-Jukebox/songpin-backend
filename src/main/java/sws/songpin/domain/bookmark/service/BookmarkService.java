package sws.songpin.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.bookmark.dto.request.BookmarkRequestDto;
import sws.songpin.domain.bookmark.dto.response.BookmarkResponseDto;
import sws.songpin.domain.bookmark.entity.Bookmark;
import sws.songpin.domain.bookmark.repository.BookmarkRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlist.repository.PlaylistRepository;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final MemberService memberService;
    private final PlaylistRepository playlistRepository;
    private final BookmarkRepository bookmarkRepository;

    // 북마크 생성
    public BookmarkResponseDto createBookmark(BookmarkRequestDto requestDto) {
        Member member = memberService.getCurrentMember();
        Playlist playlist = playlistRepository.findById(requestDto.playlistId())
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYLIST_NOT_FOUND));
        getBookmarkByPlaylistAndMember(playlist, member).ifPresent(bookmark -> {
            throw new CustomException(ErrorCode.BOOKMARK_ALREADY_EXISTS);
        });
        Bookmark bookmark = requestDto.toEntity(member, playlist);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        return BookmarkResponseDto.from(savedBookmark);
    }

    // 북마크 삭제
    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));
        Member currentMember = memberService.getCurrentMember();
        if (!bookmark.getMember().equals(currentMember)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public Optional<Bookmark> getBookmarkByPlaylistAndMember(Playlist playlist, Member member) {
        return bookmarkRepository.findByPlaylistAndMember(playlist, member);
    }
}
