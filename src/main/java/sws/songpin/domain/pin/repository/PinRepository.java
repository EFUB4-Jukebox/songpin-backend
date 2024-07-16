package sws.songpin.domain.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.song.entity.Song;

import java.util.List;

public interface PinRepository extends JpaRepository <Pin, Long> {
    List<Pin> findAllBySong(Song song);
    List<Pin> findAllByMember(Member member);
    List<Pin> findAllBySongAndMember(Song song, Member member);
    List<Pin> findAllBySongAndMemberAndVisibility(Song song, Member member, Visibility visibility);
    List<Pin> findAllBySongAndVisibility(Song song, Visibility visibility);
    List<Pin> findAllByMemberAndVisibility(Member member, Visibility visibility);
    int countBySong(Song song);
    List<Pin> findAllByPlace(Place place);
}
