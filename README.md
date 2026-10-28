# 우리의 기록

웹서비스설계 JWT 게시판 과제로 개발한 웹 애플리케이션입니다.

## 작성자와 서비스 소개

- 학번 / 이름: [본인 학번과 이름 입력]
- 서비스 이름: 우리의 기록
- 선택한 기술: Vue + Spring Boot
- 데이터 저장: SQLite

### 서비스 소개

'우리의 기록'은 사용자가 자신의 게시글을 작성하고 관리할 수 있는 JWT 인증 기반 웹 게시판입니다.

사용자는 로그인하지 않아도 게시글 목록과 상세 내용을 조회할 수 있습니다. 로그인한 사용자는 새로운 게시글을 작성할 수 있으며, 자신이 작성한 게시글만 수정하거나 삭제할 수 있습니다.

JWT를 이용해 사용자를 인증하고, 서버에서 게시글 작성자와 로그인한 사용자를 비교하여 접근 권한을 검사합니다.

### 주요 기능

- Alice/Bob 테스트 계정을 이용한 로그인
- JWT 액세스 토큰 발급 및 인증
- 게시글 목록 및 상세 조회
- 로그인한 사용자의 게시글 작성
- 작성자 본인의 게시글 수정 및 삭제
- 다른 사용자의 게시글 수정·삭제 차단
- 토큰 만료 및 인증 오류 처리
- SQLite를 이용한 게시글 데이터 저장

## 프로젝트 구조

```text
JWT-Assignment/
├── README.md                 # 프로젝트 소개 및 실행 안내
├── backend-spring/           # Spring Boot 백엔드
│   ├── src/                  # Java 소스 코드
│   ├── pom.xml               # Maven 의존성 설정
│   └── mvnw.cmd              # Windows용 Maven Wrapper
├── frontend-vue/             # Vue 프론트엔드
│   ├── src/
│   │   ├── App.vue           # 게시판 화면 및 사용자 기능
│   │   └── api.js            # API 요청 및 JWT 토큰 관리
│   ├── package.json          # 프론트엔드 실행 및 빌드 설정
│   └── vite.config.js        # Vite 및 API 프록시 설정
├── tools/
│   └── check_api.py          # API 자동 테스트 도구
└── docs/                     # 과제 문서 및 제출 자료
```

백엔드는 Spring Boot를 사용하여 JWT 인증, 게시글 관리, 작성자 권한 검사를 처리합니다.

프론트엔드는 Vue를 사용하여 로그인과 게시글 작성·조회·수정·삭제 화면을 제공합니다.

게시글과 사용자 정보는 SQLite 파일 데이터베이스에 저장합니다.


## 실행 환경

| 항목 | 사용 버전 |
|---|---|
| 운영체제 | Windows |
| 프론트 런타임 | Node.js 22.14.0 |
| 프론트 프레임워크 | Vue 3.5.43 |
| 백엔드 런타임 | JDK 22 |
| 백엔드 프레임워크 | Spring Boot 3.5.16 |
| 데이터 저장 | SQLite |
| 프론트 빌드 도구 | Vite |
| 백엔드 빌드 도구 | Maven Wrapper |


## 설치와 실행

### 1. 백엔드 준비 및 실행

Spring Boot 백엔드는 JDK 17 이상이 필요하며, 본 프로젝트에서는 JDK 22를 사용했습니다.

Windows PowerShell에서 프로젝트의 `backend-spring` 폴더로 이동합니다.

```powershell
cd backend-spring
```

JWT 서명에 사용할 비밀 키를 생성하고 환경 변수로 설정합니다.

```powershell
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET = [Convert]::ToHexString($bytes)
$env:TOKEN_TTL = "90"
```

실제 JWT 비밀 키는 README나 GitHub에 기록하지 않습니다.

이어서 Spring Boot 서버를 실행합니다.

```powershell
.\mvnw.cmd spring-boot:run
```

정상 실행 시 백엔드는 다음 주소에서 동작합니다.

- 백엔드 주소: http://127.0.0.1:8000
- API 기본 경로: `/api`
- JWT 기본 유효 시간: 90초

백엔드 실행 터미널은 프론트엔드를 사용하는 동안 종료하지 않습니다.
```

### 2. 프론트엔드 빌드 및 실행

Spring Boot 백엔드가 실행 중인 상태에서 새로운 PowerShell 터미널을 엽니다.

프로젝트의 `frontend-vue` 폴더로 이동합니다.

```powershell
cd frontend-vue
```

필요한 패키지를 설치합니다.

```powershell
npm ci
```

Vue 프로젝트를 빌드합니다.

```powershell
npm run build
```

빌드된 프론트엔드를 실행합니다.

```powershell
npm run preview
```

브라우저에서 다음 주소로 접속합니다.

- 최종 실행 주소: http://127.0.0.1:4173/
- 개발 서버 주소: http://127.0.0.1:5173/

개발 중에는 `npm run dev` 명령어를 사용합니다.

프론트엔드의 `/api` 요청은 Vite 프록시를 통해
`http://127.0.0.1:8000`에서 실행 중인 Spring Boot 백엔드로 전달됩니다.

프록시 설정은 `frontend-vue/vite.config.js`에서 확인할 수 있습니다.

프론트엔드 코드를 변경한 경우 `npm run build`를 다시 실행한 뒤
`npm run preview`로 변경된 빌드 결과를 확인합니다.

서버를 종료하려면 프론트엔드와 백엔드의 각 터미널에서 `Ctrl + C`를 누릅니다.
```

### 3. 테스트 계정과 데이터베이스

#### 테스트 계정

| 사용자 | 이메일 | 비밀번호 |
|---|---|---|
| Alice | alice@class.test | Classroom!2026 |
| Bob | bob@class.test | Classroom!2026 |

위 계정은 과제 검증을 위해 제공된 테스트 계정입니다.

#### SQLite 데이터베이스

- 데이터베이스 종류: SQLite
- 데이터베이스 파일: `backend-spring/data/board.sqlite`
- 서버 최초 실행 시 데이터베이스 파일이 자동 생성됩니다.
- 게시글과 사용자 정보는 SQLite에 저장됩니다.
- 서버를 재시작해도 기존 게시글 데이터가 유지됩니다.

데이터베이스를 초기화하려면 Spring Boot 서버를 종료한 뒤
`backend-spring/data/board.sqlite` 파일을 삭제하고 서버를 다시 실행합니다.

데이터베이스 파일은 GitHub에 업로드하지 않습니다.
```


## 구현 완료 범위

| 기능 | 완료 여부 | 주요 파일 또는 함수 |
|---|---|---|
| 공개 게시글 목록 및 상세 조회 | 완료 | `BoardController.java`, `App.vue` |
| 로그인 및 JWT 발급 | 완료 | `AuthService.java`, `BoardController.java` |
| JWT 검증 및 내 정보 조회 | 완료 | `AuthService.java`, `GET /api/me` |
| 게시글 작성 및 작성자 저장 | 완료 | `BoardController.java` |
| 본인 게시글 수정 및 삭제 | 완료 | `AuthService.java`, `BoardController.java` |
| 토큰 만료 및 로그아웃 | 완료 | `AuthService.java`, `api.js`, `App.vue` |
| 입력 오류 및 권한 오류 처리 | 완료 | `BoardController.java`, `api.js` |

### 주요 구현 내용

- 로그인 성공 시 서버에서 JWT 액세스 토큰을 발급합니다.
- 프론트엔드는 발급받은 토큰을 메모리에 보관하고 보호된 API 요청에 사용합니다.
- 서버는 JWT의 서명과 만료 시간 등을 검증하여 사용자를 인증합니다.
- 로그인한 사용자만 게시글을 작성할 수 있습니다.
- 게시글 수정과 삭제 시 로그인 사용자와 작성자가 일치하는지 검사합니다.
- 권한이 없는 사용자의 수정 및 삭제 요청은 HTTP 403으로 거부합니다.
- 토큰이 없거나 유효하지 않은 경우 HTTP 401 오류를 반환합니다.
- 토큰이 만료되면 프론트엔드에서 로그인 상태를 초기화합니다.

### 알려진 문제 및 미완성 기능

현재까지 수행한 필수 기능 테스트에서 확인된 미완성 기능은 없습니다.
```

## 테스트 방법과 결과 요약

### 1. API 자동 테스트

Spring Boot 서버가 실행 중인 상태에서 프로젝트 최상위 폴더(`JWT-Assignment`)의 PowerShell 터미널에서 다음 명령어를 실행합니다.

```powershell
python tools\check_api.py --base http://127.0.0.1:8000 --mode final
```

테스트 결과: 모든 검사 통과

JWT 만료 테스트는 백엔드 서버를 종료한 뒤 `TOKEN_TTL`을 3초로 설정하여 재실행합니다.

```powershell
$env:TOKEN_TTL = "3"
```

이후 프로젝트 최상위 폴더에서 다음 명령어를 실행합니다.

```powershell
python tools\check_api.py --base http://127.0.0.1:8000 --mode final --expiry
```

테스트 결과: 모든 검사 통과

만료 테스트 후에는 `TOKEN_TTL`을 90초로 복구하여 백엔드를 재시작합니다.

### 2. JWT 인증 및 권한 테스트

| 테스트 항목 | 결과 |
|---|---|
| Alice 계정 로그인 | 통과 |
| Bob 계정 로그인 | 통과 |
| JWT를 이용한 내 정보 조회 | 통과 |
| 토큰이 없는 요청 거부 | 통과 |
| 유효하지 않은 토큰 거부 | 통과 |
| 만료된 토큰 거부 | 통과 |
| Alice의 본인 게시글 수정 및 삭제 | 통과 |
| Bob의 Alice 게시글 수정 및 삭제 차단 | 통과 |

인증이 필요한 API에서 토큰이 없거나 유효하지 않으면 HTTP 401을 반환합니다.

다른 사용자의 게시글을 수정하거나 삭제하려는 요청은 HTTP 403을 반환합니다.

### 3. 프론트엔드 빌드 및 기능 테스트

- Spring Boot 백엔드 빌드: 성공
- Vue 프론트엔드 빌드: 성공
- 빌드 결과 실행 주소: http://127.0.0.1:4173/
- Alice 로그인: 성공
- 게시글 작성: 성공
- 게시글 수정: 성공
- 게시글 삭제: 성공

상세 테스트 과정과 증거는 구현 보고서에 정리합니다.
```

## 핵심 구현 설명

### 1. 로그인 및 JWT 발급

사용자가 이메일과 비밀번호를 입력하면 Vue 프론트엔드는
`POST /api/auth/login` API로 로그인 요청을 전송합니다.

Spring Boot 백엔드는 사용자 정보를 확인하고,
로그인에 성공하면 JWT 액세스 토큰을 발급합니다.

JWT 발급 및 검증 기능은 `AuthService.java`에서 처리합니다.

### 2. JWT 보관 및 API 요청

프론트엔드는 발급받은 JWT를 메모리에 보관합니다.

인증이 필요한 API를 호출할 때 다음과 같이
Authorization 요청 헤더에 토큰을 포함합니다.

```text id="3i77eu"
Authorization: Bearer <JWT 액세스 토큰>
```

토큰 관리와 API 요청 처리는 `frontend-vue/src/api.js`에서 담당합니다.

### 3. 서버의 JWT 검증

Spring Boot 백엔드는 전달받은 JWT의 서명, 만료 시간,
발급자 및 대상 정보 등을 검증합니다.

토큰이 없거나 유효하지 않으면 HTTP 401을 반환하고,
정상적인 토큰이면 해당 사용자를 식별합니다.

### 4. 게시글 작성자 권한 검사

게시글을 작성하면 로그인한 사용자가 작성자로 저장됩니다.

수정 또는 삭제 요청이 발생하면 서버에서
로그인한 사용자와 게시글 작성자를 비교합니다.

본인의 게시글이면 작업을 허용하고,
다른 사용자의 게시글이면 HTTP 403을 반환합니다.

권한 검사는 프론트엔드의 버튼 표시 여부와 관계없이
백엔드에서 반드시 수행합니다.

### 5. 토큰 만료 및 로그아웃

JWT에는 만료 시간이 설정되어 있습니다.

만료된 토큰으로 보호된 API를 요청하면 서버에서 HTTP 401을 반환합니다.

프론트엔드는 인증 오류 발생 시 저장된 로그인 정보를 초기화합니다.

사용자가 로그아웃 버튼을 누르는 경우에도
메모리에 저장된 토큰과 사용자 정보를 삭제합니다.
```

## AI 활용 요약과 참고 자료

- AI 사용 여부: 사용
- 사용 도구: ChatGPT (OpenAI)
- 사용 목적: JWT 인증 및 권한 검사 구현, 코드 오류 해결, 테스트 방법 확인, 문서 작성 지원
- 활용 위치: `AuthService.java`, `BoardController.java`, `api.js`, `App.vue`, README 및 보고서
- 직접 검증한 내용: 로그인, JWT 검증 및 만료, 게시글 CRUD, 작성자 권한 검사, API 자동 테스트, 프로젝트 빌드 및 실행
- 참고 자료: 웹서비스설계 수업 자료, 과제 API 명세, 제공된 Vue 및 Spring Boot 템플릿

AI의 도움을 받아 구현한 내용을 직접 실행하고 테스트하여 정상 동작 여부를 확인했습니다.
```

## 보고서와 테스트 증거

- [구현 보고서 PDF](docs/report.pdf)

구현 과정과 테스트 결과, 화면 캡처 및 AI 활용 기록은 최종 보고서에 정리합니다.

※ 보고서 PDF는 작성 완료 후 `docs/report.pdf` 경로에 추가할 예정입니다.

## 제출 확인

- [x] JWT 로그인 및 인증 기능 구현
- [x] 게시글 작성·조회·수정·삭제 기능 구현
- [x] 작성자 권한 검사 구현
- [x] JWT 만료 및 오류 처리 확인
- [x] API 자동 테스트 통과
- [x] Vue 및 Spring Boot 빌드 성공
- [x] 4173번 빌드 화면에서 주요 기능 확인
- [ ] 최종 보고서 PDF 작성
- [ ] 보고서 및 테스트 증거 GitHub 업로드
- [ ] 새 폴더에서 GitHub 저장소 복제 후 실행 확인
- [ ] GitHub 저장소 URL 및 최종 커밋 SHA 제출
```
