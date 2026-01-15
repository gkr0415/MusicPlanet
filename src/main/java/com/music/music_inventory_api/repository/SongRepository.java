package com.music.music_inventory_api.repository;

import com.music.music_inventory_api.entity.Song;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Song entity. Provides CRUD operations and custom
 * queries for song data access.
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Long>
{
    List<Song> findByAlbumId(Long albumId);

    /**
     * Custom query to find songs by duration range.
     *
     * @param minDuration
     *            minimum duration in seconds
     * @param maxDuration
     *            maximum duration in seconds
     * @return list of songs within the duration range
     */
    @Query("SELECT s FROM Song s " + "WHERE s.durationSeconds BETWEEN :minDuration AND :maxDuration "
            + "ORDER BY s.durationSeconds ASC")
    List<Song> findByDurationRange(@Param("minDuration") int minDuration, @Param("maxDuration") int maxDuration);

    /**
     * Custom query to search songs by title (case-insensitive).
     *
     * @param keyword
     *            the search keyword
     * @return list of songs matching the search criteria
     */
    @Query("SELECT s FROM Song s " + "WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "ORDER BY s.title ASC")
    List<Song> searchByTitle(@Param("keyword") String keyword);

    /**
     * Custom query to find songs by artist name.
     *
     * @param artistName
     *            the artist name
     * @return list of songs by the specified artist
     */
    @Query("SELECT s FROM Song s " + "JOIN s.album a " + "JOIN a.artist ar "
            + "WHERE LOWER(ar.name) = LOWER(:artistName) " + "ORDER BY s.trackNumber ASC")
    List<Song> findByArtistName(@Param("artistName") String artistName);

    /**
     * Custom query to find songs ordered by track number.
     *
     * @param albumId
     *            the album ID
     * @return list of songs ordered by track number
     */
    @Query("SELECT s FROM Song s " + "WHERE s.album.id = :albumId " + "ORDER BY s.trackNumber ASC")
    List<Song> findByAlbumOrderedByTrackNumber(@Param("albumId") Long albumId);
}
