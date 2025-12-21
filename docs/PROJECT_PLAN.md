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

## 📝 Development Phases

### Phase 0: CI/CD Setup (Week 0 - 1-2 days)
- Setup GitLab CI/CD pipeline
- Configure branch protection rules
- Add code quality checks
- Setup test coverage reporting
- Configure automated testing

### Phase 1: Backend Foundation (Week 1)
- Setup Spring Boot project
- Configure database
- Create all entities
- Setup repositories
- Basic CRUD operations
- Swagger configuration

### Phase 2: Backend Services & API (Week 2)
- Implement all services
- Create DTOs and mappers
- Develop REST controllers
- Custom queries
- Exception handling
- Validation

### Phase 3: Backend Testing (Week 3)
- Unit tests for services
- Integration tests for controllers
- Repository tests
- Test coverage > 80%

### Phase 4: Frontend Setup (Week 3-4)
- React project setup
- Component structure
- API service integration
- Basic pages

### Phase 5: Frontend Development (Week 4-5)
- Implement all pages
- Shopping cart functionality
- Order flow
- UI/UX polish

### Phase 6: Integration & Testing (Week 6)
- Frontend-backend integration
- End-to-end testing
- Bug fixes
- Documentation

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

## 📚 Documentation Requirements
1. README.md with setup instructions
2. API documentation via Swagger UI
3. Database schema diagram
4. Architecture documentation
5. CI/CD setup guide (see CI_CD_GUIDE.md)
6. User guide (optional)

## 🔄 CI/CD Pipeline (GitLab CI/CD)

### Pipeline Overview
Automated CI/CD pipeline that runs on every commit and merge request to ensure code quality and prevent broken builds from being merged.

### Pipeline Stages
1. **Build** - Compile backend and frontend
2. **Test** - Run all tests with coverage reports
3. **Quality** - Code quality checks and static analysis
4. **Package** - Create Docker images (optional)
5. **Deploy** - Deploy to staging/production (optional)

### GitLab CI/CD Configuration (.gitlab-ci.yml)

```yaml
stages:
  - build
  - test
  - quality
  - package

variables:
  MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository"
  MAVEN_CLI_OPTS: "--batch-mode --errors --fail-at-end --show-version"

cache:
  paths:
    - .m2/repository
    - frontend/node_modules/

# Backend Jobs
backend-build:
  stage: build
  image: maven:3.9-eclipse-temurin-17
  script:
    - cd backend
    - mvn $MAVEN_CLI_OPTS clean compile
  artifacts:
    paths:
      - backend/target/
    expire_in: 1 hour

backend-test:
  stage: test
  image: maven:3.9-eclipse-temurin-17
  services:
    - postgres:15
  variables:
    POSTGRES_DB: music_store_test
    POSTGRES_USER: test
    POSTGRES_PASSWORD: test
    SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/music_store_test
  script:
    - cd backend
    - mvn $MAVEN_CLI_OPTS test
    - mvn jacoco:report
  coverage: '/Total.*?([0-9]{1,3})%/'
  artifacts:
    when: always
    reports:
      junit: backend/target/surefire-reports/TEST-*.xml
      coverage_report:
        coverage_format: cobertura
        path: backend/target/site/jacoco/jacoco.xml
    paths:
      - backend/target/surefire-reports/
      - backend/target/site/jacoco/
    expire_in: 7 days

# Frontend Jobs
frontend-build:
  stage: build
  image: node:18-alpine
  script:
    - cd frontend
    - npm ci
    - npm run build
  artifacts:
    paths:
      - frontend/dist/
      - frontend/build/
    expire_in: 1 hour

frontend-test:
  stage: test
  image: node:18-alpine
  script:
    - cd frontend
    - npm ci
    - npm run test -- --coverage --watchAll=false
  coverage: '/All files[^|]*\|[^|]*\s+([\d\.]+)/'
  artifacts:
    when: always
    reports:
      coverage_report:
        coverage_format: cobertura
        path: frontend/coverage/cobertura-coverage.xml
    paths:
      - frontend/coverage/
    expire_in: 7 days

# Code Quality
code-quality:
  stage: quality
  image: maven:3.9-eclipse-temurin-17
  script:
    - cd backend
    - mvn $MAVEN_CLI_OPTS checkstyle:check
  allow_failure: true
```

### Pipeline Features

#### 1. **Merge Request Pipeline**
- Runs automatically on every merge request
- Must pass before merge is allowed
- Shows test results and coverage directly in MR

#### 2. **Branch Protection Rules**
- Require pipeline to pass before merge
- Require code review approvals
- Prevent force push to main/master
- Require up-to-date branches

#### 3. **Test Coverage Requirements**
- Backend: Minimum 80% coverage
- Frontend: Minimum 70% coverage
- Coverage reports visible in GitLab
- Fails pipeline if coverage drops below threshold

#### 4. **Automated Checks**
- ✅ Code compilation (backend & frontend)
- ✅ Unit tests (all services, components)
- ✅ Integration tests (controllers, API endpoints)
- ✅ Repository tests (custom @Query methods)
- ✅ Code style checks (Checkstyle, ESLint)
- ✅ Test coverage reporting
- ✅ JUnit test reports

#### 5. **Docker Image Building** (Optional)
```yaml
docker-backend:
  stage: package
  image: docker:24
  services:
    - docker:24-dind
  script:
    - docker build -t $CI_REGISTRY_IMAGE/backend:$CI_COMMIT_SHORT_SHA ./backend
    - docker push $CI_REGISTRY_IMAGE/backend:$CI_COMMIT_SHORT_SHA
  only:
    - main
    - develop

docker-frontend:
  stage: package
  image: docker:24
  services:
    - docker:24-dind
  script:
    - docker build -t $CI_REGISTRY_IMAGE/frontend:$CI_COMMIT_SHORT_SHA ./frontend
    - docker push $CI_REGISTRY_IMAGE/frontend:$CI_COMMIT_SHORT_SHA
  only:
    - main
    - develop
```

### GitLab Project Settings

#### Merge Request Approvals
- Minimum 1 approval required
- Approvals reset on new commits

#### Pipeline Configuration
- Auto-cancel redundant pipelines
- Pipeline must succeed to merge
- Show pipeline status in MR widget

#### Branch Protection (main/master)
- Allowed to merge: Maintainers only
- Allowed to push: No one
- Require merge request
- Require pipeline to succeed

### CI/CD Best Practices

1. **Fast Feedback**: Pipeline completes in < 10 minutes
2. **Fail Fast**: Build stage before tests to catch compilation errors early
3. **Parallel Execution**: Run backend and frontend jobs in parallel
4. **Cache Dependencies**: Maven and npm packages cached between runs
5. **Test Database**: Use PostgreSQL service for integration tests
6. **Coverage Tracking**: Track coverage trends over time
7. **Artifact Management**: Store test reports and coverage for 7 days

### Local Testing Before Push
```bash
# Backend
cd backend
mvn clean test
mvn jacoco:report

# Frontend
cd frontend
npm test -- --coverage
npm run build

# Verify both pass locally before pushing
```

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
