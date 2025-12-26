# Music Store Application - Project Plan

## 🎯 Project Overview
A full-stack music store application allowing customers to browse, search, and purchase music albums. The system manages artists, albums, songs, genres, customers, and orders.

## 📋 Technical Requirements Met
- ✅ **Many-to-Many Relationship**: Album ↔ Genre
- ✅ **Many-to-One Relationships**: Multiple (Album → Artist, Song → Album, Order → Customer, etc.)
- ✅ **5+ Tables**: Artist, Album, Song, Genre, Customer, Order, OrderItem, AlbumGenre (junction table)
- ✅ **Code Separation**: Controllers, Services, Repositories
- ✅ **@Query Annotations**: Custom queries in repositories
- ✅ **Tests**: Unit and Integration tests
- ✅ **Swagger/OpenAPI**: API documentation
- ✅ **DTOs & Mappers**: Request/Response DTOs with MapStruct

## 🗄️ Database Schema

### Entities

#### 1. **Artist** (Artists table)
- `id` (Long, Primary Key)
- `name` (String, NOT NULL)
- `bio` (Text)
- `country` (String)
- `created_at` (Timestamp)
- `updated_at` (Timestamp)

**Relationships**: One-to-Many with Album

#### 2. **Album** (Albums table)
- `id` (Long, Primary Key)
- `title` (String, NOT NULL)
- `artist_id` (Long, Foreign Key → Artist)
- `release_date` (Date)
- `price` (Decimal)
- `stock_quantity` (Integer)
- `cover_image_url` (String)
- `created_at` (Timestamp)
- `updated_at` (Timestamp)

**Relationships**: 
- Many-to-One with Artist
- One-to-Many with Song
- Many-to-Many with Genre

#### 3. **Song** (Songs table)
- `id` (Long, Primary Key)
- `title` (String, NOT NULL)
- `album_id` (Long, Foreign Key → Album)
- `duration_seconds` (Integer)
- `track_number` (Integer)
- `created_at` (Timestamp)

**Relationships**: Many-to-One with Album

#### 4. **Genre** (Genres table)
- `id` (Long, Primary Key)
- `name` (String, NOT NULL, UNIQUE)
- `description` (Text)
- `created_at` (Timestamp)

**Relationships**: Many-to-Many with Album

#### 5. **AlbumGenre** (Junction Table)
- `album_id` (Long, Foreign Key → Album)
- `genre_id` (Long, Foreign Key → Genre)
- **Composite Primary Key**: (album_id, genre_id)

#### 6. **Customer** (Customers table)
- `id` (Long, Primary Key)
- `email` (String, NOT NULL, UNIQUE)
- `first_name` (String, NOT NULL)
- `last_name` (String, NOT NULL)
- `phone` (String)
- `address` (String)
- `city` (String)
- `country` (String)
- `postal_code` (String)
- `created_at` (Timestamp)
- `updated_at` (Timestamp)

**Relationships**: One-to-Many with Order

#### 7. **Order** (Orders table)
- `id` (Long, Primary Key)
- `customer_id` (Long, Foreign Key → Customer)
- `order_date` (Timestamp)
- `total_amount` (Decimal)
- `status` (Enum: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- `created_at` (Timestamp)
- `updated_at` (Timestamp)

**Relationships**: 
- Many-to-One with Customer
- One-to-Many with OrderItem

#### 8. **OrderItem** (Order_Items table)
- `id` (Long, Primary Key)
- `order_id` (Long, Foreign Key → Order)
- `album_id` (Long, Foreign Key → Album)
- `quantity` (Integer)
- `unit_price` (Decimal)
- `subtotal` (Decimal)

**Relationships**: 
- Many-to-One with Order
- Many-to-One with Album

## 🏗️ Backend Architecture (Spring Boot)

### Package Structure
```
com.music.music_inventory_api/
├── config/              # Configuration classes (Swagger, CORS, etc.)
├── controller/          # REST Controllers
├── service/             # Business logic
├── repository/          # Data access layer
├── entity/              # JPA entities
├── dto/                 # Data Transfer Objects
│   ├── request/         # Request DTOs
│   └── response/        # Response DTOs
├── mapper/              # MapStruct mappers
├── exception/           # Custom exceptions & handlers
└── enums/               # Enumerations
```

### Technology Stack (Backend)
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL / MySQL
- **ORM**: Spring Data JPA / Hibernate
- **API Documentation**: Springdoc OpenAPI (Swagger)
- **DTO Mapping**: MapStruct
- **Validation**: Jakarta Validation (Hibernate Validator)
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Build Tool**: Maven

### Key Features to Implement

#### Phase 1: Core Entities & Basic CRUD
1. Setup Spring Boot project with dependencies
2. Configure database connection
3. Create all entity classes with relationships
4. Create repositories for all entities
5. Implement at least 2 custom @Query methods
6. Create DTOs and MapStruct mappers
7. Implement basic CRUD services
8. Create REST controllers
9. Add Swagger/OpenAPI documentation
10. Write unit tests for services
11. Write integration tests for controllers

#### Phase 2: Business Logic
1. Album inventory management
2. Order processing logic
3. Stock management (reduce stock on order)
4. Search functionality (by artist, genre, album name)
5. Filter and pagination
6. Order status management
7. Customer order history

#### Phase 3: Advanced Features
1. Custom @Query examples:
   - Search albums by genre and price range
   - Find top-selling albums
   - Customer order statistics
   - Albums by artist with stock availability
2. Exception handling & validation
3. Integration tests
4. API documentation completion

## 🎨 Frontend Architecture (React)

### Project Structure
```
src/
├── components/          # Reusable components
│   ├── common/          # Buttons, inputs, cards, etc.
│   ├── layout/          # Header, footer, sidebar
│   ├── album/           # Album-related components
│   ├── artist/          # Artist-related components
│   ├── order/           # Order-related components
│   └── customer/        # Customer-related components
├── pages/               # Page components
│   ├── Home.jsx
│   ├── Albums.jsx
│   ├── AlbumDetail.jsx
│   ├── Artists.jsx
│   ├── Cart.jsx
│   ├── Checkout.jsx
│   └── Orders.jsx
├── services/            # API service calls
├── hooks/               # Custom React hooks
├── context/             # React Context (Cart, Auth)
├── utils/               # Utility functions
├── styles/              # CSS/SCSS files
└── App.jsx
```

### Technology Stack (Frontend)
- **Framework**: React 18+
- **State Management**: React Context API / Redux Toolkit
- **HTTP Client**: Axios
- **Routing**: React Router v6
- **UI Framework**: Material-UI / Ant Design / TailwindCSS
- **Form Handling**: React Hook Form
- **Build Tool**: Vite / Create React App

### Key Pages & Features
1. **Home Page**: Featured albums, genres navigation
2. **Albums Page**: Browse all albums with filters (genre, price, artist)
3. **Album Detail**: View album details, songs, add to cart
4. **Artists Page**: Browse artists
5. **Shopping Cart**: Manage cart items
6. **Checkout**: Complete order
7. **Order History**: View past orders
8. **Admin Panel** (optional): Manage inventory

## 🔧 API Endpoints (Examples)

### Artists
- `GET /api/artists` - Get all artists
- `GET /api/artists/{id}` - Get artist by ID
- `POST /api/artists` - Create artist
- `PUT /api/artists/{id}` - Update artist
- `DELETE /api/artists/{id}` - Delete artist
- `GET /api/artists/{id}/albums` - Get albums by artist

### Albums
- `GET /api/albums` - Get all albums (with pagination)
- `GET /api/albums/{id}` - Get album by ID
- `GET /api/albums/{id}/songs` - Get songs for album
- `GET /api/albums/search?title={title}` - Search albums
- `GET /api/albums/genre/{genreId}` - Get albums by genre
- `POST /api/albums` - Create album
- `PUT /api/albums/{id}` - Update album
- `DELETE /api/albums/{id}` - Delete album

### Genres
- `GET /api/genres` - Get all genres
- `GET /api/genres/{id}` - Get genre by ID
- `POST /api/genres` - Create genre

### Orders
- `POST /api/orders` - Create order
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/customer/{customerId}` - Get customer orders
- `PUT /api/orders/{id}/status` - Update order status

### Customers
- `GET /api/customers` - Get all customers
- `POST /api/customers` - Create customer
- `GET /api/customers/{id}` - Get customer by ID

## 🧪 Testing Strategy

### Backend Tests
1. **Unit Tests**: Services, Mappers
2. **Integration Tests**: Controllers with MockMvc
3. **Repository Tests**: Custom queries with @DataJpaTest
4. **Test Coverage**: Aim for 80%+

### Frontend Tests
1. **Component Tests**: React Testing Library
2. **Integration Tests**: Full page flows
3. **E2E Tests** (optional): Cypress/Playwright

## 🔄 CI/CD Pipeline (GitLab CI/CD)

### Pipeline Overview
Automated CI/CD pipeline that runs on every commit and merge request to ensure code quality and prevent broken builds from being merged.

### Pipeline Stages
1. **Build** - Compile backend and frontend
2. **Test** - Run all tests with coverage reports
3. **Quality** - Code quality checks and static analysis

## 🚀 Deployment (Production-Ready)

### Backend
- Dockerize the application
- Environment-based configuration
- Logging (SLF4J/Logback)
- Health checks endpoint
- CORS configuration for frontend
- Security headers

### Frontend
- Build optimization
- Environment variables
- Error boundaries
- Loading states
- Responsive design

### Database
- Migration scripts (Flyway/Liquibase)
- Indexes on foreign keys
- Proper constraints

## ✅ Success Criteria
- All CRUD operations working
- Relationships properly implemented and tested
- Custom @Query annotations working
- Swagger UI accessible and complete
- DTOs and mappers functioning
- Tests passing with good coverage (80%+)
- CI/CD pipeline passing on all merge requests
- Clean code structure
- Professional UI/UX
- Proper error handling
- Documentation complete
- Automated testing prevents broken code from being merged
