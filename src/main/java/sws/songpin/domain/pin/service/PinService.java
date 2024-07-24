package sws.songpin.domain.pin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.genre.service.GenreService;
import sws.songpin.domain.member.dto.response.MyPinSearchResponseDto;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.model.SortBy;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.dto.request.PinAddRequestDto;
import sws.songpin.domain.pin.dto.request.PinUpdateRequestDto;
import sws.songpin.domain.pin.dto.response.PinBasicListResponseDto;
import sws.songpin.domain.pin.dto.response.PinBasicUnitDto;
import sws.songpin.domain.pin.dto.response.PinFeedListResponseDto;
import sws.songpin.domain.pin.dto.response.PinFeedUnitDto;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.service.PlaceService;
import sws.songpin.domain.playlist.entity.Playlist;
import sws.songpin.domain.playlistpin.entity.PlaylistPin;
import sws.songpin.domain.playlistpin.repository.PlaylistPinRepository;
import sws.songpin.domain.song.dto.response.*;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.song.service.SongService;
import sws.songpin.global.exception.CustomException;
import sws.songpin.global.exception.ErrorCode;

import java.time.LocalDate;
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
        updateSongAvgGenreName(pin.getSong());
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
        updateSongAvgGenreName(pin.getSong());
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
    public SongDetailsPinListResponseDto getPinsForSong(Long songId, boolean onlyMyPins, Pageable pageable) {
        Song song = songService.getSongById(songId);
        Member currentMember = memberService.getCurrentMember();
        Long currentMemberId = currentMember != null ? currentMember.getMemberId() : null;
        Page<Pin> pinPage;

        if (onlyMyPins) {
            // 내 핀만 보기 - 현재 사용자의 모든 핀 가져오기(visibility 상관없음)
            pinPage = pinRepository.findAllBySongAndMember(song, currentMember, pageable);
        } else {
            // 전체 핀 보기 - 내 핀 가져오기(visibility 상관없음) + 타유저의 공개 핀 가져오기
            pinPage = pinRepository.findPinFeed(song, currentMember, Visibility.PUBLIC, pageable);
        }
        Page<SongDetailsPinDto> songDetailsPinPage = pinPage.map(pin -> {
            Boolean isMine = pin.getMember().getMemberId().equals(currentMemberId);
            return SongDetailsPinDto.from(pin, isMine);
        });
        return SongDetailsPinListResponseDto.from(songDetailsPinPage);
    }

    // 타 유저의 공개 핀 피드 조회
    @Transactional(readOnly = true)
    public PinFeedListResponseDto getPublicPinFeed(Long memberId, Pageable pageable) {
        Member targetMember = memberService.getMemberById(memberId);
        Page<Pin> pinPage = pinRepository.findAllByMemberAndVisibilityOrderByListenedDateDesc(targetMember, Visibility.PUBLIC, pageable);
        return getPinFeedResponse(pinPage, false);
    }

    // 내 핀 피드 조회
    @Transactional(readOnly = true)
    public PinFeedListResponseDto getMyPinFeed(Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<Pin> pinPage = pinRepository.findAllByMemberOrderByListenedDateDesc(currentMember, pageable);
        return getPinFeedResponse(pinPage, true);
    }

    // 핀피드 조회 공통 메서드
    @Transactional(readOnly = true)
    private PinFeedListResponseDto getPinFeedResponse(Page<Pin> pinPage, boolean isMine) {
        List<PinFeedUnitDto> feedPinList = pinPage.stream()
                .map(pin -> PinFeedUnitDto.from(pin, isMine))
                .collect(Collectors.toList());
        return new PinFeedListResponseDto((int)pinPage.getTotalElements(), feedPinList);
    }

    // 마이페이지 캘린더
    @Transactional(readOnly = true)
    public PinBasicListResponseDto getMyPinFeedForMonth(int year, int month) {
        Member currentMember = memberService.getCurrentMember();
        List<Pin> pins = pinRepository.findAllByMemberAndDate(currentMember,year, month);
        List<PinBasicUnitDto> pinList = pins.stream()
                .map(pin -> PinBasicUnitDto.from(pin, true))
                .collect(Collectors.toList());
        return new PinBasicListResponseDto(pinList, pinList.size());
    }

    // 마이페이지에서 내 핀 검색
    @Transactional(readOnly = true)
    public MyPinSearchResponseDto searchMyPins(String keyword, Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Long currentMemberId = currentMember.getMemberId();
        String keywordNoSpaces = keyword.replace(" ", "");
        Page<Object[]> myPinPage = pinRepository.findAllBySongNameOrArtistContainingIgnoreSpaces(currentMemberId, keywordNoSpaces, pageable);

        Page<PinBasicUnitDto> myPinUnitPage = myPinPage.map(objects -> {
            Long pinId = ((Number) objects[0]).longValue();
            SongInfoDto songInfo = new SongInfoDto(
                    ((Number) objects[1]).longValue(),
                    (String) objects[2],
                    (String) objects[3],
                    (String) objects[4]
            );
            LocalDate listenedDate = ((java.sql.Date) objects[5]).toLocalDate();
            String placeName = (String) objects[6];
            double latitude = ((Number) objects[7]).doubleValue();
            double longitude = ((Number) objects[8]).doubleValue();
            GenreName genreName = GenreName.valueOf((String) objects[9]);
            Long creatorId = ((Number) objects[10]).longValue();
            Boolean isMine = creatorId.equals(currentMemberId);

            return new PinBasicUnitDto(pinId, songInfo, listenedDate, placeName, latitude, longitude, genreName, isMine);
        });

        return MyPinSearchResponseDto.from(myPinUnitPage);
    }

    @Transactional(readOnly = true)
    public Pin getPinById(Long pinId) {
        return pinRepository.findById(pinId)
                .orElseThrow(() -> new CustomException(ErrorCode.PIN_NOT_FOUND));
    }

}
