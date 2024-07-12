package sws.songpin.domain.pin.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.pin.entity.Visibility;
import sws.songpin.domain.place.dto.request.PlaceAddRequestDto;
import sws.songpin.domain.song.dto.request.SongAddRequestDto;

import java.time.LocalDate;

public record PinAddRequestDto(
        @NotNull SongAddRequestDto song,
        @NotNull LocalDate listenedDate,
        @NotNull PlaceAddRequestDto place,
        @NotNull GenreName genreName,
        @NotNull @Size(max = 200, message = "핀의 메모 길이는 200자 이내여야 합니다.") String memo,
        @NotNull Visibility visibility
) {}

