# 📁 File Service

Kotlin + Spring Boot + PostgreSQL 기반 이미지 파일 업로드 서비스 (v1.0)

## 기술 스택

| 구분 | 기술                  |
|------|---------------------|
| Language | Kotlin 2.2          |
| Framework | Spring Boot 4.0.3   |
| Database | PostgreSQL 16       |
| ORM | Spring Data JPA     |
| Image Processing | Thumbnailator       |
| Build | Gradle (Kotlin DSL) |
| Container | Podman / Docker     |

## 기능

- ✅ 이미지 업로드 (JPEG, PNG, GIF, WebP)
- ✅ 썸네일 자동 생성 (200x200, 비동기)
- ✅ 파일 다운로드
- ✅ 메타데이터 조회 (PostgreSQL 저장)
- ✅ 파일 삭제
- ✅ 파일 검증 (크기, MIME 타입, 확장자)

## 계층 설명

| 계층 | 패키지 | 역할 |
|------|--------|------|
| **Presentation** | `presentation` | REST API 엔드포인트, 요청/응답 처리, 입력 검증 |
| **Application** | `application` | 비즈니스 로직 조율, DTO 변환, 서비스 |
| **Domain** | `domain` | 핵심 비즈니스 엔티티, 리포지토리 인터페이스 |
| **Infrastructure** | `infrastructure` | 외부 시스템 연동 (DB, 파일시스템, 설정) |
| **Global** | `global` | 전역 공통 (예외, 응답 래퍼, 핸들러) |

## 실행 방법

### 1. PostgreSQL 실행 (Podman)

```bash
# 스크립트로 실행 (권장)
chmod +x scripts/start-db.sh
./scripts/start-db.sh
 
# 또는 수동 실행
podman run -d \
  --name file-service-db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=file_service \
  -p 5432:5432 \
  postgres:16-alpine
 
# 마이그레이션 실행
podman exec -i file-service-db psql -U postgres -d file_service \
  < src/main/resources/db/migration/V1__create_files_table.sql
```

### 2. 애플리케이션 실행

```bash
# Gradle로 실행
./gradlew bootRun
 
# 또는 JAR 빌드 후 실행
./gradlew bootJar
java -jar build/libs/file-service-1.0.0.jar
```

### Docker Compose로 전체 실행

```bash
# 빌드 및 실행 (PostgreSQL + App)
docker-compose up -d --build
 
# 로그 확인
docker-compose logs -f
 
# 중지
docker-compose down
```

## API 명세

> 모든 응답은 `RestApiResponse` 래퍼로 통일됩니다.

### 1. 파일 업로드

```bash
POST /api/v1/files/upload
Content-Type: multipart/form-data
```

**Request**
- `file`: 업로드할 이미지 파일 (필수)
- `generateThumbnail`: 썸네일 생성 여부 (기본: true)

```bash
curl -X POST http://localhost:8080/api/v1/files/upload \
  -F "file=@./image.jpg"
```

**Response (201 Created)**
```json
{
  "message": "CREATED",
  "status": 201,
  "data": {
    "fileId": "550e8400-e29b-41d4-a716-446655440000",
    "originalName": "image.jpg",
    "mimeType": "image/jpeg",
    "size": 1048576,
    "url": "http://localhost:8080/api/v1/files/550e8400-e29b-41d4-a716-446655440000",
    "thumbnailUrl": "http://localhost:8080/api/v1/files/550e8400-e29b-41d4-a716-446655440000/thumbnail",
    "createdAt": "2024-03-15T10:30:00"
  }
}
```

### 2. 파일 다운로드

```bash
GET /api/v1/files/{fileId}
```

```bash
curl -O http://localhost:8080/api/v1/files/{fileId}
```

> 바이너리 파일 응답 (RestApiResponse 래퍼 없음)

### 3. 썸네일 다운로드

```bash
GET /api/v1/files/{fileId}/thumbnail
```

> 바이너리 파일 응답 (RestApiResponse 래퍼 없음)

### 4. 메타데이터 조회

```bash
GET /api/v1/files/{fileId}/metadata
```

**Response (200 OK)**
```json
{
  "message": "OK",
  "status": 200,
  "data": {
    "fileId": "550e8400-e29b-41d4-a716-446655440000",
    "originalName": "image.jpg",
    "storedName": "550e8400-e29b-41d4-a716-446655440000.jpg",
    "mimeType": "image/jpeg",
    "size": 1048576,
    "width": 1920,
    "height": 1080,
    "url": "http://localhost:8080/api/v1/files/550e8400...",
    "thumbnailUrl": "http://localhost:8080/api/v1/files/550e8400.../thumbnail",
    "hasThumbnail": true,
    "createdAt": "2024-03-15T10:30:00"
  }
}
```

### 5. 파일 삭제

```bash
DELETE /api/v1/files/{fileId}
```

**Response (204 No Content)**
```json
{
  "message": "NO_CONTENT",
  "status": 204,
  "data": null
}
```

### 6. 헬스체크

```bash
GET /health
```

**Response (200 OK)**
```json
{
  "message": "OK",
  "status": 200,
  "data": {
    "status": "ok",
    "service": "file-service"
  }
}
```

## 아키텍처

```
┌──────────────────────────────────────────────────────────────┐
│                       Controller Layer                       │
│  ┌────────────────────────────────────────────────────────┐  │
│  │                   FileController                       │  │
│  │  upload / download / thumbnail / metadata / delete     │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────┐
│                        Service Layer                         │
│  ┌─────────────────────┐  ┌────────────────────────────────┐ │
│  │     FileService     │  │        ImageProcessor          │ │
│  │  + FileValidator    │  │  (Thumbnailator 기반 비동기)      │ │
│  └─────────────────────┘  └────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────┘
                            │
        ┌───────────────────┴───────────────────┐
        │                                       │
┌───────▼───────┐                       ┌───────▼───────┐
│  Repository   │                       │  LocalStorage │
│  (JPA)        │                       │  (파일시스템)    │
└───────┬───────┘                       └───────┬───────┘
        │                                       │
        ▼                                       ▼
┌──────────────┐                       ┌──────────────┐
│  PostgreSQL  │                       │  File System │
└──────────────┘                       └──────────────┘
```

## v2.0 예정 기능

- [ ] MinIO (S3 호환) 스토리지
- [ ] Presigned URL 지원
- [ ] 이미지 포맷 변환 (WebP)
- [ ] gRPC 서버 추가

