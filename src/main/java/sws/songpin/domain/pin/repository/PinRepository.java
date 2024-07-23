package sws.songpin.domain.pin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.pin.entity.Pin;
import sws.songpin.domain.place.entity.Place;
import sws.songpin.domain.song.entity.Song;

import java.util.List;
import java.util.Optional;

public interface PinRepository extends JpaRepository <Pin, Long> {
    List<Pin> findAllBySong(Song song);
    List<Pin> findAllBySongAndMember(Song song, Member member);
    List<Pin> findAllByMemberOrderByListenedDateDesc(Member member);
    List<Pin> findAllByMemberAndVisibilityOrderByListenedDateDesc(Member member, Visibility visibility);
    Optional<Pin> findTopBySongAndMemberOrderByListenedDateDesc(Song song, Member member);
    int countBySong(Song song);
    List<Pin> findAllByPlace(Place place);
    @Query("SELECT p FROM Pin p WHERE p.member = :member AND YEAR(p.listenedDate) = :year AND MONTH(p.listenedDate) = :month")
    List<Pin> findAllByMemberAndDate(@Param("member") Member member, @Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT * FROM pin ORDER BY pin_id DESC LIMIT 3", nativeQuery = true)
    List<Pin> findTop3ByOrderByPinIdDesc();
}
