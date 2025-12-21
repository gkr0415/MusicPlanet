# Music Store - Database Schema

## Entity Relationship Diagram (ERD)

```
┌──────────────────┐
│     ARTIST       │
├──────────────────┤
│ PK id            │
│    name          │
│    bio           │
│    country       │
│    created_at    │
│    updated_at    │
└────────┬─────────┘
         │
         │ 1:N
         │
         ▼
┌──────────────────┐         ┌──────────────────┐
│     ALBUM        │◄────────┤  ALBUM_GENRE     │
├──────────────────┤    N:1  ├──────────────────┤
│ PK id            │         │ PK,FK album_id   │
│ FK artist_id     │         │ PK,FK genre_id   │
│    title         │         └────────┬─────────┘
│    release_date  │                  │
│    price         │                  │ N:1
│    stock_qty     │                  │
│    cover_img_url │                  ▼
│    created_at    │         ┌──────────────────┐
│    updated_at    │         │     GENRE        │
└────────┬─────────┘         ├──────────────────┤
         │                   │ PK id            │
         │ 1:N               │    name          │
         │                   │    description   │
         ▼                   │    created_at    │
┌──────────────────┐         └──────────────────┘
│      SONG        │
├──────────────────┤
│ PK id            │
│ FK album_id      │
│    title         │
│    duration_sec  │
│    track_number  │
│    created_at    │
└──────────────────┘


┌──────────────────┐
│    CUSTOMER      │
├──────────────────┤
│ PK id            │
│    email (UQ)    │
│    first_name    │
│    last_name     │
│    phone         │
│    address       │
│    city          │
│    country       │
│    postal_code   │
│    created_at    │
│    updated_at    │
└────────┬─────────┘
         │
         │ 1:N
         │
         ▼
┌──────────────────┐
│     ORDER        │
├──────────────────┤
│ PK id            │
│ FK customer_id   │
│    order_date    │
│    total_amount  │
│    status        │
│    created_at    │
│    updated_at    │
└────────┬─────────┘
         │
         │ 1:N
         │
         ▼
┌──────────────────┐
│   ORDER_ITEM     │
├──────────────────┤
│ PK id            │
│ FK order_id      │──────┐
│ FK album_id      │      │ N:1
│    quantity      │      │
│    unit_price    │      │
│    subtotal      │      │
└──────────────────┘      │
                          ▼
                   ┌──────────────────┐
                   │     ALBUM        │
                   │  (referenced)    │
                   └──────────────────┘
```

## Detailed Table Definitions

### 1. ARTISTS
```sql
CREATE TABLE artists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    bio TEXT,
    country VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_artists_name ON artists(name);
```

### 2. GENRES
```sql
CREATE TABLE genres (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_genres_name ON genres(name);
```

### 3. ALBUMS
```sql
CREATE TABLE albums (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist_id BIGINT NOT NULL,
    release_date DATE,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    stock_quantity INTEGER NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    cover_image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE
);

CREATE INDEX idx_albums_artist ON albums(artist_id);
CREATE INDEX idx_albums_title ON albums(title);
CREATE INDEX idx_albums_price ON albums(price);
CREATE INDEX idx_albums_release_date ON albums(release_date);
```

### 4. ALBUM_GENRE (Junction Table for Many-to-Many)
```sql
CREATE TABLE album_genre (
    album_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (album_id, genre_id),
    FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

CREATE INDEX idx_album_genre_genre ON album_genre(genre_id);
```

### 5. SONGS
```sql
CREATE TABLE songs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    album_id BIGINT NOT NULL,
    duration_seconds INTEGER NOT NULL CHECK (duration_seconds > 0),
    track_number INTEGER NOT NULL CHECK (track_number > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE,
    UNIQUE (album_id, track_number)
);

CREATE INDEX idx_songs_album ON songs(album_id);
CREATE INDEX idx_songs_title ON songs(title);
```

### 6. CUSTOMERS
```sql
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_name ON customers(last_name, first_name);
```

### 7. ORDERS
```sql
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    CHECK (status IN ('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'))
);

CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_date ON orders(order_date);
CREATE INDEX idx_orders_status ON orders(status);
```

### 8. ORDER_ITEMS
```sql
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    album_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    subtotal DECIMAL(10, 2) NOT NULL CHECK (subtotal >= 0),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE RESTRICT
);

CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_album ON order_items(album_id);
```

## Relationships Summary

### One-to-Many Relationships
1. **Artist → Albums**: One artist can have many albums
2. **Album → Songs**: One album can have many songs
3. **Customer → Orders**: One customer can have many orders
4. **Order → OrderItems**: One order can have many order items

### Many-to-Many Relationship
1. **Albums ↔ Genres**: 
   - One album can belong to multiple genres
   - One genre can be associated with multiple albums
   - Junction table: `album_genre`

### Many-to-One Relationships (Inverse of One-to-Many)
1. **Albums → Artist**: Many albums belong to one artist
2. **Songs → Album**: Many songs belong to one album
3. **Orders → Customer**: Many orders belong to one customer
4. **OrderItems → Order**: Many order items belong to one order
5. **OrderItems → Album**: Many order items reference one album

## Sample Data Constraints

### Business Rules Enforced
1. Album prices must be non-negative
2. Stock quantity cannot be negative
3. Order total amount must be non-negative
4. Song duration must be positive
5. Track numbers must be unique within an album
6. Customer emails must be unique
7. Order status must be one of: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
8. Order items cannot be deleted if they reference an album (RESTRICT)
9. Cascading deletes for dependent entities

## Indexes Strategy
Indexes are created on:
- Foreign keys (for JOIN performance)
- Fields commonly used in WHERE clauses (name, email, price, status)
- Fields used in ORDER BY operations (order_date, release_date)
- Unique constraints (email, genre name)

## Sample Queries (to be implemented with @Query)

### 1. Find albums by genre and price range
```java
@Query("SELECT a FROM Album a JOIN a.genres g WHERE g.id = :genreId AND a.price BETWEEN :minPrice AND :maxPrice")
List<Album> findAlbumsByGenreAndPriceRange(@Param("genreId") Long genreId, 
                                           @Param("minPrice") BigDecimal minPrice, 
                                           @Param("maxPrice") BigDecimal maxPrice);
```

### 2. Find top-selling albums
```java
@Query("SELECT a.album, SUM(a.quantity) as totalSold FROM OrderItem a " +
       "GROUP BY a.album ORDER BY totalSold DESC")
List<Object[]> findTopSellingAlbums(Pageable pageable);
```

### 3. Find albums by artist with available stock
```java
@Query("SELECT a FROM Album a WHERE a.artist.id = :artistId AND a.stockQuantity > 0")
List<Album> findAvailableAlbumsByArtist(@Param("artistId") Long artistId);
```

### 4. Get customer order statistics
```java
@Query("SELECT c, COUNT(o), SUM(o.totalAmount) FROM Customer c " +
       "LEFT JOIN c.orders o GROUP BY c")
List<Object[]> getCustomerOrderStatistics();
```

### 5. Search albums by title or artist name
```java
@Query("SELECT a FROM Album a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
       "OR LOWER(a.artist.name) LIKE LOWER(CONCAT('%', :search, '%'))")
List<Album> searchAlbums(@Param("search") String searchTerm);
```
