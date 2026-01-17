package com.music.music_inventory_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.music.music_inventory_api.config.JpaAuditingConfig;
import com.music.music_inventory_api.entity.Album;
import com.music.music_inventory_api.entity.Artist;
import com.music.music_inventory_api.entity.Customer;
import com.music.music_inventory_api.entity.Genre;
import com.music.music_inventory_api.entity.Order;
import com.music.music_inventory_api.entity.OrderItem;
import com.music.music_inventory_api.entity.Song;
import com.music.music_inventory_api.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for all repository interfaces. Uses @DataJpaTest for
 * testing JPA repositories with an in-memory H2 database.
 */
@DataJpaTest
@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
@SuppressWarnings("null")
class RepositoryTest
{

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Test entities
    private Artist artist1;
    private Artist artist2;
    private Album album1;
    private Album album2;
    private Album album3;
    private Genre genreRock;
    private Genre genrePop;
    private Song song1;
    private Song song2;
    private Customer customer1;
    private Customer customer2;
    private Order order1;
    private Order order2;
    private OrderItem orderItem1;
    private OrderItem orderItem2;

    @BeforeEach
    void setUp()
    {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        songRepository.deleteAll();
        albumRepository.deleteAll();
        artistRepository.deleteAll();
        genreRepository.deleteAll();
        customerRepository.deleteAll();

        artist1 = new Artist();
        artist1.setName("The Beatles");
        artist1.setCountry("United Kingdom");
        artist1.setBiography("Legendary rock band");
        artist1 = artistRepository.save(artist1);

        artist2 = new Artist();
        artist2.setName("Pink Floyd");
        artist2.setCountry("United Kingdom");
        artist2.setBiography("Progressive rock pioneers");
        artist2 = artistRepository.save(artist2);

        genreRock = new Genre();
        genreRock.setName("Rock");
        genreRock.setDescription("Rock music genre");
        genreRock = genreRepository.save(genreRock);

        genrePop = new Genre();
        genrePop.setName("Pop");
        genrePop.setDescription("Pop music genre");
        genrePop = genreRepository.save(genrePop);

        album1 = new Album();
        album1.setTitle("Abbey Road");
        album1.setArtist(artist1);
        album1.setReleaseDate(LocalDate.of(1969, 9, 26));
        album1.setPrice(new BigDecimal("19.99"));
        album1.setStockQuantity(50);
        album1.addGenre(genreRock);
        album1 = albumRepository.save(album1);

        album2 = new Album();
        album2.setTitle("Let It Be");
        album2.setArtist(artist1);
        album2.setReleaseDate(LocalDate.of(1970, 5, 8));
        album2.setPrice(new BigDecimal("24.99"));
        album2.setStockQuantity(0);
        album2.addGenre(genreRock);
        album2.addGenre(genrePop);
        album2 = albumRepository.save(album2);

        album3 = new Album();
        album3.setTitle("The Dark Side of the Moon");
        album3.setArtist(artist2);
        album3.setReleaseDate(LocalDate.of(1973, 3, 1));
        album3.setPrice(new BigDecimal("29.99"));
        album3.setStockQuantity(30);
        album3.addGenre(genreRock);
        album3 = albumRepository.save(album3);

        song1 = new Song();
        song1.setTitle("Come Together");
        song1.setAlbum(album1);
        song1.setTrackNumber(1);
        song1.setDurationSeconds(259);
        song1 = songRepository.save(song1);

        song2 = new Song();
        song2.setTitle("Something");
        song2.setAlbum(album1);
        song2.setTrackNumber(2);
        song2.setDurationSeconds(182);
        song2 = songRepository.save(song2);

        customer1 = new Customer();
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setPassword("password123");
        customer1.setPhone("1234567890");
        customer1 = customerRepository.save(customer1);

        customer2 = new Customer();
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setEmail("jane.smith@example.com");
        customer2.setPassword("password456");
        customer2.setPhone("0987654321");
        customer2 = customerRepository.save(customer2);

        order1 = new Order();
        order1.setCustomer(customer1);
        order1.setStatus(OrderStatus.DELIVERED);
        order1.setTotalAmount(new BigDecimal("39.98"));
        order1.setOrderDate(LocalDateTime.now().minusDays(5));
        order1 = orderRepository.save(order1);

        order2 = new Order();
        order2.setCustomer(customer1);
        order2.setStatus(OrderStatus.PENDING);
        order2.setTotalAmount(new BigDecimal("29.99"));
        order2.setOrderDate(LocalDateTime.now());
        order2 = orderRepository.save(order2);

        orderItem1 = new OrderItem();
        orderItem1.setOrder(order1);
        orderItem1.setAlbum(album1);
        orderItem1.setQuantity(2);
        orderItem1.setUnitPrice(new BigDecimal("19.99"));
        orderItem1.setSubtotal(new BigDecimal("39.98"));
        orderItem1 = orderItemRepository.save(orderItem1);

        orderItem2 = new OrderItem();
        orderItem2.setOrder(order2);
        orderItem2.setAlbum(album3);
        orderItem2.setQuantity(1);
        orderItem2.setUnitPrice(new BigDecimal("29.99"));
        orderItem2.setSubtotal(new BigDecimal("29.99"));
        orderItem2 = orderItemRepository.save(orderItem2);
    }

    @Test
    void findByNameIgnoreCase_withExistingName_shouldReturnArtist()
    {
        // Arrange & Act
        Optional<Artist> result = artistRepository.findByNameIgnoreCase("the beatles");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("The Beatles");
    }

    @Test
    void findByCountry_withExistingCountry_shouldReturnArtists()
    {
        // Arrange & Act
        List<Artist> results = artistRepository.findByCountry("United Kingdom");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Artist::getName).containsExactlyInAnyOrder("The Beatles", "Pink Floyd");
    }

    @Test
    void searchByName_withKeyword_shouldReturnMatchingArtists()
    {
        // Arrange & Act
        List<Artist> results = artistRepository.searchByName("beatles");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("The Beatles");
    }

    @Test
    void findArtistsWithAvailableStock_whenAlbumsInStock_shouldReturnArtists()
    {
        // Arrange & Act
        List<Artist> results = artistRepository.findArtistsWithAvailableStock();

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Artist::getName).contains("The Beatles", "Pink Floyd");
    }

    @Test
    void findArtistsOrderedByAlbumCount_whenArtistsHaveAlbums_shouldReturnSortedList()
    {
        // Arrange & Act
        List<Artist> results = artistRepository.findArtistsOrderedByAlbumCount();

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getName()).isEqualTo("The Beatles");
    }

    @Test
    void searchByGenreAndPriceRange_withValidCriteria_shouldReturnMatchingAlbums()
    {
        // Arrange
        BigDecimal minPrice = new BigDecimal("15.00");
        BigDecimal maxPrice = new BigDecimal("25.00");

        // Act
        List<Album> results = albumRepository.searchByGenreAndPriceRange("Rock", minPrice, maxPrice);

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Album::getTitle).contains("Abbey Road", "Let It Be");
    }

    @Test
    void findTopSellingAlbums_whenOrderItemsExist_shouldReturnTopAlbums()
    {
        // Arrange & Act
        List<Album> results = albumRepository.findTopSellingAlbums();

        // Assert
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getTitle()).isEqualTo("Abbey Road");
    }

    @Test
    void findByArtistWithStock_withArtistHavingStock_shouldReturnAlbums()
    {
        // Arrange & Act
        List<Album> results = albumRepository.findByArtistWithStock(artist1.getId());

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Abbey Road");
    }

    @Test
    void searchByTitleOrArtistName_withKeyword_shouldReturnMatchingAlbums()
    {
        // Arrange & Act
        List<Album> results = albumRepository.searchByTitleOrArtistName("beatles");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Album::getTitle).contains("Abbey Road", "Let It Be");
    }

    @Test
    void findByReleaseYear_withValidYear_shouldReturnAlbums()
    {
        // Arrange & Act
        List<Album> results = albumRepository.findByReleaseYear(1969);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Abbey Road");
    }

    @Test
    void findLowStockAlbums_withThreshold_shouldReturnLowStockAlbums()
    {
        // Arrange & Act
        List<Album> results = albumRepository.findLowStockAlbums(40);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("The Dark Side of the Moon");
    }

    @Test
    void findByAlbumId_withValidAlbumId_shouldReturnSongs()
    {
        // Arrange & Act
        List<Song> results = songRepository.findByAlbumId(album1.getId());

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Song::getTitle).contains("Come Together", "Something");
    }

    @Test
    void findByDurationRange_withValidRange_shouldReturnSongs()
    {
        // Arrange & Act
        List<Song> results = songRepository.findByDurationRange(180, 260);

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    void searchByTitle_withKeyword_shouldReturnMatchingSongs()
    {
        // Arrange & Act
        List<Song> results = songRepository.searchByTitle("together");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Come Together");
    }

    @Test
    void findByArtistName_withValidArtist_shouldReturnSongs()
    {
        // Arrange & Act
        List<Song> results = songRepository.findByArtistName("The Beatles");

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    void findByAlbumOrderedByTrackNumber_withValidAlbumId_shouldReturnOrderedSongs()
    {
        // Arrange & Act
        List<Song> results = songRepository.findByAlbumOrderedByTrackNumber(album1.getId());

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getTrackNumber()).isEqualTo(1);
        assertThat(results.get(1).getTrackNumber()).isEqualTo(2);
    }

    @Test
    void findByNameIgnoreCase_withExistingName_shouldReturnGenre()
    {
        // Arrange & Act
        Optional<Genre> result = genreRepository.findByNameIgnoreCase("rock");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Rock");
    }

    @Test
    void searchByKeyword_withKeyword_shouldReturnMatchingGenres()
    {
        // Arrange & Act
        List<Genre> results = genreRepository.searchByKeyword("pop");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Pop");
    }

    @Test
    void findGenresOrderedByAlbumCount_whenGenresHaveAlbums_shouldReturnSortedList()
    {
        // Arrange & Act
        List<Genre> results = genreRepository.findGenresOrderedByAlbumCount();

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getName()).isEqualTo("Rock");
    }

    @Test
    void findGenresWithAvailableStock_whenAlbumsInStock_shouldReturnGenres()
    {
        // Arrange & Act
        List<Genre> results = genreRepository.findGenresWithAvailableStock();

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Rock");
    }

    @Test
    void findByEmailIgnoreCase_withExistingEmail_shouldReturnCustomer()
    {
        // Arrange & Act
        Optional<Customer> result = customerRepository.findByEmailIgnoreCase("JOHN.DOE@EXAMPLE.COM");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void searchByName_withKeyword_shouldReturnMatchingCustomers()
    {
        // Arrange & Act
        List<Customer> results = customerRepository.searchByName("john");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void findCustomersWithOrdersAfter_withValidDate_shouldReturnCustomers()
    {
        // Arrange
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        // Act
        List<Customer> results = customerRepository.findCustomersWithOrdersAfter(pastDate);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void findCustomersWithNoOrders_whenCustomerHasNoOrders_shouldReturnCustomers()
    {
        // Arrange & Act
        List<Customer> results = customerRepository.findCustomersWithNoOrders();

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFirstName()).isEqualTo("Jane");
    }

    @Test
    void findTopCustomersByOrderCount_whenCustomersHaveOrders_shouldReturnSortedList()
    {
        // Arrange & Act
        List<Customer> results = customerRepository.findTopCustomersByOrderCount();

        // Assert
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void findByCustomerId_withValidCustomerId_shouldReturnOrders()
    {
        // Arrange & Act
        List<Order> results = orderRepository.findByCustomerId(customer1.getId());

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    void findByStatus_withValidStatus_shouldReturnOrders()
    {
        // Arrange & Act
        List<Order> results = orderRepository.findByStatus(OrderStatus.DELIVERED);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void getCustomerOrderStatistics_withValidCustomerId_shouldReturnStatistics()
    {
        // Arrange & Act
        Object[] statistics = orderRepository.getCustomerOrderStatistics(customer1.getId());

        // Assert
        assertThat(statistics).isNotNull();
        assertThat(statistics.length).isGreaterThan(0);

        Object[] innerArray = (Object[]) statistics[0];

        Long orderCount = ((Number) innerArray[0]).longValue();
        BigDecimal totalAmount = (BigDecimal) innerArray[1];
        Double avgAmount = ((Number) innerArray[2]).doubleValue();

        assertThat(orderCount).isEqualTo(2);
        assertThat(totalAmount).isGreaterThan(BigDecimal.ZERO);
        assertThat(avgAmount).isGreaterThan(0.0);
    }

    @Test
    void findByDateRange_withValidRange_shouldReturnOrders()
    {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        // Act
        List<Order> results = orderRepository.findByDateRange(start, end);

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    void findByCustomerAndStatus_withValidCriteria_shouldReturnOrders()
    {
        // Arrange & Act
        List<Order> results = orderRepository.findByCustomerAndStatus(customer1.getId(), OrderStatus.PENDING);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void findOrdersAboveAmount_withThreshold_shouldReturnOrders()
    {
        // Arrange & Act
        List<Order> results = orderRepository.findOrdersAboveAmount(new BigDecimal("30.00"));

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTotalAmount()).isGreaterThan(new BigDecimal("30.00"));
    }

    @Test
    void calculateTotalRevenue_withDateRange_shouldReturnRevenue()
    {
        // Arrange
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        // Act
        BigDecimal revenue = orderRepository.calculateTotalRevenue(start, end);

        // Assert
        assertThat(revenue).isNotNull();
        assertThat(revenue).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void findByOrderId_withValidOrderId_shouldReturnOrderItems()
    {
        // Arrange & Act
        List<OrderItem> results = orderItemRepository.findByOrderId(order1.getId());

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getAlbum().getTitle()).isEqualTo("Abbey Road");
    }

    @Test
    void findByAlbumId_withValidAlbumId_shouldReturnOrderItems()
    {
        // Arrange & Act
        List<OrderItem> results = orderItemRepository.findByAlbumId(album1.getId());

        // Assert
        assertThat(results).hasSize(1);
    }

    @Test
    void calculateTotalQuantitySold_withValidAlbumId_shouldReturnQuantity()
    {
        // Arrange & Act
        Long totalQuantity = orderItemRepository.calculateTotalQuantitySold(album1.getId());

        // Assert
        assertThat(totalQuantity).isEqualTo(2);
    }

    @Test
    void calculateAlbumRevenue_withValidAlbumId_shouldReturnRevenue()
    {
        // Arrange & Act
        BigDecimal revenue = orderItemRepository.calculateAlbumRevenue(album1.getId());

        // Assert
        assertThat(revenue).isNotNull();
        assertThat(revenue).isEqualTo(new BigDecimal("39.98"));
    }

    @Test
    void findByCustomerId_withValidCustomerId_shouldReturnOrderItems()
    {
        // Arrange & Act
        List<OrderItem> results = orderItemRepository.findByCustomerId(customer1.getId());

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    void findMostPopularAlbums_whenOrderItemsExist_shouldReturnPopularAlbums()
    {
        // Arrange & Act
        List<Object[]> results = orderItemRepository.findMostPopularAlbums();

        // Assert
        assertThat(results).isNotEmpty();
        assertThat(results.get(0)[1]).isNotNull();
    }
}
