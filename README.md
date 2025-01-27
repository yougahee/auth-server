# :closed_lock_with_key: Authorization Server

<br>

### :pushpin: 사용 프레임워크 및 기술
- Spring Boot
- JWT ( JSON Web Token)
- Google SMTP
- JPA
- DB : MySQL, Redis


</br>
</br>

# :key: 구현요소(FLOW)
<br>

### :pushpin: 회원가입
-------

1. 이메일을 입력하고 인증번호 받기 버튼을 누른다.
    1. 해당 이메일이 서비스에 존재한다면 이메일이 중복되었다는 응답메세지가 간다.
2. 인증번호 받기 버튼을 누르면 해당 이메일로 인증번호가 전송된다. (전송받은 인증코드를 10분안에 입력해야 한다.)
    1. 인증번호가 잘 입력되었다면, 다음단계로 넘어간다.
    2. 인증번호가 틀렸다면 넘어가지 않는다.
    3. 인증번호전송을 여러 번 했다면, 맨 마지막으로 온 메일(최신 메일)의 인증번호로 입력해야한다.
3. 닉네임과 패스워드를 입력하여 회원가입을 할 수 있다.
    1. 닉네임은 중복되지 않아야하며 중복확인 버튼으로 체크한다.
    2. 패스워드는 8~20자 사이의 문자로 영어, 숫자, 특수문자 조합으로 이루어져있다.
4. 정상적으로 회원가입이 된다.

</br>

### :pushpin: 로그인
-------

1. 이메일 아이디, 비밀번호 입력 후 로그인 버튼을 누른다.
    1. 회원이라면 로그인이 되고 메인화면으로 넘어간다.
    2. 회원이 아니라면 로그인이 되지 않는다.
    3. 로그인시 AccessToken과 RefreshToken을 응답으로 넘겨준다.

</br>

### :pushpin: 비밀번호 찾기
-------
1. 임시비밀번호 발급  

</br>

### :pushpin: 마이페이지
---------

1. 마이페이지에 들어오면 닉네임, 포인트, 내가 팔로우한 사람들의 목록이 뜬다.
2. 포인트를 충전할 수 있다.
    1. 스트리밍 중에 포인트를 사용할 수 있다. 

</br>

### :pushpin: 그외
----

1. Refresh Token 발급
2. 비밀번호 찾기, 변경
3. 닉네임 중복체크, 변경
4. 회원정보 수정, 탈퇴
5. 포인트 update
6. admin 서비스


</br>
</br>

# :key: 개발을 하면서 고민했던 것들

<br>

1. 서버는 클라이언트가 요청을 보내면 응답 또는 예외를 상황에 맞게 적절히 응답해주는 일종의 서비스직이라고 생각한다.    
그렇기 때문에, 요청이 들어오면 상황에 맞는 메세지와 적절한 응답값을 줘야한다고 생각했다.    
예를 들어, 회원가입을 하다가 에러가 난 경우라면 어떤 에러가 났는지 무슨 문제인지를 알려주는 메세지를 제공해야한다.      
이런 생각들로 초반에 어떻게 exception 처리를 할 수 있을지 고민을 많이 했다.    
	- ControllerAdvice라는 것이 있었다. 

2. 우리의 서비스에서 왜 로그인/회원가입이 필요한가?
    - 채팅을 치기 위해 
    - 추후 개발될 팔로우, 팔로잉을 통해 내가 좋아하는 스트리머의 방송을 손쉽게 찾을 수 있다. 
    - 방송을 보는 중에 포인트로 방송을 후원할 수 있다.
    - 어떤 방송을 주로 보는 지 등의 데이터를 쌓을 수 있고 이를 통해 서비스 방향을 설정할 수 있다. 

3. 왜 PW, salt의 길이를 VARCHAR(100)으로 잡았나?


4. id를 DB에 저장할 때, 대소문자를 구분할지 안할 것인지 정해야한다. 
	- 구분을 안하는 서비스도 있지만 테스트 결과(네이버, 카카오, 구글 등)은 이메일의 대소문자를 구분하지 않는다.    
	ID는 대/소문자를 구분하지 않고 DB에 저장할 때 항상 소문자로 변환하여 저장한다.

5. Spring Data Redis에서 Redis에 접근하는 2가지 방식이 있다. 
	- 첫째는 RedisTemplate방식, 두번째는 Redis Repository방식.

6. 만약, refresh token도 갈취를 당하면 어떻게 하나?
   	- refresh token도 새로 발급은 할 수 있지만, 로컬에 담겨있다는 점, header 또는 body에 담겨서 서버로 보내진다는 점을 두고 봤을 때, 갈취를 당했을 경우를 대비해야한다. 
   	- 요청하는 IP 주소나 다른 정보들을 함께 저장해두어 refresh token을 새로 발급해준다던가 하는 방법이 있을 것 같다.
   	- access token이 만료되서 refresh token을 보내는 과정이 상당히 복잡하여 DB에 저장해놓은 refresh token을 어떻게 잘 활용할 수 있을까? 
   		- 내가 구현한 대로 문제를 해결하면 속도와 성능이 느려질 가능성이 꽤 커보인다. 

7. 유저 정보를 안전하게 보호하기 위해 어떤 노력을 했나?
    - 실제 구현
        - 비밀번호 입력을 할 때, 문자, 숫자, 특수문자 조합으로 입력
        - salt 사용 ( 고유한 salt값을 가져야하고 32바이트 이상이어야 한다.)
        - SHA256 단방향 암호화 사용
    - 추후 발전 방안
        - 한 장비에서 1초에 5번 정도만 비교할 수 있도록 한다. (원래는 약 50억번정도 가능하다고 한다. 무분별한 해킹의 위험을 막기 위해서)
   

8. token은 Header? Body? Cookie? 어느 곳으로 보내주는 것이 맞을까?
	- Header나 Cookie에 포함시켜서 보내는 것이 일반적이라고 한다. 
	- 처음엔 Response Body에 보내줘도 괜찮지 않을까? 생각했다.
	Header 또는 Body에 넣던 틀린 것은 아니지만 "일반적"으로는 Header에 사용

9. token을 response header에 담아서 보내는 이유  
	- jwt라는 데이터의 특성상 body에 담기는 data랑 구분하는 것이 좋을 것이라고 판단
		- 해당 페이지도 header에 보내는 것을 추천 하고 있다.  
			- [링크](https://stackoverflow.com/questions/47709451/pass-jwt-refresh-token-on-header-or-body )  
		- CSRF, XSS 해킹 위험 감소
		- CSRF 

10. Log 관리 
	- 요청에 대한 Request, Response 정보들은 Fileter를 통해 로그를 통해 200 또는 400 에러 등 어느 곳에서 에러가 나고 성공이 되었는지 확인하고 있다.   
	  좀 더 고민해 볼만한 상황

</br>
</br>

# :key: API 문서
- swagger 사용해보기
- 사내 위키에 UpLoad

</br>
</br>


# :key: 인증 

### :pushpin: JWT(JSON Web Token)
- header, payload, signature로 나누어진다.
- token, refresh token

</br>
</br>

### :pushpin: 사용자 패스워드 암호화방법
1. PBKDF2
2. bcrypt
    - 패스워드 저장을 목적으로 설계
    - OpenBSD에서 기본 암호 인증 메커니즘으로 사용
    - 입력 값으로 72byte character 제약
    
</br>
</br>


### :pushpin: Token이 필요한 이유?
- Access Token을 사용하는 이유는 사용자를 인증하는 방식이다. 예를 들어 마이페이지에 접근한다고 생각해보면, '나'라는 것이 증명되어야 한다. 
- '나'라는 것을 어떻게 증명할 수 있을까?
  - 증명이 필요한 요청마다 로그인을 할 수도 있겠다.
  - 하지만, 매번 로그인을 해서 증명하는 것은 너무나 번거로운 일이다. 그렇기 때문에, 로그인을 했을 때, token을 발급받아서 클라이언트에서 token('나'를 증명해줄 증명서)을 저장하여 인증이 필요한 요청 header에 함께 보낸다. 

</br>
</br>


### :pushpin: 그렇다면 RefreshToken은 왜 사용하는가?
- 클라이언트와 서버사이에서 정보를 주고받을 때, Token을 활용하는데, HTTP로 보내지는 과정에서 갈취를 당할 수도 있다. 
- 그렇기 때문에 Access Token의 유지시간을 짧게 주고 Refresh Token을 사용한다. 

</br>
</br>


### :pushpin: 구현한 방법
1. 사용자가 로그인을 했을 때, access token과 refresh token을 발급해준다.  
	- access token의 만료시간은 1시간, refresh token의 만료시간은 1주일로 설정했다.
2. 로그인을 한 유저는 header에 token을 담아서 본인임을 인증한다.
3. 만약, token이 만료가 된다면 서버에서는 만료된 토큰이라는 응답을 보낸다.
4. 클라이언트에서 refresh token을 보내고 자신임을 증명하고 서버는 다시 access token과 refresh token을 발급해준다.

</br>
</br>
</br>


:key: 비밀번호 암호화
=============

- SHA-256
- salt
- BCryptPasswordEncoder

</br>

### :pushpin: BCryptPasswordEncoder?
----------------------
- 비밀번호 단방향 해시 알고리즘 중 하나
- 암호화시킬 text + salt를 더하여 digest를 만드는 것이다. 

### :pushpin: 그외
1. PBKDF2(Password-Based Key Derivation Function)
2. scrypt

### :pushpin: SHA-256이란?

- Secure Hash Algorithm의 약자로 해시함수를 사용하는 알고리즘이다. 

### :pushpin: MD5란?
- MD5(Message-Digest algorithm 5)는 128비트 암호화 해시 함수이다.
- 심한 암호화 결함이 있기 때문에 보안관련 용도로는 권장하지 않고 있으며 보통 프로그램이나 파일이 원본 그대로인지 확인하는 무결성 검사등에 활용된다. 

### :pushpin: 대부분 SHA-256을 더 사용하고 MD5는 쓰는 것을 잘 보지 못했다. 그 이유는?
-----------------------------------------------------------
- 현재 MD5 알고리즘을 보안 관련 용도로 쓰는 것을 권장하지 않고 있으며, 심각한 보안 문제를 야기할 수 도 있다고 한다. ( SSL 인증서를 변조하는 것이 가능하다는 것이 발표되었다. )

</br>


# :key: Error Log
1. BCrypto를 사용하기 위해 dependecies에 implementation 'org.springframework.boot:spring-boot-starter-security' 을 추가했다.
  	- 이것을 추가하고 POSTMAN으로 테스트를 해보니 spring security가 생겨서 401 에러가 발생했고, web으로 url을 입력해보니, 로그인을 하라는 창이 나왔다.
  	- 원인 : 추가한 dependency는 spring security를 활성화시켜주었다.
  	- 해결방법 : implementation 'org.springframework.security:spring-security-crypto:5.1.5.RELEASE' 대체

</br>

2. E-mail 전송하기 위해 사용한 JavaMailSender가 계속해서 NULL값이 들어왔다.
  	- 기본 생성자 주입을 해주면 되는데, @AutoWired를 썼더니 순환호출로 인해 메모리가 터져서 컴퓨터가 다운됐다.
	- @RequiredArgsConstructor 을 사용하자!
		- 이것을 사용할 때는 생성자 주입이 될 대상을 private final로 적어주어야 한다.

</br>

3. properties에서 gmail로 메일을 보내기 위한 설정도 다 해놓았고, JavaMailSender에도 NULL값이 들어가지 않는것도 확인했는데, MailSendException이 발생하였다.
	- 에러메시지를 보니 SSL, SSLHandShakeException, certification등 보안관련한 용어들이 나왔다.
	- 구글링 끝에 보안 프로그램을 끄고 돌려보니 메일이 잘 온 것을 확인할 수 있었다. 

</br>

# 📂 ERD

🏃‍♂️ user
----------

| Column | type |  설명 | PK/FK |
| :----: | :------: | :----------------------: | :----: |
| user_idx | BIGINT | 유저 고유값 ( 자동 생성 ) | PK |
| email | VARCHAR(255) | user ID(이메일형식) |  |
| pwd | VARCHAR(255) | user 비밀번호 |  |
| salt | VARCHAR(255) | user 비밀번호 Salt |  |
| nickname | VARCHAR(255) | 닉네임 |  |
| grade | TINYINT | 유저상태 | Default 0  |
| point | BIGINT | 포인트 | Default 50000 |
| create_dt | DATETIME | 생성날짜 |  |
| login_dt | DATETIME | 로그인한 시간 |  |
| update_dt | DATETIME | update한 시간 |  |

</br>
</br>

:punch: follow
----------

| Column | type |  설명 | PK/FK |
| :----: | :------: | :----------------------: | :----: |
| follow_idx | BIGINT | | FK |
| user_follow_idx | BIGINT | | FK |
| user_streamer_idx | BIGINT | | FK |
| create_dt | DATETIME | 생성날짜 |  |

</br>
</br>

:door: streaming_room
----------

| Column | type |  설명 | PK/FK |
| :----: | :------: | :----------------------: | :----: |
| room_idx | BIGINT | 방 고유값 ( 자동 생성 ) | PK |
| user_idx | BIGINT | 스트리머고유값 |  |
| title | VARCHAR(255) | 방제목 |  |
| thumnail_location | VARCHAR(1000) | 썸네일저장경로 |  |
| video_location | VARCHAR(1000) | 영상저장경로 |  |
| created_dt | DATETIME | 방송시작날짜 | NOT NULL  |
| end_stream_dt | DATETIME | 방송종료날짜 |  |

</br>
</br>

user의 grade정보
----------------

| Number | 설명 |
| :----: | :----------------------: | 
| 0 | 이메일 인증하기 전 |
| 1 | 회원가입완료(일반회원) | 
| 3 | 이메일 인증을 한 회원 |  
| 5 | 관리자 |  
| 9 | 탈퇴한 회원 |  

</br>
</br>


# :key: dependencies
```
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.boot:spring-boot-devtools'

	//db
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'mysql:mysql-connector-java'

	//redis
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'

	//lombok
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	implementation 'org.springframework.boot:spring-boot-starter-test'

	//encryption
	implementation 'org.springframework.security:spring-security-crypto:5.1.5.RELEASE'

	//valid
	implementation 'org.springframework.boot:spring-boot-starter-validation:2.3.3.RELEASE'

	//jwt
	implementation group: 'io.jsonwebtoken', name: 'jjwt', version: '0.7.0'
	implementation 'com.auth0:java-jwt:3.4.0'

	//email
	implementation 'org.springframework.boot:spring-boot-starter-mail'
	//compile "com.sun.mail:javax.mail"
	//implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'

	// Swagger 2
	compile group: 'io.springfox', name: 'springfox-swagger2', version: '2.9.2'
	compile group: 'io.springfox', name: 'springfox-swagger-ui', version: '2.9.2'
}
```


</br>
</br>
