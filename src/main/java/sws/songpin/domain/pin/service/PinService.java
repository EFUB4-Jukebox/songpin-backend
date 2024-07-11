package sws.songpin.domain.pin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sws.songpin.domain.pin.dto.request.PinRequestDto;
import sws.songpin.domain.pin.dto.request.PinUpdateRequestDto;
import sws.songpin.domain.pin.dto.response.PinResponseDto;
import sws.songpin.domain.pin.repository.PinRepository;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.dto.request.PlaceRequestDto;
import sws.songpin.domain.song.dto.request.SongRequestDto;
import sws.songpin.domain.song.dto.response.SongDetailResponseDto;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.service.MemberService;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.place.service.PlaceService;
import sws.songpin.domain.song.repository.SongRepository;
import sws.songpin.domain.song.service.SongService;
import sws.songpin.domain.genre.service.GenreService;
import sws.songpin.domain.genre.entity.GenreName;
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
    private final MemberService memberService;
    private final SongService songService;
    private final PlaceService placeService;
    private final GenreService genreService;

    // 음악 핀 생성 - 노래, 장소가 없다면 추가하기
    public SongDetailResponseDto createPin(PinRequestDto pinRequestDto) {
        Member member = memberService.getCurrentMember();
        Song finalSong = getOrCreateSong(pinRequestDto.song());
        Place finalPlace = getOrCreatePlace(pinRequestDto.place());
        Genre genre = genreService.getGenreByGenreName(pinRequestDto.genreName());

        Pin pin = Pin.builder()
                .listenedDate(pinRequestDto.listenedDate())
                .memo(pinRequestDto.memo())
                .visibility(pinRequestDto.visibility())
                .member(member)
                .song(finalSong)
                .place(finalPlace)
                .genre(genre)
                .build();

        pin = pinRepository.save(pin);
        updateSongAvgGenreName(finalSong);

        List<PinResponseDto> pinResponseDtos = getPinResponseDtosForSong(finalSong);

        // 노래 상세정보 페이지로 이동
        return getSongDetailPage(finalSong);
    }

    private Song getOrCreateSong(SongRequestDto songRequestDto) {
        return songService.getSongByProviderTrackCode(songRequestDto.getProviderTrackCode())
                .orElseGet(() -> songService.createSong(songRequestDto));
    }

    private Place getOrCreatePlace(PlaceRequestDto placeRequestDto) {
        return placeService.getPlaceByProviderAddressId(placeRequestDto.providerAddressId())
                .orElseGet(() -> placeService.createPlace(placeRequestDto));
    }

    private void updateSongAvgGenreName(Song song) {
        List<Genre> genres = pinRepository.findAllBySong(song).stream()
                .map(Pin::getGenre)
                .collect(Collectors.toList());

        Optional<GenreName> avgGenreName = songService.calculateAvgGenreName(genres);
        avgGenreName.ifPresent(song::setAvgGenreName);
        songRepository.save(song);
    }

    @Transactional(readOnly = true)
    public List<PinResponseDto> getPinResponseDtosForSong(Song song) {
        List<Pin> pins = pinRepository.findAllBySong(song);
        return pins.stream()
                .map(PinResponseDto::from)
                .collect(Collectors.toList());
    }

    private SongDetailResponseDto getSongDetailPage(Song song) {
        List<PinResponseDto> pinResponseDtos = getPinResponseDtosForSong(song);
        return new SongDetailResponseDto(
                song.getSongId(),
                song.getTitle(),
                song.getArtist(),
                song.getImgPath(),
                song.getAvgGenreName(),
                pinResponseDtos.size(),
                pinResponseDtos
        );
    }

    @Transactional(readOnly = true)
    public Optional<Pin> getPinById(Long pinId) {
        return pinRepository.findById(pinId);
    }

    // 음악 핀 수정
    public SongDetailResponseDto updatePin(Long pinId, PinUpdateRequestDto pinUpdateRequestDto) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(() -> new CustomException(ErrorCode.PIN_NOT_FOUND));

        // 현재 로그인된 사용자가 핀의 생성자인지 확인
        Member currentMember = memberService.getCurrentMember();
        if (!pin.getMember().getMemberId().equals(currentMember.getMemberId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        Genre genre = genreService.getGenreByGenreName(pinUpdateRequestDto.genreName());
        pin.updatePin(pinUpdateRequestDto.listenedDate(), pinUpdateRequestDto.memo(), pinUpdateRequestDto.visibility(), genre);
        pinRepository.save(pin);

        return getSongDetailPage(pin.getSong());
    }
}
