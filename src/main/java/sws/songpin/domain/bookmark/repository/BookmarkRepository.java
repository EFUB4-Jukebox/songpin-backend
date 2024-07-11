package sws.songpin.domain.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.bookmark.entity.Bookmark;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.playlist.entity.Playlist;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
//    boolean existsByPlaylistAndMember(Playlist playlist, Member member);
    Optional<Bookmark> findByPlaylistAndMember(Playlist playlist, Member member);
}
