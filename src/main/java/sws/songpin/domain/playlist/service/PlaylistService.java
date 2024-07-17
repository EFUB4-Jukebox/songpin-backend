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
import sws.songpin.domain.song.dto.response.SongInfoDto;
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
    private final MemberService memberService;
    private final PlaylistRepository playlistRepository;
    private final PlaylistPinRepository playlistPinRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FollowService followService;

    // 플레이리스트 생성
    public PlaylistAddResponseDto createPlaylist(PlaylistAddRequestDto requestDto) {
        Member member = memberService.getCurrentMember();
        Playlist playlist = requestDto.toEntity(member);
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
        Member currentMember = memberService.getCurrentMember();
        Playlist playlist = findPlaylistById(playlistId);
        List<PlaylistPin> playlistPinList = playlist.getPlaylistPins();
        // imgPathList, pinList
        List<PlaylistPinUnitDto> pinList = new ArrayList<>();
        List<String> imgPathList = new ArrayList<>();

        playlistPinList.stream()
                .sorted(Comparator.comparingInt(PlaylistPin::getPinIndex).reversed())
                .forEach(playlistPin -> {
                    // SongInfo
                    SongInfoDto songInfo = SongInfoDto.from(playlistPin.getPin().getSong());
                    PlaylistPinUnitDto pinListDto = new PlaylistPinUnitDto(
                            playlistPin.getPlaylistPinId(),
                            playlistPin.getPin().getPinId(),
                            songInfo,
                            playlistPin.getPin().getListenedDate(),
                            playlistPin.getPin().getPlace().getPlaceName(),
                            playlistPin.getPin().getPlace().getLatitude(),
                            playlistPin.getPin().getPlace().getLongitude(),
                            playlistPin.getPin().getGenre().getGenreName(),
                            playlistPin.getPinIndex()
                    );
                    pinList.add(pinListDto);

                    if (imgPathList.size() < 3) {
                        imgPathList.add(playlistPin.getPin().getSong().getImgPath());
                    }
                });
        // isMine
        boolean isMine = playlist.getCreator().equals(currentMember);
        // bookmarkId
        Long bookmarkId = bookmarkRepository.findByPlaylistAndMember(playlist, currentMember)
                .map(Bookmark::getBookmarkId)
                .orElse(null);
        return PlaylistDetailsResponseDto.from(playlist, imgPathList, pinList, isMine, bookmarkId);
    }

    // 플레이리스트 편집
    public void updatePlaylist(Long playlistId, PlaylistUpdateRequestDto requestDto) {
        Playlist playlist = findPlaylistById(playlistId);
        // 플레이리스트 정보 수정
        playlist.updatePlaylistName(requestDto.playlistName());
        playlist.updateVisibility(requestDto.visibility());

        // 현재 핀 리스트 가져오기
        List<PlaylistPin> currentPins = playlist.getPlaylistPins();

        // 요청된 핀 리스트 가져오기
        List<Long> requestedPinIds = requestDto.pinList().stream()
                .map(PlaylistPinUpdateDto::playlistPinId)
                .collect(Collectors.toList());

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
        if(!currentMember.equals(playlist.getCreator())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        playlistRepository.delete(playlist);
    }

    // 내 플레이리스트 조회
    @Transactional(readOnly = true)
    public PlaylistListResponseDto getAllPlaylists(){
        Member currentMember = memberService.getCurrentMember();
        return getAllPlaylists(currentMember, currentMember);
    }

    // 타 유저 플레이리스트 조회
    @Transactional(readOnly = true)
    public PlaylistListResponseDto getAllPlaylists(Long memberId) {
        Member creator = memberService.getMemberById(memberId);
        Member currentMember = memberService.getCurrentMember();
        return getAllPlaylists(creator, currentMember);
    }

    @Transactional(readOnly = true)
    public PlaylistListResponseDto getAllPlaylists(Member creator, Member currentMember) {
        Boolean isMine = creator.equals(currentMember)? true : false;
        List<Playlist> playlists = playlistRepository.findAllByCreator(creator);
        // 플레이리스트 ID 목록 생성
        List<Long> playlistIds = playlists.stream()
                .map(Playlist::getPlaylistId)
                .collect(Collectors.toList());

        // 내가 북마크한 플레이리스트 ID 조회
        List<Long> bookmarkedPlaylistIds = bookmarkRepository.findBookmarkedPlaylistIdsByMemberAndPlaylistIds(currentMember, playlistIds);

        List<PlaylistUnitDto> playlistList = playlists.stream()
                .filter(playlist -> isMine || playlist.getVisibility().equals(Visibility.PUBLIC))
                .map(playlist -> {
                    // imgPathList
                    List<String> imgPathList = getPlaylistThumbnailImgPathList(playlist);
                    // isBookmarked
                    boolean isBookmarked = bookmarkedPlaylistIds.contains(playlist.getPlaylistId());
                    return PlaylistUnitDto.from(playlist, imgPathList, isBookmarked);
                }).collect(Collectors.toList());

        return PlaylistListResponseDto.from(playlistList);
    }

    // imgPathList
    @Transactional(readOnly = true)
    public List<String> getPlaylistThumbnailImgPathList(Playlist playlist) {
        return playlist.getPlaylistPins().stream()
                .map(playlistPin -> playlistPin.getPin().getSong().getImgPath())
                .limit(3)
                .collect(Collectors.toList());
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
                .map(playlist -> {
                    List<String> imgPathList = getPlaylistThumbnailImgPathList(playlist);
                    boolean isBookmarked = bookmarkRepository.existsByPlaylistAndMember(playlist, currentMember);
                    return PlaylistUnitDto.from(playlist, imgPathList, isBookmarked);
                })
                .collect(Collectors.toList());

        // followingPlaylists
        List<Follow> followings = followService.findAllFollowingsOfMember(currentMember);
        List<Member> followingMembers = followings.stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());
        List<PlaylistUnitDto> followingPlaylists = allPlaylists.stream()
                .filter(playlist -> followingMembers.contains(playlist.getCreator()) && playlist.getVisibility() == Visibility.PUBLIC)
                .sorted((p1, p2) -> Long.compare(p2.getPlaylistId(), p1.getPlaylistId()))
                .limit(4)
                .map(playlist -> {
                    List<String> imgPathList = getPlaylistThumbnailImgPathList(playlist);
                    boolean isBookmarked = bookmarkRepository.existsByPlaylistAndMember(playlist, currentMember);
                    return PlaylistUnitDto.from(playlist, imgPathList, isBookmarked);
                })
                .collect(Collectors.toList());

        return PlaylistMainResponseDto.from(recentPlaylists, followingPlaylists);
    }

    // 플레이리스트 검색
    @Transactional(readOnly = true)
    public Object searchPlaylists(String keyword, SortBy sortBy, Pageable pageable) {
        String keywordNoSpaces = keyword.replace(" ", "");
        Page<Object[]> playlistPage;
        switch (sortBy) {
            case COUNT -> playlistPage = playlistRepository.findAllByPlaylistNameContainingIgnoreSpacesOrderByCount(keywordNoSpaces, pageable);
            case NEWEST -> playlistPage = playlistRepository.findAllByPlaylistNameContainingIgnoreSpacesOrderByNewest(keywordNoSpaces, pageable);
            case ACCURACY -> playlistPage = playlistRepository.findAllByPlaylistNameContainingIgnoreSpacesOrderByAccuracy(keywordNoSpaces, pageable);
            default -> throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
        // Page<Object[]>를 Page<PlaylistUnitDto>로 변환
        Page<PlaylistUnitDto> playlistUnitPage = playlistPage.map(objects -> {
            Member currentMember = memberService.getCurrentMember();
            Long playlistId = ((Number) objects[0]).longValue();
            Playlist playlist = findPlaylistById(playlistId);
            List<String> imgPathList = getPlaylistThumbnailImgPathList(playlist);
            boolean isBookmarked = bookmarkRepository.existsByPlaylistAndMember(playlist, currentMember);
            return PlaylistUnitDto.from(playlist, imgPathList, isBookmarked);
        });
        // PlaylistSearchResponseDto를 반환
        return PlaylistSearchResponseDto.from(playlistUnitPage);
    }
}
