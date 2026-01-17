# Docker Deployment Guide

## Quick Start

```bash
# Start all services
docker-compose up -d

# Access:
# Frontend: http://localhost:80
# Backend:  http://localhost:8081
# Swagger:  http://localhost:8081/swagger-ui.html

# Stop services
docker-compose down
```

## Docker Images

### Backend
- **Base**: `eclipse-temurin:11-jre-alpine`
- **Port**: 8081
- **Health**: `/actuator/health`
- **User**: Non-root (appuser:1001)

### Frontend
- **Base**: `nginx:alpine`
- **Port**: 80
- **Health**: `/health`

## Image Tagging (GitHub Actions)

| Tag | When |
|-----|------|
| `sha-abc123` | Every push |
| `main` / `develop` | Branch pushes |
| `v1.0.0` | Git tags |
| `latest` | Main branch |

## Environment Variables

### Backend
| Variable | Description |
|----------|-------------|
| `SPRING_DATASOURCE_URL` | PostgreSQL URL |
| `SPRING_DATASOURCE_USERNAME` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | DB password |
| `JWT_SECRET` | JWT signing key |
| `JWT_EXPIRATION` | Token expiry (ms) |

### Frontend
| Variable | Description |
|----------|-------------|
| `VITE_API_URL` | Backend API URL (build-time) |

## Pull from GitHub Container Registry

```bash
# Login
echo $GITHUB_TOKEN | docker login ghcr.io -u USERNAME --password-stdin

# Pull
docker pull ghcr.io/OWNER/REPO/backend:latest
docker pull ghcr.io/OWNER/REPO/frontend:latest
```

## Production Example

```yaml
services:
  backend:
    image: ghcr.io/your-org/musicplanet/backend:latest
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/musicplanet
      JWT_SECRET: your-production-secret
    
  frontend:
    image: ghcr.io/your-org/musicplanet/frontend:latest
    ports:
      - "80:80"
```
