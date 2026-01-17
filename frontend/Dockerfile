# Frontend Dockerfile 
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
ARG VITE_API_URL=http://localhost:8081/api
ENV VITE_API_URL=${VITE_API_URL}
RUN npm run build

FROM nginx:alpine AS runtime
RUN apk add --no-cache curl
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl -f http://localhost:80/ || exit 1
CMD ["nginx", "-g", "daemon off;"]
