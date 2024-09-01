# Server
Self Servicing Law

## 💡 Features
- 인증 서비스: Kakao, Google, Naver 소셜 로그인
- AI 상담 서비스: 법률 지식을 학습한 AI와 법률 상담
- 서류 서비스
  - 서류 첨삭: 기존에 작성한 법률 서류를 AI가 첨삭
  - 서류 생성: 작성된 양식에 따라 AI가 사용자가 팔요로 하는 서류를 생성
- 변호사 서비스: 분야별 변호사 소개 및 알선(추가 서비스 개발 예정)
- 문의/건의 서비스: 사용자의 문의/건의 사항을 받고 관리자가 답변

## ⚒️ Tech Stack

### Languages
- Java 17

### Frameworks
- Spring Boot 3.2.5
  - Spring Boot Web
  - Spring Boot WebSocket
  - Spring Boot Validation
  - Spring Boot OAuth2 Client
  - Spring Boot Security
  - Spring Boot Data JPA

### Security
- Spring Security
- JWT

### Database
- MySQL 8.0.32
- Redis

### Test
- Mockito
- JUnit(+ AssertJ)

### Deploy
- AWS
  - EC2
  - RDS
  - S3 (적용 예정)

### Build Tools
- Gradle

### Utilities
- Lombok
- Spring Boot DevTools
  
### API Test/Documentation 
- Postman

## 🔍 Architecture

### ERD (24.09.01)
![image](https://github.com/user-attachments/assets/9a8fcc63-1822-4a9f-9735-35e7f41e9eda)

### System Architecture
<img width="765" alt="스크린샷 2024-07-04 17 58 54" src="https://github.com/pp8817/SSL/assets/71458064/72bad016-0b8b-458e-b07a-10120294f751">

### Commit Message Convention
<img width="721" alt="commit Message" src="https://github.com/user-attachments/assets/f9b6d831-7976-4eeb-817c-4c448dcf3080">




