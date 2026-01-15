package com.music.music_inventory_api.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.music.music_inventory_api.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Tests for entity creation and relationships using Arrange-Act-Assert pattern.
 */
class EntityTest
{

    @Test
    void artistCreation_withValidData_shouldCreateArtistSuccessfully()
    {
        // Arrange
        String name = "The Beatles";
        String country = "United Kingdom";
        String biography = "Legendary rock band";

        // Act
        Artist artist = Artist.builder().name(name).country(country).biography(biography).build();

        // Assert
        assertEquals(name, artist.getName());
        assertEquals(country, artist.getCountry());
        assertEquals(biography, artist.getBiography());
        assertNotNull(artist.toString());
    }

    @Test
    void genreCreation_withValidData_shouldCreateGenreSuccessfully()
    {
        // Arrange
        String name = "Rock";
        String description = "Rock music genre";

        // Act
        Genre genre = Genre.builder().name(name).description(description).build();

        // Assert
        assertEquals(name, genre.getName());
        assertEquals(description, genre.getDescription());
        assertNotNull(genre.toString());
    }

    @Test
    void albumCreation_withArtistAndValidData_shouldCreateAlbumSuccessfully()
    {
        // Arrange
        Artist artist = Artist.builder().id(1L).name("The Beatles").build();
        String title = "Abbey Road";
        LocalDate releaseDate = LocalDate.of(1969, 9, 26);
        BigDecimal price = new BigDecimal("19.99");
        Integer stockQuantity = 100;

        // Act
        Album album = Album.builder().title(title).artist(artist).releaseDate(releaseDate).price(price)
                .stockQuantity(stockQuantity).build();

        // Assert
        assertEquals(title, album.getTitle());
        assertEquals(artist, album.getArtist());
        assertEquals(releaseDate, album.getReleaseDate());
        assertEquals(price, album.getPrice());
        assertEquals(stockQuantity, album.getStockQuantity());
        assertNotNull(album.toString());
    }

    @Test
    void albumGenreRelationship_whenAddingAndRemovingGenres_shouldManageGenresCorrectly()
    {
        // Arrange
        Album album = Album.builder().title("Abbey Road").price(new BigDecimal("19.99")).stockQuantity(100).build();
        Genre rock = Genre.builder().id(1L).name("Rock").build();
        Genre pop = Genre.builder().id(2L).name("Pop").build();

        // Act - Add genres
        album.addGenre(rock);
        album.addGenre(pop);

        // Assert - Both genres added
        assertEquals(2, album.getGenres().size());
        assertTrue(album.getGenres().contains(rock));
        assertTrue(album.getGenres().contains(pop));

        // Act - Remove one genre
        album.removeGenre(pop);

        // Assert - Only one genre remains
        assertEquals(1, album.getGenres().size());
        assertTrue(album.getGenres().contains(rock));
        assertFalse(album.getGenres().contains(pop));
    }

    @Test
    void songCreation_withAlbumAndValidData_shouldCreateSongSuccessfully()
    {
        // Arrange
        Album album = Album.builder().id(1L).title("Abbey Road").build();
        String title = "Come Together";
        Integer trackNumber = 1;
        Integer durationSeconds = 259;

        // Act
        Song song = Song.builder().title(title).album(album).trackNumber(trackNumber).durationSeconds(durationSeconds)
                .build();

        // Assert
        assertEquals(title, song.getTitle());
        assertEquals(album, song.getAlbum());
        assertEquals(trackNumber, song.getTrackNumber());
        assertEquals(durationSeconds, song.getDurationSeconds());
        assertNotNull(song.toString());
    }

    @Test
    void customerCreation_withValidData_shouldCreateCustomerSuccessfully()
    {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String phone = "+1234567890";
        String address = "123 Main St";
        String city = "New York";
        String country = "USA";
        String postalCode = "10001";

        // Act
        Customer customer = Customer.builder().firstName(firstName).lastName(lastName).email(email).phone(phone)
                .address(address).city(city).country(country).postalCode(postalCode).build();

        // Assert
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
        assertEquals(email, customer.getEmail());
        assertEquals(phone, customer.getPhone());
        assertEquals(address, customer.getAddress());
        assertEquals(city, customer.getCity());
        assertEquals(country, customer.getCountry());
        assertEquals(postalCode, customer.getPostalCode());
        assertNotNull(customer.toString());
    }

    @Test
    void orderCreation_withCustomerAndValidData_shouldCreateOrderSuccessfully()
    {
        // Arrange
        Customer customer = Customer.builder().id(1L).firstName("John").lastName("Doe").email("john.doe@example.com")
                .build();
        OrderStatus status = OrderStatus.PENDING;
        BigDecimal totalAmount = new BigDecimal("39.98");
        LocalDateTime orderDate = LocalDateTime.now();
        String shippingAddress = "123 Main St";
        String shippingCity = "New York";
        String shippingCountry = "USA";
        String shippingPostalCode = "10001";

        // Act
        Order order = Order.builder().customer(customer).status(status).totalAmount(totalAmount).orderDate(orderDate)
                .shippingAddress(shippingAddress).shippingCity(shippingCity).shippingCountry(shippingCountry)
                .shippingPostalCode(shippingPostalCode).build();

        // Assert
        assertEquals(customer, order.getCustomer());
        assertEquals(status, order.getStatus());
        assertEquals(totalAmount, order.getTotalAmount());
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(shippingAddress, order.getShippingAddress());
        assertEquals(shippingCity, order.getShippingCity());
        assertEquals(shippingCountry, order.getShippingCountry());
        assertEquals(shippingPostalCode, order.getShippingPostalCode());
        assertNotNull(order.toString());
    }

    @Test
    void orderItemCreation_withOrderAndAlbum_shouldCreateOrderItemSuccessfully()
    {
        // Arrange
        Order order = Order.builder().id(1L).build();
        Album album = Album.builder().id(1L).title("Abbey Road").build();
        Integer quantity = 2;
        BigDecimal unitPrice = new BigDecimal("19.99");
        BigDecimal subtotal = new BigDecimal("39.98");

        // Act
        OrderItem orderItem = OrderItem.builder().order(order).album(album).quantity(quantity).unitPrice(unitPrice)
                .subtotal(subtotal).build();

        // Assert
        assertEquals(order, orderItem.getOrder());
        assertEquals(album, orderItem.getAlbum());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(unitPrice, orderItem.getUnitPrice());
        assertEquals(subtotal, orderItem.getSubtotal());
        assertNotNull(orderItem.toString());
    }

    @Test
    void orderOrderItemRelationship_whenAddingAndRemovingItems_shouldManageBidirectionalRelationship()
    {
        // Arrange
        Order order = Order.builder().customer(Customer.builder().id(1L).build()).status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO).orderDate(LocalDateTime.now()).build();
        Album album = Album.builder().id(1L).title("Abbey Road").build();
        OrderItem orderItem = OrderItem.builder().album(album).quantity(1).unitPrice(new BigDecimal("19.99"))
                .subtotal(new BigDecimal("19.99")).build();

        // Act - Add order item
        order.addOrderItem(orderItem);

        // Assert - Item added and relationship established
        assertEquals(1, order.getOrderItems().size());
        assertEquals(order, orderItem.getOrder());
        assertTrue(order.getOrderItems().contains(orderItem));

        // Act - Remove order item
        order.removeOrderItem(orderItem);

        // Assert - Item removed and relationship cleared
        assertEquals(0, order.getOrderItems().size());
        assertNull(orderItem.getOrder());
    }

    @Test
    void entityEquals_whenEntitiesHaveSameId_shouldBeEqual()
    {
        // Arrange
        Artist artist1 = Artist.builder().id(1L).name("Artist 1").build();
        Artist artist2 = Artist.builder().id(1L).name("Artist 2").build();

        // Act & Assert
        assertEquals(artist1, artist2);
        assertEquals(artist1.hashCode(), artist2.hashCode());
    }

    @Test
    void entityEquals_whenEntitiesHaveDifferentIds_shouldNotBeEqual()
    {
        // Arrange
        Artist artist1 = Artist.builder().id(1L).name("Artist 1").build();
        Artist artist3 = Artist.builder().id(2L).name("Artist 1").build();

        // Act & Assert
        assertNotEquals(artist1, artist3);
    }

    @Test
    void orderStatusEnum_whenValueOfCalled_shouldReturnCorrectEnumValues()
    {
        // Act & Assert
        assertEquals(OrderStatus.PENDING, OrderStatus.valueOf("PENDING"));
        assertEquals(OrderStatus.PROCESSING, OrderStatus.valueOf("PROCESSING"));
        assertEquals(OrderStatus.SHIPPED, OrderStatus.valueOf("SHIPPED"));
        assertEquals(OrderStatus.DELIVERED, OrderStatus.valueOf("DELIVERED"));
        assertEquals(OrderStatus.CANCELLED, OrderStatus.valueOf("CANCELLED"));
        assertEquals(OrderStatus.REFUNDED, OrderStatus.valueOf("REFUNDED"));
    }
}
