package com.music.music_inventory_api.repository;

import com.music.music_inventory_api.entity.Artist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Artist entity. Provides CRUD operations and custom
 * queries for artist data access.
 */
@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long>
{
    Optional<Artist> findByNameIgnoreCase(String name);

    /**
     * Custom query to search artists by country.
     *
     * @param country
     *            the country to search for
     * @return list of artists from the specified country
     */
    @Query("SELECT a FROM Artist a WHERE LOWER(a.country) = LOWER(:country)")
    List<Artist> findByCountry(@Param("country") String country);

    /**
     * Custom query to search artists by name containing a keyword
     * (case-insensitive).
     *
     * @param keyword
     *            the keyword to search for in artist names
     * @return list of artists matching the search criteria
     */
    @Query("SELECT a FROM Artist a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Artist> searchByName(@Param("keyword") String keyword);

    /**
     * Custom query to find artists with albums in stock.
     *
     * @return list of artists who have albums with stock quantity greater than 0
     */
    @Query("SELECT DISTINCT al.artist FROM Album al " + "WHERE al.stockQuantity > 0")
    List<Artist> findArtistsWithAvailableStock();

    /**
     * Custom query to find artists with their album count. Returns artists ordered
     * by album count descending.
     *
     * @return list of artists with at least one album, ordered by album count
     */
    @Query("SELECT al.artist FROM Album al " + "GROUP BY al.artist " + "HAVING COUNT(al) > 0 "
            + "ORDER BY COUNT(al) DESC")
    List<Artist> findArtistsOrderedByAlbumCount();
}
