# 🎵 Music Store Application

A full-stack music e-commerce application built with **Spring Boot** and **React**, featuring automated CI/CD pipeline for quality assurance.

## 📋 Project Overview

This is a university project demonstrating production-ready software development practices including:
- Full-stack web application (Backend + Frontend)
- Relational database design with complex relationships
- RESTful API with comprehensive documentation
- Automated testing with high coverage (80%+)
- **CI/CD pipeline with GitHub Actions** for continuous integration
- Docker containerization for deployment

## 🎯 Features

### Music Store Functionality
- 🎵 Browse albums by artist, genre, and price
- 🔍 Search functionality across albums and artists
- 🛒 Shopping cart management
- 📦 Order processing and tracking
- 👤 Customer management
- 📊 Inventory management

### Technical Features
- **Database**: 8 tables with Many-to-Many and Many-to-One relationships
- **REST API**: 30+ endpoints with Swagger documentation
- **Testing**: Unit, Integration, and Repository tests (80%+ coverage)
- **CI/CD**: Automated build, test, and quality checks on every commit
- **Code Quality**: Automated Checkstyle validation
- **DTOs & Mappers**: Clean separation with MapStruct
- **Docker**: Containerized backend and frontend

## 🏗️ Architecture

### Backend (Spring Boot 2.7)
```
Controllers → Services → Repositories → Database
     ↓           ↓
   DTOs    Business Logic
```

- **Framework**: Spring Boot 2.7.18 with Java 11
- **Database**: PostgreSQL 15
- **ORM**: Spring Data JPA / Hibernate
- **API Docs**: Springdoc OpenAPI (Swagger)
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Coverage**: JaCoCo (tracked, not enforced)
- **Build**: Maven

### Frontend (React 18+)
```
Pages → Components → Services → API
   ↓         ↓          ↓
Context   Hooks     Axios
```

- **Framework**: React 18+
- **Build Tool**: Vite
- **State**: React Context API
- **HTTP Client**: Axios
- **UI**: Material-UI / TailwindCSS
- **Testing**: Jest, React Testing Library
- **Coverage**: Tracked (not enforced)

### Database Schema
8 tables with proper relationships:
- **Artists** (1:N with Albums)
- **Albums** (M:N with Genres, 1:N with Songs)
- **Songs** (N:1 with Albums)
- **Genres** (M:N with Albums)
- **Customers** (1:N with Orders)
- **Orders** (1:N with OrderItems)
- **OrderItems** (N:1 with Orders and Albums)
- **AlbumGenre** (Junction table)

## 🚀 Quick Start

### Prerequisites
- Java 11+
- Node.js 18+ (for frontend, when implemented)
- PostgreSQL 15
- Maven 3.9+
- Git

### Setup

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd MusicPlanet
   ```

2. **Setup Database**
   ```bash
   # Create database
   createdb music_store_db
   
   # Update connection in src/main/resources/application.properties
   ```

3. **Backend Setup**
   ```bash
   # Install dependencies and run tests
   mvn clean install
   
   # Run application
   mvn spring-boot:run
   
   # API will be available at: http://localhost:8080
   # Swagger UI: http://localhost:8080/swagger-ui.html
   ```

4. **Frontend Setup** (when implemented)
   ```bash
   cd frontend
   npm install
   npm run dev
   
   # App will be available at: http://localhost:5173
   ```

5. **Run Tests**
   ```bash
   # Backend tests with coverage
   mvn clean test jacoco:report
   # View coverage: target/site/jacoco/index.html
   
   # Frontend tests with coverage
   cd frontend
   npm run test:coverage
   # View coverage: coverage/lcov-report/index.html
   ```

## 🔄 CI/CD Pipeline

The project includes a complete **GitHub Actions** pipeline that runs automatically on every commit:

### Pipeline Jobs
1. **Build Backend** - Compile code with Maven
2. **Run Tests** - Execute all tests with PostgreSQL service
3. **Code Quality Checks** - Run Checkstyle validation
4. **Pipeline Summary** - Display results

### How It Works
- ✅ Runs automatically on push and pull requests
- ✅ Must pass before merging (if branch protection enabled)
- ✅ Generates test reports and coverage
- ✅ Results visible in Pull Request checks

---

## 🛡️ Branch Protection Rules

The `main` branch is protected with the following rules to ensure code quality:

### Protection Settings

#### ✅ Pull Request Requirements
- **Require pull request before merging**: All changes must go through PR
- **Require approvals**: Minimum 1 approval required
- **Dismiss stale approvals**: Approvals reset when new commits are pushed

#### ✅ Status Checks Required
Before merging, these checks must pass:
- ✅ **Build Backend** - Code must compile
- ✅ **Run Tests** - All tests must pass
- ✅ **Code Quality Checks** - Checkstyle validation
- ✅ **Pipeline Summary** - Overall pipeline status

#### ✅ Additional Protections
- **Require branches to be up to date**: Must merge latest main first
- **Automatically delete head branches**: Clean up after merge
- **Do not allow bypassing**: Even admins must follow rules

### Development Workflow

```bash
# 1. Create feature branch
git checkout -b feature/my-feature

# 2. Make changes and commit
git add .
git commit -m "feat: Add new feature"

# 3. Push to GitHub
git push origin feature/my-feature

# 4. Create Pull Request on GitHub
# 5. Pipeline runs automatically ✅
# 6. Request review from team member
# 7. Address feedback if needed
# 8. Once approved and checks pass → Merge!
```

### Pull Request Checklist

Before your PR can be merged:
- ✅ All CI/CD checks must pass (Build, Test, Quality)
- ✅ At least 1 approval from team member
- ✅ Branch must be up to date with main
- ✅ All review comments addressed

**Result**: Only high-quality, tested code makes it to `main`! 🎯

## 📚 Documentation

Comprehensive documentation is available in the `docs/` folder:

| Document | Description |
|----------|-------------|
| [docs/PROJECT_PLAN.md](docs/PROJECT_PLAN.md) | Complete project plan and architecture |
| [docs/DATABASE_SCHEMA.md](docs/DATABASE_SCHEMA.md) | Database design with ERD and SQL |
| [docs/PROJECT_VISUAL_SUMMARY.md](docs/PROJECT_VISUAL_SUMMARY.md) | Visual diagrams and summaries |

## 🧪 Testing

### Test Coverage Requirements
- **Backend**: Coverage tracked (not enforced)
- **Frontend**: Coverage tracked (not enforced)

### Test Types
- **Unit Tests**: Test individual components/services
- **Integration Tests**: Test API endpoints with MockMvc
- **Repository Tests**: Test custom @Query methods

### Running Tests Locally
```bash
# Backend
mvn test                          # Run all tests
mvn test -Dtest=AlbumServiceTest  # Run specific test
mvn jacoco:report                 # Generate coverage report

# Frontend
npm test                          # Run all tests
npm run test:coverage             # Run with coverage
```

## 🛠️ Technology Stack

### Backend
- Spring Boot 2.7.18
- Spring Data JPA
- PostgreSQL 15
- MapStruct
- Lombok
- Swagger/OpenAPI
- JUnit 5 & Mockito
- JaCoCo

### Frontend
- React 18+
- Vite
- Axios
- React Router
- Material-UI / TailwindCSS
- Jest
- React Testing Library

### DevOps
- GitHub Actions
- Docker & Docker Compose
- Maven
- npm

## 📈 Development

### Branching Strategy
```
main (protected)
  ↑
  └── feature/album-service
  └── feature/shopping-cart
  └── feature/order-processing
```

### Workflow
1. Create feature branch from `main`
2. Make changes and commit
3. Push to GitHub (pipeline runs automatically)
4. Create Pull Request
5. Get code review approval (minimum 1)
6. Merge when pipeline passes ✅

## 🐳 Docker Deployment

```bash
# Build and run with Docker Compose
docker-compose up -d

# Services will be available at:
# Backend API: http://localhost:8080
# Frontend: http://localhost:3000
# PostgreSQL: localhost:5432
```

## 📝 License

This project is for educational purposes (University Project).

