# buythedip - 실시간 암호화폐 시세 및 차트 서비스
<img width="1536" height="1024" alt="buythedip" src="https://github.com/user-attachments/assets/fa782821-3787-438b-b75b-bdcd6df58a94" />

# 아키텍처
<img width="1008" height="916" alt="image" src="https://github.com/user-attachments/assets/fc82194c-bc9e-4ae6-9614-ae7166471909" />

# Stacks
#### Backend
![JAVA](https://img.shields.io/badge/Java21-007396?style=for-the-badge&logo=data:image/svg%2bxml;base64,PCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KDTwhLS0gVXBsb2FkZWQgdG86IFNWRyBSZXBvLCB3d3cuc3ZncmVwby5jb20sIFRyYW5zZm9ybWVkIGJ5OiBTVkcgUmVwbyBNaXhlciBUb29scyAtLT4KPHN2ZyB3aWR0aD0iMTUwcHgiIGhlaWdodD0iMTUwcHgiIHZpZXdCb3g9IjAgMCAzMi4wMCAzMi4wMCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiBmaWxsPSIjZmZmZmZmIiBzdHJva2U9IiNmZmZmZmYiIHN0cm9rZS13aWR0aD0iMC4yNTYiPgoNPGcgaWQ9IlNWR1JlcG9fYmdDYXJyaWVyIiBzdHJva2Utd2lkdGg9IjAiLz4KDTxnIGlkPSJTVkdSZXBvX3RyYWNlckNhcnJpZXIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIvPgoNPGcgaWQ9IlNWR1JlcG9faWNvbkNhcnJpZXIiPiA8cGF0aCBmaWxsPSIjZmZmZmZmIiBkPSJNMTIuNTU3IDIzLjIyYzAgMC0wLjk4MiAwLjU3MSAwLjY5OSAwLjc2NSAyLjAzNyAwLjIzMiAzLjA3OSAwLjE5OSA1LjMyNC0wLjIyNiAwIDAgMC41OSAwLjM3IDEuNDE1IDAuNjkxLTUuMDMzIDIuMTU3LTExLjM5LTAuMTI1LTcuNDM3LTEuMjN6TTExLjk0MiAyMC40MDVjMCAwLTEuMTAyIDAuODE2IDAuNTgxIDAuOTkgMi4xNzYgMC4yMjQgMy44OTUgMC4yNDMgNi44NjktMC4zMyAwIDAgMC40MTEgMC40MTcgMS4wNTggMC42NDUtNi4wODUgMS43NzktMTIuODYzIDAuMTQtOC41MDgtMS4zMDV6TTE3LjEyNyAxNS42M2MxLjI0IDEuNDI4LTAuMzI2IDIuNzEzLTAuMzI2IDIuNzEzczMuMTQ5LTEuNjI1IDEuNzAzLTMuNjYxYy0xLjM1MS0xLjg5OC0yLjM4Ni0yLjg0MSAzLjIyMS02LjA5MyAwIDAtOC44MDEgMi4xOTgtNC41OTggNy4wNDJ6TTIzLjc4MyAyNS4zMDJjMCAwIDAuNzI3IDAuNTk5LTAuODAxIDEuMDYyLTIuOTA1IDAuODgtMTIuMDkxIDEuMTQ2LTE0LjY0MyAwLjAzNS0wLjkxNy0wLjM5OSAwLjgwMy0wLjk1MyAxLjM0NC0xLjA2OSAwLjU2NC0wLjEyMiAwLjg4Ny0wLjEgMC44ODctMC4xLTEuMDIwLTAuNzE5LTYuNTk0IDEuNDExLTIuODMxIDIuMDIxIDEwLjI2MiAxLjY2NCAxOC43MDYtMC43NDkgMTYuMDQ0LTEuOTV6TTEzLjAyOSAxNy40ODljMCAwLTQuNjczIDEuMTEtMS42NTUgMS41MTMgMS4yNzQgMC4xNzEgMy44MTQgMC4xMzIgNi4xODEtMC4wNjYgMS45MzQtMC4xNjMgMy44NzYtMC41MSAzLjg3Ni0wLjUxcy0wLjY4MiAwLjI5Mi0xLjE3NSAwLjYyOWMtNC43NDUgMS4yNDgtMTMuOTExIDAuNjY3LTExLjI3Mi0wLjYwOSAyLjIzMi0xLjA3OSA0LjA0Ni0wLjk1NiA0LjA0Ni0wLjk1NnpNMjEuNDEyIDIyLjE3NGM0LjgyNC0yLjUwNiAyLjU5My00LjkxNSAxLjAzNy00LjU5MS0wLjM4MiAwLjA3OS0wLjU1MiAwLjE0OC0wLjU1MiAwLjE0OHMwLjE0Mi0wLjIyMiAwLjQxMi0wLjMxOGMzLjA3OS0xLjA4MyA1LjQ0OCAzLjE5My0wLjk5NCA0Ljg4Ny0wIDAgMC4wNzUtMC4wNjcgMC4wOTctMC4xMjZ6TTE4LjUwMyAzLjMzN2MwIDAgMi42NzEgMi42NzItMi41MzQgNi43ODEtNC4xNzQgMy4yOTYtMC45NTIgNS4xNzYtMC4wMDIgNy4zMjMtMi40MzYtMi4xOTgtNC4yMjQtNC4xMzMtMy4wMjUtNS45MzQgMS43NjEtMi42NDQgNi42MzgtMy45MjUgNS41Ni04LjE3ek0xMy41MDMgMjguOTY2YzQuNjMgMC4yOTYgMTEuNzQtMC4xNjQgMTEuOTA4LTIuMzU1IDAgMC0wLjMyNCAwLjgzMS0zLjgyNiAxLjQ5LTMuOTUyIDAuNzQ0LTguODI2IDAuNjU3LTExLjcxNiAwLjE4IDAgMCAwLjU5MiAwLjQ5IDMuNjM1IDAuNjg1eiIvPiA8L2c+Cg08L3N2Zz4=) <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
#### Database 
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">
#### Development Tools & Environment
<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=github&logoColor=white"> 

# 화면 구성
| 회원가입|로그인|왓치리스트|왓치리스트 추가 |
| --- | --- | --- | --- |
| <img width="200" height="600" alt="스크린샷 2025-10-04 오후 7 34 31" src="https://github.com/user-attachments/assets/cb8b0c85-acfc-405e-b0d6-5993cc71af41" /> | <img width="200" height="600" alt="스크린샷 2025-10-04 오후 7 31 35" src="https://github.com/user-attachments/assets/2e512200-9529-44bd-a57a-db59065c59e1" /> | <img width="200" height="600" alt="스크린샷 2025-10-04 오후 7 35 00" src="https://github.com/user-attachments/assets/10e438ec-d309-4253-8c79-bd26b821572f" /> | <img width="200" height="600" alt="스크린샷 2025-10-04 오후 7 35 33" src="https://github.com/user-attachments/assets/68a95abc-12c7-4800-a906-067cd89795d0" /> |

| 종목 검색 및 왓치리스트에 추가 | 차트 | 정보 |
| --- | --- | --- |
| <img width="200" height="600" alt="스크린샷 2025-10-04 오후 7 35 44" src="https://github.com/user-attachments/assets/a56c1fb8-08d6-43dd-9ea0-7268078b2308" /> | <img width="200" height="600" alt="스크린샷 2025-10-04 오후 7 35 53" src="https://github.com/user-attachments/assets/bc15a7fc-daab-4c53-8244-d99fa4c9db6c" /> | <img width="200" height="600" alt="스크린샷 2025-10-04 오후 7 35 58" src="https://github.com/user-attachments/assets/885a63ac-0508-471f-a12c-e44d56e638f0" />

# 주요 기능
<details>
  <summary><b>1. OAuth2 기반 간편 로그인</b></summary>
  <div markdown="1">
    
  ```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo ->
					userInfo.userService(customOAuth2UserService)
				)
				.successHandler(customOAuth2SuccessHandler)
				.failureHandler(customOAuth2FailureHandler)
			)

      ...
}
```
</div>
</details>

# ERD
<img width="1490" height="952" alt="image" src="https://github.com/user-attachments/assets/9e574d79-6bd6-4b3a-bc6d-d5edf7c12b92" />
