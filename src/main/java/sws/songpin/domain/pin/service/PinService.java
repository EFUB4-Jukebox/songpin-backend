package sws.songpin.domain.pin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.genre.service.GenreService;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.dto.request.PinAddRequestDto;
import sws.songpin.domain.pin.dto.request.PinUpdateRequestDto;
import sws.songpin.domain.pin.dto.response.PinFeedListResponseDto;
import sws.songpin.domain.pin.dto.response.PinFeedUnitDto;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.service.PlaceService;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.domain.playlistpin.repository.PlaylistPinRepository;
import sws.songpin.domain.song.dto.response.SongDetailsPinDto;
import sws.songpin.domain.song.dto.response.SongDetailsPinListResponseDto;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.song.service.SongService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PinService {
    private final PinRepository pinRepository;
    private final SongRepository songRepository;
    private final PlaylistPinRepository playlistPinRepository;
    private final MemberService memberService;
    private final SongService songService;
    private final PlaceService placeService;
    private final GenreService genreService;

    // 음악 핀 생성 - 노래, 장소가 없다면 추가하기
    public Long createPin(PinAddRequestDto pinAddRequestDto) {
        Member member = memberService.getCurrentMember();
        Song finalSong = songService.getOrCreateSong(pinAddRequestDto.song());
        Place finalPlace = placeService.getOrCreatePlace(pinAddRequestDto.place());
        Genre genre = genreService.getGenreByGenreName(pinAddRequestDto.genreName());

        Pin pin = Pin.builder()
                .listenedDate(pinAddRequestDto.listenedDate())
                .memo(pinAddRequestDto.memo())
                .visibility(pinAddRequestDto.visibility())
                .member(member)
                .song(finalSong)
                .place(finalPlace)
                .genre(genre)
                .build();
        pinRepository.save(pin);
        updateSongAvgGenreName(finalSong);

        // 노래 상세정보 페이지로 이동
        return finalSong.getSongId();
    }

    // 음악 핀 수정
    public Long updatePin(Long pinId, PinUpdateRequestDto pinUpdateRequestDto) {
        Pin pin = validatePinCreator(pinId);
        Genre genre = genreService.getGenreByGenreName(pinUpdateRequestDto.genreName());
        pin.updatePin(pinUpdateRequestDto.listenedDate(), pinUpdateRequestDto.memo(), pinUpdateRequestDto.visibility(), genre);
        pinRepository.save(pin);

        return pin.getSong().getSongId();
    }

    // 음악 핀 삭제
    public void deletePin(Long pinId) {
        Pin pin = validatePinCreator(pinId);
        List<PlaylistPin> playlistPins = playlistPinRepository.findAllByPin(pin);
        for (PlaylistPin playlistPin : playlistPins) {
            Playlist playlist = playlistPin.getPlaylist();
            playlist.removePlaylistPin(playlistPin);
        }
        playlistPinRepository.deleteAll(playlistPins);
        pinRepository.delete(pin);
    }

    public void updateSongAvgGenreName(Song song) {
        List<Genre> genres = pinRepository.findAllBySong(song).stream()
                .map(Pin::getGenre)
                .collect(Collectors.toList());
        Optional<GenreName> avgGenreName = songService.calculateAvgGenreName(genres);
        avgGenreName.ifPresent(song::setAvgGenreName);
        songRepository.save(song);
    }

    // 현재 로그인된 사용자가 핀의 생성자인지 확인
    @Transactional(readOnly = true)
    public Pin validatePinCreator(Long pinId) {
        Pin pin = getPinById(pinId);
        Member currentMember = memberService.getCurrentMember();
        if (!pin.getMember().getMemberId().equals(currentMember.getMemberId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        return pin;
    }

    // 특정 노래에 대한 핀 조회
    @Transactional(readOnly = true)
    public SongDetailsPinListResponseDto getPinsForSong(Long songId, boolean onlyMyPins) {
        Song song = songService.getSongById(songId);
        Member currentMember = memberService.getCurrentMember();
        Long currentMemberId = currentMember != null ? currentMember.getMemberId() : null;
        List<Pin> pins;
        List<SongDetailsPinDto> songDetailsPinList;

        if (onlyMyPins) {
            // 내 핀만 보기 - 현재 사용자의 모든 핀 가져오기(visibility 상관없음)
            pins = pinRepository.findAllBySongAndMember(song, currentMember);
            songDetailsPinList = pins.stream()
                    .map(pin -> SongDetailsPinDto.from(pin , currentMemberId))
                    .collect(Collectors.toList());
        } else {
            // 전체 핀 보기
            pins = pinRepository.findAllBySong(song);
            songDetailsPinList = pins.stream()
                    .filter(pin -> pin.getVisibility() == Visibility.PUBLIC ||
                            (pin.getMember().equals(currentMember) && pin.getVisibility() == Visibility.PRIVATE))
                    .map(pin -> SongDetailsPinDto.from(pin, currentMemberId))
                    .collect(Collectors.toList());
        }
        return SongDetailsPinListResponseDto.from(songDetailsPinList);
    }

    // 타 유저의 공개 핀 피드 조회
    @Transactional(readOnly = true)
    public PinFeedListResponseDto getPublicFeedPins(Long memberId) {
        Member targetMember = memberService.getMemberById(memberId);
        List<Pin> pins = pinRepository.findAllByMemberAndVisibility(targetMember, Visibility.PUBLIC);
        return getFeedPinsResponse(pins, false);
    }

    // 내 핀 피드 조회
    @Transactional(readOnly = true)
    public PinFeedListResponseDto getMyFeedPins() {
        Member currentMember = memberService.getCurrentMember();
        List<Pin> pins = pinRepository.findAllByMember(currentMember);
        return getFeedPinsResponse(pins, true);
    }

    // 내 피드 월별로 조회
    @Transactional(readOnly = true)
    public PinFeedListResponseDto getMyFeedPinsForMonth(int year, int month) {
        Member currentMember = memberService.getCurrentMember();
        List<Pin> pins = pinRepository.findAllByMemberAndDate(currentMember, year, month);
        return getFeedPinsResponse(pins, true);
    }

    // 피드 조회 공통 메서드
    @Transactional(readOnly = true)
    private PinFeedListResponseDto getFeedPinsResponse(List<Pin> pins, boolean isMine) {
        List<PinFeedUnitDto> feedPinList = pins.stream()
                .map(pin -> PinFeedUnitDto.from(pin, isMine))
                .collect(Collectors.toList());
        return new PinFeedListResponseDto(feedPinList.size(), feedPinList);
    }

    @Transactional(readOnly = true)
    public Pin getPinById(Long pinId) {
        return pinRepository.findById(pinId)
                .orElseThrow(() -> new CustomException(ErrorCode.PIN_NOT_FOUND));
    }

}
