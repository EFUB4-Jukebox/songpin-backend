package sws.songpin.entity.pin.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.member.domain.Member;
import sws.songpin.entity.song.domain.Song;
import sws.songpin.entity.genre.domain.Genre;
import sws.songpin.entity.place.domain.Place;
import sws.songpin.global.BaseTimeEntity;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Pin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pin_id", updatable = false)
    private Long pinId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "listened_date", nullable = false)
    private LocalDate listenedDate;

    @Column(name = "memo", length = 200)
    private String memo;

    @Column(name = "visibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Builder
    public Pin(Long pinId, Member member, Song song, Genre genre, Place place, LocalDate listenedDate, String memo, Visibility visibility) {
        this.pinId = pinId;
        this.member = member;
        this.song = song;
        this.genre = genre;
        this.place = place;
        this.listenedDate = listenedDate;
        this.memo = memo;
        this.visibility = visibility;
    }

}
