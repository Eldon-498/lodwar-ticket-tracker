## Build stage.
FROM node:22-alpine AS builder
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci --no-audit --no-fund
COPY . .
RUN npm run build

## Runtime stage — nginx serves the SPA and proxies /api/ to the backend service.
FROM nginx:1.27-alpine
COPY --from=builder /app/dist/lodwar-ui/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
