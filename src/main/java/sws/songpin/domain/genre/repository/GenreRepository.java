package sws.songpin.domain.genre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.genre.entity.Genre;
import sws.songpin.domain.genre.entity.GenreName;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre,Long> {
    Optional<Genre> findByGenreName(GenreName genreName);
}
