# Football League Standings Application

![Football League Standings Application](https://github.com/user-attachments/assets/5e55c1fc-184e-4f12-be33-4bbb7b6016d4)

## 1. Introduction

This microservice provides access to football league standings data. It's built using Java with the Spring Boot framework, following RESTful API principles, and is designed to be containerized with Docker.

### 1.1 Project Goals

- Provide a RESTful API to retrieve football league standings.
- Support fetching data from an external API (`apifootball.com`).
- Implement an offline mode to retrieve data from a cache when the external API is unavailable.
- Demonstrate SOLID principles, 12-Factor App methodology, HATEOAS, and relevant design patterns.
- Containerize the application with Docker for easy deployment.
- React application to access the microservice.

## 2. Features

- **RESTful API:**
  - `GET /api/v1/countries`: Retrieves a list of countries.
  - `GET /api/v1/league?countryId=${countryId}`: Retrieves a list of leagues based on a country id.
  - `GET /api/v1/standings?offlineMode=${mode}&leagueId=${leagueId}`: Retrieves league standings based on league id.
  - `GET /api/v1/team?leagueId=${leagueId}` Retrieves a list of teams in the league.
- **Data Source:** Fetches data from the `apifootball.com` API.
- **Offline Mode:**
  - If the `offlineMode` parameter is set to `true`, the application will attempt to retrieve data from an in-memory cache.
  - When online and data is fetched from the external API, it is stored in the cache.
- **HATEOAS:**
  - Responses include links to related resources (e.g., league details, team details) to improve API discoverability.

## 3. Technical Design

### 3.1 Architecture

The application follows a layered architecture:

- **Controller Layer:** Handles HTTP requests and responses.
- **Service Layer:** Contains the core business logic, including data retrieval and processing.
- **Client Layer:** Interacts with the external `apifootball.com` API.
- **DTOs:** Data Transfer Objects are used to structure request and response data.
- **Strategy Pattern:** Used to select the appropriate data fetching strategy (API or cache).

### 3.2 Key Technologies

- **Java:** Programming language
- **Spring Boot:** Framework for building the application
- **Spring Web (REST):** For building RESTful APIs
- **RestTemplate:** For making HTTP requests to the external API
- **Jackson:** For JSON processing (serialization/deserialization)
- **Lombok:** For reducing boilerplate code in DTOs
- **JUnit 5 & Mockito:** For testing
- **Maven:** Build tool
- **Docker:** Containerization
- **Spring HATEOAS:** For implementing HATEOAS in the API responses

### 3.3 Design Patterns

The following design pattern is used:

- **Strategy Pattern:**
  - The `StandingsService` uses the Strategy Pattern to choose between fetching standings data from the external API (`ApiFetchingStrategy`) or from the cache (`CacheFetchingStrategy`).
  - This provides flexibility and allows for easy addition of new data sources in the future.

### 3.4 SOLID Principles

The application adheres to the SOLID principles:

- **Single Responsibility Principle (SRP):** Classes have single, well-defined responsibilities. For example, `StandingsController` handles HTTP requests, `StandingsService` handles business logic, and `FootballApiClient` handles external API communication.
- **Open/Closed Principle (OCP):** The Strategy Pattern allows for extending the data fetching behavior without modifying the `StandingsService`. New strategies can be added without changing the core service logic.
- **Liskov Substitution Principle (LSP):** The `ApiFetchingStrategy` and `CacheFetchingStrategy` can be used interchangeably by the `StandingsService`.
- **Interface Segregation Principle (ISP):** Interfaces like `FootballApiClient` and `StandingsFetchingStrategy` are designed to be specific to their clients.
- **Dependency Inversion Principle (DIP):** The `StandingsService` depends on abstractions (`StandingsFetchingStrategy`) rather than concrete implementations. Dependencies are injected via the constructor.

### 3.5 12-Factor App

The application follows the principles of the 12-Factor App methodology:

1.  **Codebase:** The project is managed with Git.
2.  **Dependencies:** Dependencies are explicitly declared in `pom.xml`.
3.  **Config:** Configuration (e.g., API key) is stored in environment variables.
4.  **Backing Services:** The external API is treated as an attached resource.
5.  **Build, Release, Run:** The CI/CD pipeline separates these stages.
6.  **Stateless Processes:** The application is stateless.
7.  **Port Binding:** The application exports its service via port 8080.
8.  **Concurrency:** The application can be scaled horizontally.
9.  **Disposability:** The application starts up and shuts down quickly.
10. **Dev/Prod Parity:** Docker ensures consistency across environments.
11. **Logs:** The application writes logs to standard output.
12. **Admin Processes:** Administrative tasks (if any) are handled as separate processes.

### 3.6 HATEOAS

The API responses include HATEOAS links to related resources, enabling clients to discover and navigate the API dynamically. For example, the `/api/v1/standings` response includes links to league and team details.

### 3.7 TDD

The backend service was developed using Test-Driven Development (TDD). Tests were written before the implementation code to drive the design and ensure testability. See the `src/test/java` directory for the tests.

## 4. Getting Started

### 4.1 Prerequisites

- Java 17
- Maven 3.6+
- Docker
- A free API key from [https://www.api-football.com/](https://www.api-football.com/)

### 4.2 Installation

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/sidsid14/football-standings.git
    cd football-standings
    ```

    A. **Using Docker**

    - Docker images for both frontend and backend are available on docker hub.
      ```bash
      docker compose up -d
      ```
      B. **Local Development/Deployment**
    - Run Sprinboot application in backend.
    - Run React application in fronend.

    Access the UI at http://localhost

2.  **Set the API key:**
    - **Option 1: Environment Variable (Recommended for running with Docker):**
      ```bash
      export API_FOOTBALL_KEY="YOUR_API_KEY_HERE"
      ```
    - **Option 2: `application.properties` (for local development without Docker):**
      Create a file named `application.properties` in `backend/src/main/resources/` and add the following line:
      ```properties
      api.football.key=YOUR_API_KEY_HERE
      ```

### 4.3 Building the Application

#### 4.3.1 Building the Backend

```bash
cd backend
./mvnw clean package
docker build -t football-standings-backend .

#For docker hub
docker build -t sidsid14/football-standings-backend .

docker push sidsid14/football-standings-backend
```

This will compile the Java code and create an executable JAR file in the backend/target/ directory.

#### 4.3.2 Building the Frontend

```bash
cd frontend
npm install
npm run build
docker build -t football-standings-frontend .
```

### 4.4 Running the Application

#### 4.4.1 Running with Docker Compose (Recommended)

Ensure you are in the root directory of the project (where docker-compose.yml is located).

Start the services:

```bash
docker-compose up -d
```

The application will be accessible at http://localhost.

#### 4.4.2 Running the Backend Locally (Without Docker)

Ensure you have set the API_FOOTBALL_KEY in application.properties or as an environment variable.

Run the Spring Boot application:

```bash
cd backend
./mvnw spring-boot:run
```

The backend will be accessible at http://localhost:8080. You will also need to serve the static assets from the frontend separately.

### 5. API Endpoints

GET List of countries [/api/v1/countries](http://localhost:8080/api/v1/countries)

GET Leagues in a country [/api/v1/league?countryId=44](http://localhost:8080/api/v1/league?countryId=44)

GET Standings by league id [/api/v1/standings?offlineMode=false&leagueId=149](http://localhost:8080/api/v1/standings?offlineMode=false&leagueId=149)

GET teams by league id [/api/v1/team?leagueId=149](http://localhost:8080/api/v1/team?leagueId=149)

### 6. Testing

The backend service includes unit tests written with JUnit 5 and Mockito. To run the tests:

```bash
cd backend
./mvnw test
```

### 7. Deployment

The application is designed to be deployed using Docker. A Dockerfile is provided for the backend. For production deployments, consider using a container orchestration platform like Kubernetes.

### 8. Caching

The application uses an in-memory cache (ConcurrentHashMap) to store standings data. When offlineMode is enabled, the application attempts to retrieve data from the cache. When data is fetched from the external API, it is also stored in the cache. The cache is not persistent across application restarts.

### 9. Further Development

Implement a more robust cache
