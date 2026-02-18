# GitHub Activity Tracker - Spring Boot

A Spring Boot application that fetches and displays recent GitHub user activity through a REST API.

## Requirements

- Java 17+
- Maven 3.6+

## Running the Application

```bash
cd github-activity-spring
./mvnw spring-boot:run
```

Or build and run the JAR:

```bash
./mvnw clean package
java -jar target/github-activity-tracker-1.0.0.jar
```

## API Endpoints

### Get User Activity (Formatted)
```
GET /api/activity/{username}
```

Example response:
```json
[
  "- Pushed commits to kamranahmedse/developer-roadmap",
  "- Starred kamranahmedse/developer-roadmap",
  "- Opened a pull request in kamranahmedse/developer-roadmap"
]
```

### Get Raw Activity (JSON)
```
GET /api/activity/{username}/raw
```

Returns the raw GitHub event data as JSON.

## Error Handling

- **404 Not Found**: When the GitHub user doesn't exist
- **500 Internal Server Error**: When GitHub API is unavailable or rate limited

## Configuration

Edit `src/main/resources/application.properties`:

```properties
server.port=8081
github.api.url=https://api.github.com/users/%s/events
```
