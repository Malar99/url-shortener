# 🔗 URL Shortener - Spring Boot Application

A high-performance URL shortening service similar to Bit.ly, built with Spring Boot, Redis, and JPA. Features include analytics tracking, link expiration, and Redis caching for fast redirects.

## 🛠️ Tech Stack

**Backend:**
- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- Redis (Caching)
- H2 Database (Development)
- MySQL (Production-ready)

**Build & Tools:**
- Maven
- Spring Actuator (Monitoring)
- Lombok

## ✨ Key Features

- URL shortening with custom aliases
- Redirect with 301/302 status codes
- Click analytics (counts, referrers, devices)
- Link expiration (TTL support)
- Redis caching for high performance
- REST API documentation (Swagger/OpenAPI)
- API key authentication (Optional)

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven
- Redis server
- MySQL (optional)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Malar99/url-shortener.git
   cd url-shortener
2. Configure application:

bash
cp src/main/resources/application.example.properties src/main/resources/application.properties
Edit the properties file with your Redis and DB configs.

3. Run the application:

bash
mvn spring-boot:run
