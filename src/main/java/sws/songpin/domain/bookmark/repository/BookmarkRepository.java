package sws.songpin.domain.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.bookmark.entity.Bookmark;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.playlist.entity.Playlist;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByPlaylistAndMember(Playlist playlist, Member member);
    boolean existsByPlaylistAndMember(Playlist playlist, Member member);
    List<Bookmark> findAllByMember(Member member);
    @Query("SELECT b.playlist.playlistId FROM Bookmark b WHERE b.member = :member AND b.playlist.playlistId IN :playlistIds")
    List<Long> findBookmarkedPlaylistIdsByMemberAndPlaylistIds(@Param("member") Member member, @Param("playlistIds") List<Long> playlistIds);
    void deleteAllByMember(Member member);
}
