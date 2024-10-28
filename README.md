# 프로젝트 이름

API 서버 - 사용자 및 역할 관리 애플리케이션

## 설명

이 프로젝트는 사용자를 관리하고 역할을 관리하는 Spring Boot 애플리케이션입니다. 사용자 인증, 역할 관리 및 안전한 비밀번호 저장과 같은 기능을 포함하고 있습니다.

"포트폴리오 갤러리" 프로젝트를 시작하기 전, 기본 뼈대 백엔드 프로젝트로 계획하고 있습니다.
대부분의 페이지에서 필수적으로 많이 사용되는 기법들을 연습해볼 생각입니다.

## 사용 기술

- Java
- Spring Boot
- Swagger
- Spring Security
- Spring Data JPA
- QueryDSL
- Gradle
- JWT

## 시작하기

### 필수 조건

- Java 17 이상
- Gradle 7.5 이상

### 설치

1. 저장소를 클론합니다:
    ```sh
    git clone https://github.com/NewChoBo/api-server.git
    ```
2. 프로젝트 디렉토리로 이동합니다:
    ```sh
    cd api-server
    ```
3. 프로젝트를 빌드합니다:
    ```sh
    ./gradlew build
    ```

### 애플리케이션 실행

1. 애플리케이션을 실행합니다:
    ```sh
    ./gradlew bootRun
    ```
2. 애플리케이션은 `http://localhost:8080`에서 사용할 수 있습니다.

## API 엔드포인트

이 프로젝트의 API 엔드포인트에 대한 자세한 내용은 Swagger 문서를 참조하십시오.

Swagger 페이지 주소는 다음과 같습니다: [Swagger UI](http://localhost:8080/swagger-ui/index.html)

## 보안

비밀번호는 데이터베이스에 저장되기 전에 `BCryptPasswordEncoder`를 사용하여 안전하게 해싱됩니다.