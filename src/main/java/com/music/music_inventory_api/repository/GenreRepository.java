package com.music.music_inventory_api.repository;

import com.music.music_inventory_api.entity.Genre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Genre entity. Provides CRUD operations and custom
 * queries for genre data access.
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>
{
    Optional<Genre> findByNameIgnoreCase(String name);

    /**
     * Custom query to search genres by name containing a keyword.
     *
     * @param keyword
     *            the search keyword
     * @return list of genres matching the search criteria
     */
    @Query("SELECT g FROM Genre g " + "WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " + "ORDER BY g.name ASC")
    List<Genre> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Custom query to find genres with their album count.
     *
     * @return list of genres ordered by album count descending
     */
    @Query("SELECT g FROM Genre g " + "LEFT JOIN Album a ON EXISTS (" + "  SELECT 1 FROM Album al2 "
            + "  JOIN al2.genres g2 " + "  WHERE g2.id = g.id AND al2.id = a.id" + ") " + "GROUP BY g.id "
            + "ORDER BY COUNT(a) DESC")
    List<Genre> findGenresOrderedByAlbumCount();

    /**
     * Custom query to find genres that have albums in stock.
     *
     * @return list of genres with at least one album in stock
     */
    @Query("SELECT DISTINCT g FROM Genre g " + "JOIN Album a ON EXISTS (" + "  SELECT 1 FROM Album al2 "
            + "  JOIN al2.genres g2 " + "  WHERE g2.id = g.id AND al2.id = a.id" + ") " + "WHERE a.stockQuantity > 0")
    List<Genre> findGenresWithAvailableStock();
}
