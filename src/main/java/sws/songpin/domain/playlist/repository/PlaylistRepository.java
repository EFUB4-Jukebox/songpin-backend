package sws.songpin.domain.playlist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sws.songpin.domain.member.entity.Member;
import sws.songpin.domain.model.Visibility;
import sws.songpin.domain.playlist.entity.Playlist;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findAllByCreator(Member member);

    @Query("SELECT p FROM Playlist p WHERE p.creator = :creator AND p.visibility = 'PUBLIC'")
    List<Playlist> findAllPublicByCreator(@Param("creator") Member creator);


    @Query(value = "SELECT p.playlist_id, p.playlist_name, COUNT(pin.pin_id) AS pin_count " +
            "FROM playlist p " +
            "LEFT JOIN playlist_pin pin ON p.playlist_id = pin.playlist_id " +
            "WHERE REPLACE(p.playlist_name, ' ', '') LIKE %:keywordNoSpaces% " +
            "AND (p.visibility = 'PUBLIC' OR p.creator_id = :currentMemberId) " +
            "GROUP BY p.playlist_id " +
            "ORDER BY pin_count DESC, p.playlist_name ASC",
            countQuery = "SELECT COUNT(DISTINCT p.playlist_id) " +
                    "FROM playlist p " +
                    "LEFT JOIN playlist_pin pin ON p.playlist_id = pin.playlist_id " +
                    "WHERE REPLACE(p.playlist_name, ' ', '') LIKE %:keywordNoSpaces% " +
                    "AND (p.visibility = 'PUBLIC' OR p.creator_id = :currentMemberId)",
            nativeQuery = true)
    Page<Object[]> findAllByPlaylistNameContainingIgnoreSpacesOrderByCount(@Param("keywordNoSpaces") String keywordNoSpaces, @Param("currentMemberId") Long currentMemberId, Pageable pageable);

    @Query(value = "SELECT p.playlist_id, p.playlist_name, COUNT(pin.pin_id) AS pin_count " +
            "FROM playlist p " +
            "LEFT JOIN playlist_pin pin ON p.playlist_id = pin.playlist_id " +
            "WHERE REPLACE(p.playlist_name, ' ', '') LIKE %:keywordNoSpaces% " +
            "AND (p.visibility = 'PUBLIC' OR p.creator_id = :currentMemberId) " +
            "GROUP BY p.playlist_id " +
            "ORDER BY MAX(p.modified_time) DESC",
            countQuery = "SELECT COUNT(DISTINCT p.playlist_id) " +
                    "FROM playlist p " +
                    "LEFT JOIN playlist_pin pin ON p.playlist_id = pin.playlist_id " +
                    "WHERE REPLACE(p.playlist_name, ' ', '') LIKE %:keywordNoSpaces% " +
                    "AND (p.visibility = 'PUBLIC' OR p.creator_id = :currentMemberId)",
            nativeQuery = true)
    Page<Object[]> findAllByPlaylistNameContainingIgnoreSpacesOrderByNewest(@Param("keywordNoSpaces") String keywordNoSpaces, @Param("currentMemberId") Long currentMemberId, Pageable pageable);

    @Query(value = "SELECT p.playlist_id, p.playlist_name, COUNT(pin.pin_id) AS pin_count " +
            "FROM playlist p " +
            "LEFT JOIN playlist_pin pin ON p.playlist_id = pin.playlist_id " +
            "WHERE REPLACE(p.playlist_name, ' ', '') LIKE %:keywordNoSpaces% " +
            "AND (p.visibility = 'PUBLIC' OR p.creator_id = :currentMemberId) " +
            "GROUP BY p.playlist_id " +
            "ORDER BY p.playlist_name ASC",
            countQuery = "SELECT COUNT(DISTINCT p.playlist_id) " +
                    "FROM playlist p " +
                    "LEFT JOIN playlist_pin pin ON p.playlist_id = pin.playlist_id " +
                    "WHERE REPLACE(p.playlist_name, ' ', '') LIKE %:keywordNoSpaces% " +
                    "AND (p.visibility = 'PUBLIC' OR p.creator_id = :currentMemberId)",
            nativeQuery = true)
    Page<Object[]> findAllByPlaylistNameContainingIgnoreSpacesOrderByAccuracy(@Param("keywordNoSpaces") String keywordNoSpaces, @Param("currentMemberId") Long currentMemberId, Pageable pageable);
}