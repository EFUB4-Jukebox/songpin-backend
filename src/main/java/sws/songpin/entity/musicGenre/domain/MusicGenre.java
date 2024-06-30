package sws.songpin.entity.musicGenre.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.genre.domain.Genre;
import sws.songpin.entity.music.domain.Music;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class MusicGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_genre_id", updatable = false)
    private Long musicGenreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id", nullable = false)
    private Music music;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Builder
    public MusicGenre(Music music, Genre genre) {
        this.music = music;
        this.genre = genre;
    }
}