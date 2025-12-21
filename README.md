# 🎵 Music Store Application

A full-stack music e-commerce application built with **Spring Boot** and **React**, featuring automated CI/CD pipeline for quality assurance.

## 📋 Project Overview

This is a university project demonstrating production-ready software development practices including:
- Full-stack web application (Backend + Frontend)
- Relational database design with complex relationships
- RESTful API with comprehensive documentation
- Automated testing with high coverage (80%+)
- **CI/CD pipeline with GitLab** for continuous integration
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
- **Code Quality**: Automated Checkstyle and ESLint validation
- **DTOs & Mappers**: Clean separation with MapStruct
- **Docker**: Containerized backend and frontend

## 🏗️ Architecture

### Backend (Spring Boot 3.x)
```
Controllers → Services → Repositories → Database
     ↓           ↓
   DTOs    Business Logic
```

- **Framework**: Spring Boot 3.x with Java 17+
- **Database**: PostgreSQL 15
- **ORM**: Spring Data JPA / Hibernate
- **API Docs**: Springdoc OpenAPI (Swagger)
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Coverage**: JaCoCo (80%+ required)
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
- **Coverage**: 70%+ required

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
- Java 17+
- Node.js 18+
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

The project includes a complete GitLab CI/CD pipeline that runs automatically on every commit:

### Pipeline Stages
1. **Build** - Compile backend and frontend
2. **Test** - Run all tests with coverage reports
3. **Quality** - Code quality checks (Checkstyle, ESLint)
4. **Package** - Build Docker images (main/develop branches only)

### Setup CI/CD
```bash
# Copy template to activate pipeline
cp docs/.gitlab-ci.yml.template .gitlab-ci.yml

# Configure branch protection in GitLab
# Settings → Repository → Branch Rules
# - Require pipeline to pass before merge
# - Require 1+ approval

# Push to trigger pipeline
git add .gitlab-ci.yml
git commit -m "ci: Enable CI/CD pipeline"
git push
```

**See [CI/CD Guide](docs/CI_CD_GUIDE.md) for complete setup instructions.**

## 📚 Documentation

Comprehensive documentation is available in the `docs/` folder:

| Document | Description |
|----------|-------------|
| [START_HERE.md](START_HERE.md) | **Start here** - Complete package overview |
| [docs/README_DOCUMENTATION.md](docs/README_DOCUMENTATION.md) | Master documentation index |
| [docs/PROJECT_PLAN.md](docs/PROJECT_PLAN.md) | Complete project plan and architecture |
| [docs/DATABASE_SCHEMA.md](docs/DATABASE_SCHEMA.md) | Database design with ERD and SQL |
| [docs/CI_CD_GUIDE.md](docs/CI_CD_GUIDE.md) | Complete CI/CD setup and usage guide |
| [docs/CI_CD_QUICK_REFERENCE.md](docs/CI_CD_QUICK_REFERENCE.md) | Quick reference for daily development |
| [docs/PROJECT_VISUAL_SUMMARY.md](docs/PROJECT_VISUAL_SUMMARY.md) | Visual diagrams and summaries |

## 🧪 Testing

### Test Coverage Requirements
- **Backend**: 80%+ coverage (enforced by CI/CD)
- **Frontend**: 70%+ coverage (enforced by CI/CD)

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

## 📊 Project Status

### Current Phase
- [ ] **Phase 0**: CI/CD Setup (In Progress)
- [ ] **Phase 1**: Backend Foundation
- [ ] **Phase 2**: Backend Services & API
- [ ] **Phase 3**: Backend Testing
- [ ] **Phase 4**: Frontend Setup
- [ ] **Phase 5**: Frontend Development
- [ ] **Phase 6**: Integration & Deployment

### Requirements Checklist
- [x] Many-to-Many relationship (Album ↔ Genre)
- [x] Many-to-One relationships (4+ relationships)
- [x] 5+ tables (8 tables planned)
- [ ] Code separation (Controllers, Services, Repositories)
- [ ] @Query annotations (5+ examples planned)
- [ ] Tests (Unit, Integration, Repository)
- [ ] Swagger API documentation
- [ ] DTOs & Mappers (MapStruct)
- [x] CI/CD Pipeline configuration

## 🛠️ Technology Stack

### Backend
- Spring Boot 3.x
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
- GitLab CI/CD
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
3. Push to GitLab (pipeline runs automatically)
4. Create merge request
5. Get code review approval
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

## 👥 Team / Contributors

- [Your Name] - Full Stack Developer

## 📝 License

This project is for educational purposes (University Project).

## 🎓 University Requirements

This project fulfills all requirements:
- ✅ Many-to-Many relationship (Album ↔ Genre)
- ✅ Many-to-One relationships (Album→Artist, Song→Album, Order→Customer, OrderItem→Order/Album)
- ✅ Minimum 5 tables (8 tables implemented)
- ✅ Code separation (Controllers, Services, Repositories, DTOs, Mappers)
- ✅ Custom @Query annotations (5+ examples)
- ✅ Comprehensive testing (Unit, Integration, Repository)
- ✅ Swagger API documentation
- ✅ DTOs with MapStruct mappers
- ✅ **BONUS: CI/CD Pipeline with automated testing**

## 🚀 Next Steps

1. **Read Documentation**: Start with [START_HERE.md](START_HERE.md)
2. **Setup CI/CD**: Follow [CI/CD Guide](docs/CI_CD_GUIDE.md)
3. **Begin Development**: Follow [Project Plan](docs/PROJECT_PLAN.md)
4. **Track Progress**: Use GitLab issues for each milestone

## 📞 Support

For questions or issues:
1. Check the [documentation](docs/)
2. Review [CI/CD Quick Reference](docs/CI_CD_QUICK_REFERENCE.md)
3. Check GitLab CI/CD logs for pipeline failures

---

**Built with ❤️ for learning and demonstrating production-ready development practices**

