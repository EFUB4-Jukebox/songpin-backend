package sws.songpin.domain.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.playlist.entity.Playlist;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findAllByCreator(Member member);
}
