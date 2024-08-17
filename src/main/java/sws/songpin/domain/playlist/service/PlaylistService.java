package sws.songpin.domain.playlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.bookmark.entity.Bookmark;
import sws.songpin.domain.bookmark.repository.BookmarkRepository;
import sws.songpin.domain.follow.service.FollowService;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.follow.entity.Follow;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.playlist.dto.response.*;
import sws.songpin.domain.playlist.dto.request.PlaylistAddRequestDto;
import sws.songpin.domain.playlist.dto.request.PlaylistUpdateRequestDto;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlist.repository.PlaylistRepository;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.domain.playlistpin.repository.PlaylistPinRepository;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistPinRepository playlistPinRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberService memberService;
    private final FollowService followService;

    // 플레이리스트 생성
    public PlaylistAddResponseDto createPlaylist(PlaylistAddRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        Playlist playlist = requestDto.toEntity(currentMember);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        return PlaylistAddResponseDto.from(savedPlaylist);
    }

    @Transactional(readOnly = true)
    public Playlist findPlaylistById(Long playlistId){
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAYLIST_NOT_FOUND));
    }

    // 플레이리스트 상세 정보 가져오기
    @Transactional(readOnly = true)
    public PlaylistDetailsResponseDto getPlaylist(Long playlistId) {
        Member currentMember = memberService.getCurrentMemberOrNull();
        Playlist playlist = findPlaylistById(playlistId);
        // isMine
        boolean isMine = currentMember != null && currentMember.equals(playlist.getCreator());
        if (!isMine && playlist.getVisibility() == Visibility.PRIVATE) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        List<PlaylistPin> playlistPins = playlist.getPlaylistPins();
        // imgPathList, pinList
        List<PlaylistPinUnitDto> pinList = new ArrayList<>();
        List<String> imgPathList = new ArrayList<>();
        playlistPins.stream()
                .sorted(Comparator.comparingInt(PlaylistPin::getPinIndex).reversed())
                .forEach(playlistPin -> {
                    pinList.add(PlaylistPinUnitDto.from(playlistPin));
                    if (imgPathList.size() < 3) {
                        imgPathList.add(playlistPin.getPin().getSong().getImgPath());
                    }
                });
        // isBookmarked
        boolean isBookmarked = isPlaylistBookmarkedByMember(playlist, currentMember);
        return PlaylistDetailsResponseDto.from(playlist, imgPathList, pinList, isMine, isBookmarked);
    }

    // 플레이리스트 편집
    public void updatePlaylist(Long playlistId, PlaylistUpdateRequestDto requestDto) {
        Playlist playlist = findPlaylistById(playlistId);
        Member currentMember = memberService.getCurrentMember();
        if (!currentMember.equals(playlist.getCreator())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        // 플레이리스트 정보 수정
        playlist.updatePlaylistName(requestDto.playlistName());
        playlist.updateVisibility(requestDto.visibility());

        // 현재 핀 리스트 가져오기
        List<PlaylistPin> currentPins = playlist.getPlaylistPins();

        // 요청된 핀 리스트 가져오기
        List<Long> requestedPinIds = requestDto.pinList().stream()
                .map(PlaylistPinUpdateDto::playlistPinId)
                .toList();

        // 핀 삭제 & 순서 변경
        List<PlaylistPin> updatedPins = new ArrayList<>();
        List<PlaylistPin> pinsToDelete = new ArrayList<>();
        for (PlaylistPin currentPin : currentPins) {
            // 핀 삭제
            if (!requestedPinIds.contains(currentPin.getPlaylistPinId())) {
                pinsToDelete.add(currentPin);
            } else {
                // 핀 순서 변경
                PlaylistPinUpdateDto pinDto = requestDto.pinList().stream()
                        .filter(dto -> dto.playlistPinId().equals(currentPin.getPlaylistPinId()))
                        .findFirst()
                        .orElseThrow(() -> new CustomException(ErrorCode.PLAYLIST_PIN_NOT_FOUND));
                currentPin.updatePinIndex(pinDto.pinIndex());
                updatedPins.add(currentPin);
            }
        }
        for (PlaylistPin pin : pinsToDelete) {
            playlist.removePlaylistPin(pin);
        }
        playlistPinRepository.deleteAll(pinsToDelete);
        playlistPinRepository.saveAll(updatedPins);
    }

    // 플레이리스트 삭제
    public void deletePlaylist(Long playlistId) {
        Playlist playlist = findPlaylistById(playlistId);
        Member currentMember = memberService.getCurrentMember();
        if (!currentMember.equals(playlist.getCreator())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        playlistRepository.delete(playlist);
    }

    // 내 플레이리스트 조회
    @Transactional(readOnly = true)
    public PlaylistListResponseDto getMemberPlaylists(){
        Member currentMember = memberService.getCurrentMember();
        return getMemberPlaylists(currentMember, currentMember);
    }

    // 타 유저 플레이리스트 조회
    @Transactional(readOnly = true)
    public PlaylistListResponseDto getMemberPlaylists(String handle) {
        Member creator = memberService.getActiveMemberByHandle(handle);
        Member currentMember = memberService.getCurrentMember();
        return getMemberPlaylists(creator, currentMember);
    }

    @Transactional(readOnly = true)
    public PlaylistListResponseDto getMemberPlaylists(Member creator, Member currentMember) {
        List<Playlist> playlists;
        if (creator.equals(currentMember)) {
            playlists = playlistRepository.findAllByCreator(creator);
        } else {
            playlists = playlistRepository.findAllPublicByCreator(creator);
        }
        List<PlaylistUnitDto> playlistList = playlists.stream()
                .map(playlist -> convertToPlaylistUnitDto(playlist, currentMember))
                .collect(Collectors.toList());
        return PlaylistListResponseDto.from(playlistList);
    }

    // 플레이리스트 메인 페이지 조회
    @Transactional(readOnly = true)
    public PlaylistMainResponseDto getPlaylistMain() {
        Member currentMember = memberService.getCurrentMember();
        List<Playlist> allPlaylists = playlistRepository.findAll();

        // recentPlaylists
        List<PlaylistUnitDto> recentPlaylists = allPlaylists.stream()
                .filter(playlist -> playlist.getVisibility() == Visibility.PUBLIC || playlist.getCreator().equals(currentMember))
                .sorted((p1, p2) -> Long.compare(p2.getPlaylistId(), p1.getPlaylistId()))
                .limit(4)
                .map(playlist -> convertToPlaylistUnitDto(playlist, currentMember))
                .toList();

        // followingPlaylists
        List<Follow> followings = followService.findAllFollowingsOfMember(currentMember);
        List<Member> followingMembers = followings.stream()
                .map(Follow::getFollowing)
                .toList();
        List<PlaylistUnitDto> followingPlaylists = allPlaylists.stream()
                .filter(playlist -> followingMembers.contains(playlist.getCreator()) && playlist.getVisibility() == Visibility.PUBLIC)
                .sorted((p1, p2) -> Long.compare(p2.getPlaylistId(), p1.getPlaylistId()))
                .limit(4)
                .map(playlist -> convertToPlaylistUnitDto(playlist, currentMember))
                .toList();

        return PlaylistMainResponseDto.from(recentPlaylists, followingPlaylists);
    }

    // 플레이리스트 검색
    @Transactional(readOnly = true)
    public Object searchPlaylists(String keyword, SortBy sortBy, Pageable pageable) {
        String keywordNoSpaces = keyword.replace(" ", "");
        Page<Object[]> playlistPage;
        Member currentMember = memberService.getCurrentMember();
        Long currentMemberId = currentMember.getMemberId();
        switch (sortBy) {
            case COUNT -> playlistPage = playlistRepository.findAllByPlaylistNameContainingIgnoreSpacesOrderByCount(keywordNoSpaces, currentMemberId, pageable);
            case NEWEST -> playlistPage = playlistRepository.findAllByPlaylistNameContainingIgnoreSpacesOrderByNewest(keywordNoSpaces, currentMemberId, pageable);
            case ACCURACY -> playlistPage = playlistRepository.findAllByPlaylistNameContainingIgnoreSpacesOrderByAccuracy(keywordNoSpaces, currentMemberId, pageable);
            default -> throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
        // Page<Object[]>를 Page<PlaylistUnitDto>로 변환
        Page<PlaylistUnitDto> playlistUnitPage = playlistPage.map(objects -> {
            Long playlistId = ((Number) objects[0]).longValue();
            Playlist playlist = findPlaylistById(playlistId);
            return convertToPlaylistUnitDto(playlist, currentMember);
        });
        // PlaylistSearchResponseDto를 반환
        return PlaylistSearchResponseDto.from(playlistUnitPage);
    }

    // Playlist를 PlaylistUnitDto로 변환
    @Transactional(readOnly = true)
    public PlaylistUnitDto convertToPlaylistUnitDto(Playlist playlist, Member currentMember) {
        List<String> imgPathList = playlist.getPlaylistPins().stream()
                .sorted(Comparator.comparingInt(PlaylistPin::getPinIndex).reversed())
                .map(playlistPin -> playlistPin.getPin().getSong().getImgPath())
                .limit(3)
                .collect(Collectors.toList());
        boolean isBookmarked = isPlaylistBookmarkedByMember(playlist, currentMember);
        return PlaylistUnitDto.from(playlist, imgPathList, isBookmarked);
    }

    @Transactional(readOnly = true)
    public boolean isPlaylistBookmarkedByMember(Playlist playlist, Member member) {
        return bookmarkRepository.findByPlaylistAndMember(playlist, member).isPresent();
    }

    public void deleteAllPlaylistsOfMember(Member member) {
        playlistRepository.deleteAllByCreator(member);
    }
}
