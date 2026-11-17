# RoomieSync Backend

Spring Boot REST API for RoomieSync.

## Requirements

- Java 17
- Maven 3.6.3+
- Docker Desktop with Docker Compose, or MySQL 8+

## Start the local database

From this repository:

```bash
docker compose up -d
```

Docker Compose starts MySQL 8.4 with a persistent volume and creates the `roomiesync` database.

## Run the API

```bash
mvn spring-boot:run
```

The API listens on `http://localhost:8080`.

## Local configuration

The defaults in `src/main/resources/application.properties` match the included Docker Compose service. Override them with environment variables when needed:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `PORT`

See `.env.example` for example values. Spring Boot does not automatically load a plain `.env` file; export these variables in your shell or configure them in your IDE run configuration.

## Stop MySQL

```bash
docker compose down
```

To remove the database and all its local data:

```bash
docker compose down -v
```

## Important security note

This repository is currently configured for local development. Before public deployment, implement password hashing, authentication and authorization, safe DTO responses, input validation, production CORS configuration, and database migrations. Do not use the sample local credentials in production.
