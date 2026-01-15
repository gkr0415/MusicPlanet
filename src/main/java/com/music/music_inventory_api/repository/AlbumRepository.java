package com.music.music_inventory_api.repository;

import com.music.music_inventory_api.entity.Album;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Album entity. Provides CRUD operations and custom
 * queries for album data access.
 */
@Repository
public interface AlbumRepository extends JpaRepository<Album, Long>
{
    List<Album> findByArtistId(Long artistId);

    /**
     * Custom query to search albums by genre and price range. One of the
     * required @Query examples.
     *
     * @param genreName
     *            the genre name
     * @param minPrice
     *            minimum price
     * @param maxPrice
     *            maximum price
     * @return list of albums matching the criteria
     */
    @Query("SELECT DISTINCT a FROM Album a " + "JOIN a.genres g " + "WHERE LOWER(g.name) = LOWER(:genreName) "
            + "AND a.price BETWEEN :minPrice AND :maxPrice " + "ORDER BY a.price ASC")
    List<Album> searchByGenreAndPriceRange(@Param("genreName") String genreName, @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Custom query to find top-selling albums. One of the required @Query examples.
     *
     * @return list of top-selling albums ordered by total quantity sold
     */
    @Query("SELECT oi.album FROM OrderItem oi " + "GROUP BY oi.album " + "ORDER BY SUM(oi.quantity) DESC")
    List<Album> findTopSellingAlbums();

    /**
     * Custom query to find albums by artist with available stock. One of the
     * required @Query examples.
     *
     * @param artistId
     *            the artist ID
     * @return list of albums by the artist that are in stock
     */
    @Query("SELECT a FROM Album a " + "WHERE a.artist.id = :artistId " + "AND a.stockQuantity > 0 "
            + "ORDER BY a.releaseDate DESC")
    List<Album> findByArtistWithStock(@Param("artistId") Long artistId);

    /**
     * Custom query to search albums by title or artist name. One of the
     * required @Query examples.
     *
     * @param keyword
     *            the search keyword
     * @return list of albums matching the search criteria
     */
    @Query("SELECT a FROM Album a " + "WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(a.artist.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " + "ORDER BY a.title ASC")
    List<Album> searchByTitleOrArtistName(@Param("keyword") String keyword);

    /**
     * Custom query to find albums released in a specific year.
     *
     * @param year
     *            the release year
     * @return list of albums released in the specified year
     */
    @Query("SELECT a FROM Album a " + "WHERE YEAR(a.releaseDate) = :year " + "ORDER BY a.releaseDate ASC")
    List<Album> findByReleaseYear(@Param("year") int year);

    /**
     * Custom query to find albums with low stock (below threshold).
     *
     * @param threshold
     *            the stock threshold
     * @return list of albums with stock below the threshold
     */
    @Query("SELECT a FROM Album a " + "WHERE a.stockQuantity < :threshold AND a.stockQuantity > 0 "
            + "ORDER BY a.stockQuantity ASC")
    List<Album> findLowStockAlbums(@Param("threshold") int threshold);

    List<Album> findByStockQuantity(int stockQuantity);
}
