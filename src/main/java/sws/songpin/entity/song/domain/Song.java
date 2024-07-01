package sws.songpin.entity.song.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.genre.domain.Genre;
import sws.songpin.entity.songGenre.domain.SongGenre;
import sws.songpin.entity.pin.domain.Pin;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id", updatable = false)
    private Long songId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Column(name = "img_path")
    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avg_genre")
    private Genre avgGenre;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pin> pins;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SongGenre> songGenres = new ArrayList<>();

    @Builder
    public Song(Long songId, String title, String artist, String imgPath, Genre avgGenre) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.imgPath = imgPath;
        this.avgGenre = avgGenre;
        this.pins = new ArrayList<>();
        this.songGenres = new ArrayList<>();
    }
}
