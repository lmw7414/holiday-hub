# 🗓️ Holiday-Hub
### 공공 API를 통해 국가별 공휴일 정보를 조회/갱신/삭제할 수 있는 서비스입니다.  

## 1. 빌드 & 실행 방법
>1. 환경 java 21 버전 체크
>2. 프로젝트 클론
>3. 실행
>```bash
>git clone https://github.com/lmw7414/holiday-hub.git
>cd holiday-hub
>./gradlew bootRun
>```

## 2. 설계한 REST API 명세 요약
### 1. 공휴일 API
#### 1. 공휴일 정보 검색하기
```aiignore
GET /api/holidays
```
**요청 파라미터**

| 파라미터           | 설명     | 예시                                | 필수 여부 |
| -------------- | ------ | --------------------------------- | ----- |
| `from`         | 조회 시작일 | `2024-01-01`                      | ❌     |
| `to`           | 조회 종료일 | `2024-12-31`                      | ❌     |
| `countryCode`  | 국가 코드  | `KR`, `US`, ...                   | ❌     |
| `type`         | 공휴일 타입 | `PUBLIC`, `BANK`, `OPTIONAL`, ... | ❌     |
| `page`, `size` | 페이징    | `0`, `20`                         | ❌     |

<details>
<summary>응답 예시</summary>
<div markdown="1">

```json
{
  "content": [
    {
      "date": "2020-01-01",
      "localName": "새해",
      "name": "New Year's Day",
      "countryCode": "KR",
      "fixed": false,
      "global": true,
      "counties": null,
      "launchYear": null,
      "types": [
        "PUBLIC"
      ]
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 1,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "last": false,
  "totalPages": 76,
  "totalElements": 76,
  "first": true,
  "size": 1,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "numberOfElements": 1,
  "empty": false
}
```

</div>
</details>

#### 2. 공휴일 정보 덮어쓰기
```aiignore
PUT /api/holidays/sync
```
**요청 파라미터**

| 파라미터          | 설명     | 예시                          | 필수 여부 |
|---------------|--------| --------------------------- | --- |
| `year`        | 덮어쓸 년도 | `2024`                      |   ⭕  |
| `countryCode` | 국가 코드  | `KR`, `US`, ...             | ⭕    |

#### 2. 공휴일 정보 지우기
```aiignore
DELETE /api/holidays
```
**요청 파라미터**

| 파라미터          | 설명     | 예시                          | 필수 여부 |
|---------------|--------| --------------------------- | --- |
| `year`        | 덮어쓸 년도 | `2024`                      |   ⭕  |
| `countryCode` | 국가 코드  | `KR`, `US`, ...             | ⭕    |

### 2. 국가 API
#### 1. 국가 정보 전체 조회

<details>
<summary>요청 파라미터</summary>
<div markdown="1">

```json
[
  {
    "countryCode": "string",
    "name": "string"
  }
]
```

</div>
</details>

## 🔍 Swagger UI 확인
Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/api-docs

## 📂 기술 스택
- Spring Boot 3.4.7
- Java 21
- JPA + H2 + QueryDSL
- Swagger (springdoc-openapi)
- Scheduler (Spring @Scheduled)

### 테스트 성공 스크린 샷
![테스트_성공_스크린샷](/document/test_success_screenshot.png)