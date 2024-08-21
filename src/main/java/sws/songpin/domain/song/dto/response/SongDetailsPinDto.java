package sws.songpin.domain.song.dto.response;

import sws.songpin.domain.genre.entity.GenreName;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.member.entity.Status;
import sws.songpin.domain.pin.dto.response.PinBasicUnitDto;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.song.entity.Song;

import java.time.LocalDate;

public record SongDetailsPinDto(
        Long pinId,
        String creatorHandle,
        String creatorNickname,
        Status creatorStatus,
        LocalDate listenedDate,
        String memo,
        Visibility visibility,
        String placeName,
        double latitude,
        double longitude,
        Boolean isMine
) {
    public static SongDetailsPinDto from(Pin pin, Member creator, Place place, String memo, Boolean isMine) {
        return new SongDetailsPinDto(
                pin.getPinId(),
                creator.getHandle(),
                creator.getNickname(),
                creator.getStatus(),
                pin.getListenedDate(),
                memo,
                pin.getVisibility(),
                place.getPlaceName(),
                place.getLatitude( ),
                place.getLongitude(),
                isMine
        );
    }
}
