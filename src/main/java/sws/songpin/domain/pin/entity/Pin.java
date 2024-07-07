package sws.songpin.domain.pin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.song.entity.Song;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.place.entity.Place;
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

    @Column(name = "listened_date")
    @NotNull
    private LocalDate listenedDate;

    @Column(name = "memo", length = 200)
    @NotNull
    private String memo;

    @Column(name = "visibility", length = 10)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    @NotNull
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", updatable = false)
    @NotNull
    private Song song;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", updatable = false)
    @NotNull
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    @NotNull
    private Genre genre;

    @Builder
    public Pin(Long pinId, LocalDate listenedDate, String memo, Visibility visibility,
               Member member, Song song, Place place, Genre genre) {
        this.pinId = pinId;
        this.listenedDate = listenedDate;
        this.memo = memo;
        this.visibility = visibility;
        this.member = member;
        this.song = song;
        this.place = place;
        this.genre = genre;
    }

}
