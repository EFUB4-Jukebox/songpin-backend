package sws.songpin.entity.songGenre.domain;

import jakarta.persistence.*;
import lombok.*;
import sws.songpin.entity.genre.domain.Genre;
import sws.songpin.entity.song.domain.Song;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class SongGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_genre_id", updatable = false)
    private Long songGenreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Builder
    public SongGenre(Song song, Genre genre) {
        this.song = song;
        this.genre = genre;
    }
}