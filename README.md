# hammer-internal

Hammer 플랫폼의 내부 어드민 서비스.

## 아키텍처

### Hexagonal Architecture (Ports & Adapters)

외부 의존성(DB, HTTP)과 핵심 비즈니스 로직을 포트 인터페이스로 분리하는 헥사고날 아키텍처를 채택했다. ArchUnit으로 레이어 간 의존성 규칙을 컴파일 타임에 강제한다.

```
                        ┌─────────────────────────────────┐
                        │         adapter.in.web           │
                        │   (REST Controllers, Requests)   │
                        └──────────────┬──────────────────┘
                                       │ implements
                        ┌──────────────▼──────────────────┐
                        │      application.port.in         │
                        │     (Use Case Interfaces)        │
                        └──────────────┬──────────────────┘
                                       │
                        ┌──────────────▼──────────────────┐
                        │      application.service         │
                        │   (Use Case Implementations)     │
                        │                                  │
                        │      application.dto             │
                        │  (Commands, Responses, Criteria) │
                        └──────────────┬──────────────────┘
                                       │ depends on
                        ┌──────────────▼──────────────────┐
                        │          domain                  │
                        │  (Entities, Value Objects, Enums) │
                        └──────────────┬──────────────────┘
                                       │
                        ┌──────────────▼──────────────────┐
                        │      application.port.out        │
                        │    (Persistence Port Interfaces)  │
                        └──────────────┬──────────────────┘
                                       │ implements
                        ┌──────────────▼──────────────────┐
                        │     adapter.out.persistence      │
                        │ (JPA Entities, Repos, Adapters)  │
                        └─────────────────────────────────┘
```

### 의존성 방향

```
adapter.in.web  ──▶  port.in  ◀──  service  ──▶  port.out  ◀──  adapter.out.persistence
                                      │
                                      ▼
                                   domain
```

- **Domain**: 순수 자바. Spring, JPA 등 프레임워크 의존성 없음
- **Application**: 어댑터에 의존하지 않음. JPA(`jakarta.persistence`) 직접 사용 금지
- **Port(in/out)**: 반드시 인터페이스
- **Adapter in ↔ out**: 서로 의존 금지
- **모듈 간**: 순환 의존 금지

### 모듈 구조

```
com.hammer.internal/
├── common/              # 공통 인프라 (예외 처리, CORS, 페이지네이션)
├── user/                # 사용자 조회
├── quiz/                # 퀴즈 CRUD + 키워드 검색
├── notification/        # 알림 템플릿 CRUD + 채널 필터 + 미리보기
├── errorlog/            # 에러 로그 조회 + 고급 필터링
└── dashboard/           # 대시보드 통계 집계
```

각 비즈니스 모듈의 패키지 구조:

```
{module}/
├── domain/                    # 도메인 모델, 값 객체, Enum
├── application/
│   ├── port/in/               # Use Case 인터페이스 (인바운드)
│   ├── port/out/              # Persistence Port 인터페이스 (아웃바운드)
│   ├── dto/                   # Command, Response, Criteria
│   └── service/               # Use Case 구현체 (@Service)
└── adapter/
    ├── in/web/                # REST Controller, Request DTO
    └── out/persistence/       # JPA Entity, Repository, Mapper, Adapter
```

### 접근 제어

대부분의 구현 클래스(Service, Controller, Adapter, Entity, Repository)는 **package-private**으로 선언한다. 외부에 공개되는 것은 포트 인터페이스와 DTO뿐이다.

### ArchUnit 규칙

| 카테고리 | 규칙 |
|---------|------|
| 레이어 의존성 | Domain → Application, Adapter 의존 금지 |
| | Domain → Spring, Jakarta 의존 금지 |
| | Application → Adapter 의존 금지 |
| | Application → `jakarta.persistence` 의존 금지 |
| | Adapter in ↔ out 상호 의존 금지 |
| 포트 계약 | `port.in`, `port.out` 패키지의 클래스는 반드시 인터페이스 |
| 구현 계약 | Service → Use Case 포트 구현 필수 |
| | PersistenceAdapter → Out 포트 구현 필수 |
| | Controller → `@RestController` 어노테이션 필수 |
| DI 규칙 | `@Autowired` 필드 주입 금지 (생성자 주입만 허용) |
| 모듈 | 모듈 간 순환 의존 금지 |

### 에러 처리

`GlobalExceptionHandler`가 모든 예외를 RFC 7807 `ProblemDetail` 형식으로 변환하고, 동시에 `error_logs` 테이블에 자동 기록한다.

| 예외 | HTTP 상태 | error_code |
|------|----------|------------|
| `NotFoundException` | 404 | NOT_FOUND |
| `IllegalArgumentException` | 400 | BAD_REQUEST |
| `MethodArgumentNotValidException` | 400 | VALIDATION_ERROR |
| 그 외 | 500 | INTERNAL_ERROR |

### 테스트 전략

- **단위 테스트**: Mockito 기반. Service, Controller(`@WebMvcTest`), PersistenceAdapter 각각 독립 테스트
- **통합 테스트**: Testcontainers(PostgreSQL)로 실제 DB 기반 E2E 테스트. `@Tag("integration")`으로 분리
- **아키텍처 테스트**: ArchUnit으로 레이어 규칙 강제
- **커버리지**: JaCoCo 기준 전체 70%, Service/Domain 80%

### 기술 스택

- Java 21, Spring Boot 3.5, PostgreSQL, Flyway, JPA/Hibernate
- ArchUnit 1.4, JUnit 5, Testcontainers, JaCoCo
- Spotless + Palantir Java Format
- SpringDoc OpenAPI 2.8 (Swagger 3)
