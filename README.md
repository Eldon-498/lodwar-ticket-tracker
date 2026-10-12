# Lodwar Services — Mini Ticket Tracker

A take-home for Lodwar Services: a small ticket tracker with a Spring Boot REST API, MySQL, and an Angular 21 SPA.

Mono-repo layout:

```
.
├── backend/   Spring Boot 3.2 + Gradle + Flyway + MySQL
└── frontend/  Angular 21 (NgModules) + reactive forms + SCSS
```

## Prerequisites

- JDK 17+ (tested with Java 17 and Java 24)
- Node 20+ and npm 10+
- Docker (for the MySQL container; `make` is optional)

## Quickstart

Open two terminals.

### 1. Backend

```bash
cd backend
make start                 # or:  docker-compose up -d
./gradlew bootRun
```

The API comes up at **http://localhost:8082/api**. Flyway runs the migration on first start and creates the `ticket` table in the `lodwar` database.

Teardown:

```bash
make stop     # pause the container
make down     # remove it entirely
```

### 2. Frontend

```bash
cd frontend
npm install
npm start
```

The UI is served at **http://localhost:4200/tickets**. It expects the API at `http://localhost:8082/api` (configured in `src/environments/environment.ts`).

### 3. Tests

The backend integration tests also use the dockerized MySQL (schema `lodwar_test` is created on first run — the init script grants `lodwar` user access to all `lodwar_*` schemas).

```bash
cd backend
make start                 # MySQL must be running
./gradlew test
```

## API

Base path: `/api`

| Method | Path                       | Purpose                                                 |
|--------|----------------------------|---------------------------------------------------------|
| POST   | `/tickets`                 | Create a ticket                                         |
| GET    | `/tickets?status=&page=&size=` | List tickets, optional `status` filter + pagination |
| PATCH  | `/tickets/{id}`            | Update a ticket's `status` and/or `priority`            |
| GET    | `/tickets/stats`           | Count of tickets grouped by status                      |

**Ticket shape**

```json
{
  "id": 1,
  "title": "Printer offline",
  "description": "Second-floor printer unreachable",
  "status": "OPEN",            // OPEN | IN_PROGRESS | CLOSED
  "priority": "HIGH",          // LOW | MEDIUM | HIGH
  "createdAt": "2026-10-12T20:41:22.358Z",
  "updatedAt": "2026-10-12T20:41:22.358Z"
}
```

**Error shape** (uniform envelope from the global `@RestControllerAdvice`):

```json
{ "code": "2001", "message": "Ticket with id [42] not found." }
```

Validation errors on `POST /tickets` return 400 with the field name and reason (e.g. `"title is required"`).

## Project layout

### Backend (`backend/src/main/java/ke/co/ssd/lodwar/`)

```
LodwarTicketTrackerApplication.java    @SpringBootApplication + @EnableJpaAuditing
config/
  CorsConfig.java                      allows http://localhost:4200
  ExceptionHandlerConfig.java          global @RestControllerAdvice
exceptions/                            ServiceException + typed subclasses
  response/                            ErrorResponseEnum, ErrorResponseModel
utils/
  AuditableDateEntity.java             @CreatedDate / @LastModifiedDate base
  SpecificationUtils.java              AND combiner for optional predicates
ticket/
  controller/TicketController.java     /api/tickets endpoints
  dto/request/                         CreateTicketRequestDTO, UpdateTicketRequestDTO, TicketRequestDTO (filter)
  dto/response/                        TicketResponseDTO, TicketStatsResponseDTO
  entity/
    Ticket.java                        extends AuditableDateEntity
    enums/TicketStatus.java            OPEN | IN_PROGRESS | CLOSED
    enums/TicketPriority.java          LOW | MEDIUM | HIGH
  factory/TicketFactory.java           static Ticket builder from request DTO
  repository/
    TicketRepository.java              JpaRepository + JpaSpecificationExecutor + stats query
    specification/TicketSpecification.java   dynamic status filter
  service/
    TicketService.java                 interface
    impl/TicketServiceImpl.java        @Transactional writes, read-only reads
```

Migrations live in `src/main/resources/migrations/V{timestamp}__Desc.sql` (Flyway owns the schema; `spring.jpa.hibernate.ddl-auto=none`).

### Frontend (`frontend/src/app/`)

```
app.module.ts, app-routing.module.ts   root module with lazy-loaded tickets route
app.component.*                        shell with <router-outlet>
services/
  http/http.service.ts                 wraps HttpClient, prepends environment.API_BASE_URL
  apis/ticket/ticket.service.ts        create / list / update / stats
modules/tickets/
  tickets-module.ts, tickets-routing-module.ts
  models/ticket.model.ts               TS types for API payloads
  page/tickets-page.component.*        header, stats cards, hosts child components
  create/ticket-create.component.*     reactive form with validation + toast
  list/ticket-list.component.*         table + status filter + inline status change + pagination
```

## Decisions & Tradeoffs

- **Scope discipline.** The brief said 3–4 focused hours and "please don't over-build," so I limited features to exactly what was specified: create, list with status filter + pagination, patch status/priority, stats. No auth, no comments, no edit-title, no soft delete, no roles.
- **Flyway owns the schema.** `ddl-auto` is `none` in all profiles — the SQL migration is the single source of truth. Hibernate's `validate` mode trips on `@Enumerated(STRING)` because MySQL stores it as `VARCHAR` while Hibernate 6 wants `ENUM`, so `validate` would create more friction than it prevents here.
- **Enums as `VARCHAR(20)` with `@Enumerated(STRING)`.** Human-readable in the DB and safe to reorder without breaking data; the tradeoff is one more byte per row vs. an ordinal column, which doesn't matter at this scale.
- **Pagination via Spring's `Pageable`.** `GET /tickets` accepts `page` and `size` (plus `sort`) — the standard Spring envelope ships `totalElements`, `totalPages`, etc. without extra code. The UI reads `.content` and the page metadata directly.
- **Specification pattern for the status filter.** Overkill for a single optional field, but it matches the repo style this project is modeled on and makes adding another filter (e.g. priority, created-after) a one-line change.
- **Integration tests hit a real MySQL (not H2, not Testcontainers).** I started with Testcontainers but Docker Desktop on the test machine was returning a stub `/info` response that broke client discovery. Rather than ship a fragile setup, I pointed the tests at the same docker-compose MySQL used for dev, with a separate `lodwar_test` database that's created on first connect and truncated between tests. This matches the pattern used in the reference project and keeps the tests honest — same engine, same migrations, no in-memory surprises.
- **CORS at `http://localhost:4200` only.** Enough for the SPA in dev; a prod deployment would route the UI from the same origin as the API (or lock CORS down to the real domain).
- **With more time I would:** add a `priority` filter + sort on `GET /tickets`; add service-layer unit tests to cover `UnprocessableEntityException` branches without Spring context; add OpenAPI/springdoc so the UI could regenerate its DTOs; swap my hand-rolled dark theme for PrimeNG components (I imported `primeng` but didn't need its components for this scope); and front the API with Caddy / nginx so dev and prod CORS behave the same.
