package sws.songpin.domain.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.song.entity.Song;

import java.time.LocalDate;
import java.util.List;

public interface PinRepository extends JpaRepository <Pin, Long> {
    List<Pin> findAllBySong(Song song);

    // 메서드명 너무 길어서 수정할 방법이 있는지 찾고싶음
    boolean existsByMemberAndSongAndPlaceAndListenedDate(Member member, Song song, Place place, LocalDate listenedDate);
}
