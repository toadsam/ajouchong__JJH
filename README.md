# ajouchong Backend

아주대학교 총학생회 웹서비스를 위한 Spring Boot 백엔드입니다. 공지, 소개, 일정, 제휴, Q&A, 회칙 등 사용자/관리자 API를 제공하고 PostgreSQL과 Docker 배포 구성을 포함합니다.

## 프로젝트 개요

`ajouchong__JJH`는 총학생회 홈페이지와 관리자 화면에서 사용할 API 서버입니다. 사용자용 조회 API와 관리자용 관리 API를 분리하고, JWT 인증과 Spring Security 기반 보안 구성을 포함합니다.

## 주요 기능

- 공지사항 API
- 총학생회 소개글 API
- 일정 API
- 제휴 API
- Q&A API
- 회칙 API
- 관리자용 게시글 관리 API
- 사용자용 조회 API
- JWT 기반 인증 구조
- PostgreSQL 연동
- Docker Compose 기반 DB/서버/Nginx 구성

## 기술 스택

- Java 21
- Spring Boot `3.3.1`
- Spring Web
- Spring Security
- Spring Data JPA / JDBC
- PostgreSQL
- JWT
- Thymeleaf
- Gradle
- Docker / Docker Compose
- Nginx

## 폴더 구조

```text
.
├── src/main/java/com/ajouchong/
│   ├── controller/
│   │   ├── admin/
│   │   └── user/
│   ├── config/
│   ├── common/
│   └── AjouchongApplication.java
├── src/main/resources/application.yml
├── nginx/
├── scripts/
├── Dockerfile
├── docker-compose.yml
├── build.gradle
└── README.md
```

## 로컬 실행

```bash
./gradlew bootRun
```

Windows에서는 다음 명령을 사용할 수 있습니다.

```bash
gradlew.bat bootRun
```

기본 서버 포트는 `20232`입니다.

## Docker 실행

```bash
docker compose up --build
```

Docker Compose는 PostgreSQL, 백엔드, Nginx 프록시를 함께 실행하도록 구성되어 있습니다.

## 환경 변수

배포 또는 Docker 실행 시 다음 값을 환경에 맞게 설정해야 합니다.

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/ajouchong
SPRING_DATASOURCE_USERNAME=chongchong
SPRING_DATASOURCE_PASSWORD=change-me
JWT_SECRET=change-me
```

## 관련 저장소

- `toadsam/ajouchong-web`: 사용자용 React 프론트엔드
- `toadsam/ajouchong-admin----JJH`: 관리자용 React 프론트엔드

## 개발 메모

운영 환경에서는 DB 계정, JWT secret, 메일 설정 같은 민감 정보를 반드시 환경 변수 또는 별도 secret 관리 도구로 분리해야 합니다.
