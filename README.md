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
#### 1. 로그인
<details>
  <summary>OAuth2 기반 간편 로그인</summary>
  <div markdown="1">
    
  ```java

oauth2Login()을 통해 OAuth2 소셜 로그인을 사용하도록 설정하고,
로그인 성공/실패 시 동작을 직접 구현한 핸들러(CustomOAuth2SuccessHandler, CustomOAuth2FailureHandler)로 연결합니다.

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

```java


- OAuth2 로그인 시 사용자 정보를 불러오고, DB에 저장하거나 기존 유저와 매칭하는 역할을 합니다.
- 구글, 네이버, 카카오 로그인 요청이 들어오면 각 Provider에 맞는 응답 객체(GoogleOAuth2Response, NaverOAuth2Response, KakaoOAuth2Response)로 변환합니다.
- DB에 동일 이메일을 가진 사용자가 있으면 검증 후 기존 계정으로 로그인합니다.
- 없는 경우라면 새로 소셜 계정을 생성 후 저장하고, 가입 이벤트(UserRegisteredEvent)를 발행합니다.

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String provider = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = getOAuth2Response(oAuth2User, provider);

		return userRepository.findByEmail(oAuth2Response.getEmail())
			.map(user -> {
				validateExistingUserForOAuth2Login(user, oAuth2Response);
				return new CustomOAuth2User(user);
			})
			.orElseGet(() -> {
				User user = User.createSocialUser(oAuth2Response, createRandomNickname());
				User savedUser = userRepository.save(user);

				eventPublisher.publishEvent(UserRegisteredEvent.createFrom(savedUser));
				return new CustomOAuth2User(user);
			});
	}

	private OAuth2Response getOAuth2Response(
		OAuth2User oAuth2User,
		String provider
	) {
		return switch (provider) {
			case "google" -> new GoogleOAuth2Response(oAuth2User.getAttributes());
			case "naver" -> new NaverOAuth2Response(oAuth2User.getAttributes());
			case "kakao" -> new KakaoOAuth2Response(oAuth2User.getAttributes());
			default -> throw new IllegalArgumentException("지원하지 않는 Provider 입니다.");
		};
	}

	private void validateExistingUserForOAuth2Login(
		User user,
		OAuth2Response oAuth2Response
	) {
		if (user.getUserType() == UserType.NORMAL) {
			throw new SocialEmailAlreadyRegisteredException();
		}

		if (user.getOAuth2Provider() != oAuth2Response.getProvider()) {
			throw new DuplicateEmailWithDifferentProviderException();
		}
	}

	private String createRandomNickname() {
		return "user" + UUID.randomUUID().toString().substring(0, 8);
	}
}
```
```java


- 인증이 성공하면 사용자 정보를 기반으로 JWT Access Token과 Refresh Token을 생성합니다.
- 로그인 페이지로 redirect 하면서 Access Token과 닉네임을 쿼리 파라미터로 전달합니다.

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtProvider jwtProvider;
	private final RedisCacheService redisCacheService;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException, ServletException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		Long userId = oAuth2User.getUserId();
		String nickname = oAuth2User.getName();

		String accessToken = jwtProvider.generateAccessToken(userId, oAuth2User.getAuthorities());
		String refreshToken = jwtProvider.generateRefreshToken(userId);
		redisCacheService.set(userId.toString(), refreshToken);

		String encodedNickname = URLEncoder.encode(nickname, Charset.defaultCharset());
		String redirectionUrl = "http://localhost:5173/login?token=" + accessToken + "&nickname=" + encodedNickname;

		response.sendRedirect(redirectionUrl);
	}
}
```
```java

- 발생한 예외 메시지를 인코딩해서 실패 페이지(/login/error)로 전달합니다.

@Component
@RequiredArgsConstructor
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException exception
	) throws IOException, ServletException {
		String errorMessage = URLEncoder.encode(exception.getMessage(), Charset.defaultCharset());
		String redirectUrl = "http://localhost:5173/login/error?message=" + errorMessage;

		response.sendRedirect(redirectUrl);
	}
}
```

</div>
</details>

#### 2. 실시간 시세 및 차트 정보
<details>
<summary>일일 시가 데이터 캐시 스케줄러</summary>
	
```java

- 당일 등락률, 등락가격 계산을 위한 일일 시가 캐시 스케줄러
- 매일 UTC 00:00:03(09:00:03 KST)에 실행 (@Scheduled(cron = "3 0 0 * * *", zone = "UTC"))
- 분산 환경에서도 중복 실행 방지를 위해 ShedLock 사용
- 빠른 처리를 위한 Reactor 기반 비동기 처리 (Flux/Mono 활용)

@Component
@RequiredArgsConstructor
public class CryptoScheduler {
	private final BinanceClient binanceClient;
	private final CryptoRepository cryptoRepository;
	private final RedisReactiveCacheService reactiveCacheService;

	private static final String SYMBOL_SUFFIX = "USDT";

	@Scheduled(cron = "3 0 0 * * *", zone = "UTC")
	@SchedulerLock(name = "cacheDailyOpenPrice_lock")
	public void cacheDailyOpenPrice() {
		long startTime = System.currentTimeMillis();
		log.info("open price 캐싱 작업을 시작합니다.");

		Mono<List<String>> symbolsMono = Mono.fromCallable(() ->
			cryptoRepository.findAll().stream()
				.map(crypto -> crypto.getSymbol() + SYMBOL_SUFFIX)
				.toList()
		);

		symbolsMono
			.subscribeOn(Schedulers.boundedElastic())
			.flatMapMany(Flux::fromIterable)
			.flatMap(this::cacheSymbolOpenPrice)
			.doFinally(signalType -> {
				long endTime = System.currentTimeMillis();
				log.info("open price 캐싱 작업이 최종적으로 완료되었습니다. (총 소요 시간: {}ms) [종료 타입: {}]",
					endTime - startTime, signalType);
			})
			.subscribe();
	}

	private Mono<Void> cacheSymbolOpenPrice(String symbol) {
		return binanceClient.getKline(symbol, "1d", 1)
			.flatMap(klines -> {
				BigDecimal openPrice = klines.get(0).getOpen();
				String key = RedisKey.OPEN_PRICE_KEY_PREFIX.getValue() + symbol;

				return reactiveCacheService.set(key, openPrice.toPlainString())
					.doOnSuccess(result -> {
							if (result) log.debug("[캐싱 성공] [symbol]: {} | [open price]: {}", symbol, openPrice);
							else log.warn("[캐싱 실패] [symbol]: {}", symbol);
						});
			})
			.onErrorResume(error -> {
					log.warn("[API 요청 실패] [symbol]: {} | [에러]: {}", symbol, error.getMessage());
					return Mono.empty();
				}
			)
			.then();
	}
}
```
</details>


<details>
<summary>바이낸스 웹소켓 스트림 매니저 선출</summary>

```java

- 분산 락(Redisson)을 사용해 한 대의 서버만 스트림 매니저 역할을 맡도록 했습니다.
- lock.lock()을 호출하면 Redisson이 내부적으로 watchdog 스레드를 실행합니다.
- 이 스레드는 락을 점유 중인 인스턴스가 살아있는지 주기적으로 확인하고, 정상 동작 중이면 자동으로 락 시간을 연장 해줍니다.
- 스트림 매니저 애플리케이션이 죽거나 네트워크가 문제가 발생하면 watchdog이 갱신을 멈추고 락은 해제됩니다.
- 락이 해제되면 다른 인스턴스가 락을 획득해 새 스트림 매니저가 되어 다시 스트림을 연결합니다.

@EventListener(ApplicationReadyEvent.class)
public void onApplicationReadyEvent(ApplicationReadyEvent event) {
	Thread thread = new Thread(this::tryToBecomeLeader);
	thread.setDaemon(true);
	thread.start();
}

public void tryToBecomeLeader() {
	lock = redissonClient.getLock(LEADER_LOCK_KEY);

	try {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				lock.lock();
				isLeader = true;
				log.info("[{}] 리더로 지정... 스트림 연결을 시작합니다.", hostname);
				stopStreams();
				startStreams();

				while (lock.isLocked() && lock.isHeldByCurrentThread()) {
					Thread.sleep(5000);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			} finally {
				if (lock.isHeldByCurrentThread()) {
					log.warn("락을 해제합니다.");
					lock.unlock();
				}
				isLeader = false;
				log.warn("[{}] 리더 해제... 스트림 연결을 종료합니다.", hostname);
				stopStreams();
			}
		}
	} catch (Exception e) {
		log.error("예기치 않은 오류 발생", e);
	}
}
```
```java

- 바이낸스 웹소켓은 연결 후 11시간이 지나면 자동으로 연결이 끊깁니다.
- 위와 같은 이유로 연결 후 10시간이 지나면 선제적으로 재연결을 시도합니다.

@Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
public void scheduledReconnect() {
	if (!isLeader) {
		return;
	}

	if (lastConnectionTime == 0L) {
		return;
	}

	long uptime = System.currentTimeMillis() - lastConnectionTime;
	long tenHoursInMillis = 10 * 60 * 60 * 1000L;

	if (uptime > tenHoursInMillis) {
		log.info("연결 유지 시간이 10시간을 초과했습니다 ({}분 경과). 재연결을 시작합니다.", TimeUnit.MILLISECONDS.toMinutes(uptime));

		stopStreams();
		startStreams();
	}
}
```
</details>

<details>
<summary>실시간 데이터 발행</summary>
	
```java
@Service
@RequiredArgsConstructor
public class CryptoService {
	
...

public void publishTicker(TickerData data) {
	BigDecimal openPrice = getOpenPrice(data.getSymbol());
	BigDecimal currentPrice = data.getClosePrice();
	BigDecimal changePrice = currentPrice.subtract(openPrice);
	BigDecimal changeRate = getChangeRate(openPrice, currentPrice);

	String tickSize = redisCacheService.get(RedisKey.TICK_SIZE_KEY_PREFIX.getValue() + data.getSymbol());
	int scale = getScale(tickSize);

	TickerResponse tickerResponse = TickerResponse.createOf(
		data.getSymbol().replace(SYMBOL_SUFFIX, ""),
		currentPrice.setScale(scale, RoundingMode.HALF_UP),
		changePrice.setScale(scale, RoundingMode.HALF_UP),
		changeRate.setScale(2, RoundingMode.HALF_UP)
	);

	redisMessagePublisher.publishMessage(
		Channels.TICKER_CHANNEL.getTopic(),
		tickerResponse
		);
	}

...
}

```
```java

- 브로커로 Redis Pub/Sub을 사용했습니다.
- 서비스 내부에서 발생한 이벤트를 Redis 채널로 발행합니다.
- Ticker 데이터가 들어오면 ticker 채널에 JSON 메시지를 발행합니다.
- Chart 데이터가 들어오면 chart 채널에 JSON 메시지를 발행합니다.

@Component
@RequiredArgsConstructor
public class RedisMessagePublisher {
	private final ObjectMapper objectMapper;
	private final StringRedisTemplate redisTemplate;

	public void publishMessage(String topic, Object message) {
		try {
			redisTemplate.convertAndSend(topic, objectMapper.writeValueAsString(message));
		} catch (JsonProcessingException e) {
			log.warn(e.getMessage());
		}
	}
}
```
```java

- 수신한 JSON 메시지를 각 타입(TickerResponse, ChartResponse)으로 역직렬화하고,
destination prefix와 심볼/인터벌을 조합해 STOMP destination을 완성합니다.
- 실시간 시세와 차트 데이터를 WebSocket(STOMP) 으로 클라이언트에게 전달합니다.
- ticker → /topic/ticker/{symbol}
- chart → /topic/chart/{symbol}/{interval}

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber {
	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;

	public void sendMessage(String message, String channel) {
		Optional<Channels> channelOptional = getChannel(channel);

		if (channelOptional.isEmpty()) {
			log.warn("지원하지 않는 채널입니다: {}", channel);
			return;
		}

		Channels channelEnum = channelOptional.get();

		try {
			Object payload = objectMapper.readValue(message, channelEnum.getType());
			String destination = getDestination(channelEnum, payload);

			if (destination.isEmpty()) {
				log.warn("목적지를 찾을 수 없습니다. Channel: {}, Payload: {}", channel, payload);
				return;
			}

			messagingTemplate.convertAndSend(destination, payload);

		} catch (JsonProcessingException e) {
			log.error("역직렬화 실패 Channel: {}, Message: {}", channel, message, e);
		}
	}

	private String getDestination(Channels channel, Object payload) {
		return switch (payload) {
			case TickerResponse tickerResponse ->
				channel.getDestinationPrefix() + tickerResponse.getSymbol();
			case ChartResponse chartResponse ->
				channel.getDestinationPrefix() + chartResponse.getSymbol() + "/" + chartResponse.getInterval();
			default -> "";
		};
	}

...

```
```java

- Channels enum으로 채널(topic), destination prefix, payload 타입을 정의해 두었습니다.

@Getter
public enum Channels {
	TICKER_CHANNEL("ticker", "/topic/ticker/", TickerResponse.class),
	CHART_CHANNEL("chart", "/topic/chart/", ChartResponse.class);

	private String topic;
	private String destinationPrefix;
	private Class<?> type;

	Channels(String topic, String destinationPrefix, Class<?> type) {
		this.topic = topic;
		this.destinationPrefix = destinationPrefix;
		this.type = type;
	}
}
```
</details>

# 트러블 슈팅
### 1. @TransactionalEventListener
```
====문제 상황 분석====
OAuth2 로그인 시나리오 (loadUser 메소드)
기존에 loadUser() 메소드에 @transactional 어노테이션이 없었을 때, 트랜잭션 컨텍스트가 없는 상태였습니다.
이때 userRepository.save(user)가 실행되면 JPA는 내부적으로 자체 트랜잭션을 생성해서 사용자를 저장합니다. 그리고 저장이 완료되면 즉시 트랜잭션을 커밋합니다. 문제는 여기서 발생합니다. eventPublisher.publishEvent(UserRegisteredEvent.createFrom(savedUser))가 호출되어 이벤트가 발행되지만, 이때는 이미 사용자 저장 트랜잭션이 완료된 후입니다. @TransactionalEventListener는 기본 설정 시 현재 트랜잭션이 커밋된 후에 실행되는데, 트랜잭션 컨텍스트가 없는 상황에서 이벤트가 제대로 처리되지 않았습니다.

일반 회원가입 시나리오 (signup 메소드)
signup() 메소드는 @transactional 어노테이션이 있었기 때문에 트랜잭션 컨텍스트는 존재했습니다. 하지만 createDefaultWatchlist() 메소드에 기본 전파 옵션(REQUIRED)을 사용했을 때 문제가 발생했습니다. 기본 REQUIRED 전파 방식에서는 이미 존재하는 트랜잭션에 참여합니다. 즉, signup() 메소드의 트랜잭션과 createDefaultWatchlist() 메소드가 같은 트랜잭션을 공유하게 됩니다.

WARNING: if the TransactionPhase is set to AFTER_COMMIT (the default), AFTER_ROLLBACK,
or AFTER_COMPLETION, the transaction will have been committed or rolled back already,
but the transactional resources might still be active and accessible.

source: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/event/TransactionalEventListener.html

위 내용을 보면 @TransactionalEventListener 페이즈 옵션을 기본값으로 사용하게 되면 "트랜잭션은 이미 커밋되거나 롤백되어 있을 것. 하지만 트랜잭션 관련 리소스(예: 영속성 컨텍스트, 세션 등)는 여전히 활성 상태일 수 있음. 따라서 커밋될 새로운 변경은 주의" 라고 돼있습니다.

====해결 방법====
@transactional 추가 (OAuth2 기반 로그인)
loadUser() 메소드에 @transactional을 추가함으로써 전체 OAuth2 사용자 로드 과정이 하나의 트랜잭션으로 감싸집니다. 이제 userRepository.save(user)와 eventPublisher.publishEvent()가 동일한 트랜잭션 컨텍스트 내에서 실행됩니다.

REQUIRES_NEW 전파 옵션 (createDefaultWatchlist 메소드)
REQUIRES_NEW 전파 옵션은 새로운 독립적인 트랜잭션을 생성합니다. 이는 기존 트랜잭션을 일시 중단하고, 완전히 새로운 트랜잭션을 시작합니다.
```

### 2. Double.toString()으로 BigDecimal 생성 시 scale 오차 문제
```
바이낸스는 각 암호화폐별로 호가 단위(Tick Size) 정보를 제공합니다.
이를 적용하면, 사용자는 바이낸스와 동일한 형태의 가격 정보를 확인할 수 있습니다.
가격 데이터를 처리할 때, 호가 단위와 BigDecimal을 통해 처리하는 과정에서 표기 오류가 있었습니다.

====문제 상황 분석====
ex) Double num = 0.0001 / 기대하는 스케일 4

Java에서 Double.toString() 메서드는 매우 작거나 큰 수를 지수 표기법(Scientific Notation)으로 바꿔줍니다.

- 조건: 지수가 –3보다 작거나, 7 이상일 때
- 규칙
  - 유효숫자가 한 자리(n=1) 면 s₁.0E e 형태
  - 두 자리 이상(n>1)은 s₁.s₂...sₙE e 형태

0.0001는 1 × 10⁻⁴이므로 유효숫자는 “1” 하나.
따라서 Java 스펙에 따라 1.0E-4라는 문자열이 만들어집니다.
1.0E-4는 0.00010 이므로 1.0E-4 라는 문자열을 BigDecimal 생성자에 넘긴다면

BigDecimal decimal = new BigDecimal(d.toString());
System.out.println(decimal);        // 0.00010
System.out.println(decimal.scale()); // 5

스케일 4가 아닌 5를 반환 합니다.

====해결 방법====
Bigdecimal.stripTrailingZeros() 메서드를 이용해 불필요한 자리수 0을 제거한다
```

### 3. 분산환경에서 OAuth2 request session 오류
```java
====문제 상황 분석====
Spring Security OAuth2 로그인 과정에서 Authorization Request가 세션에 저장됩니다.
단일 서버 환경에서는 문제없이 동작하지만, ECS 등 분산 환경에서 세션이 공유되지 않으면 오류가 발생합니다.
분산환경에서 OAuth2 인증 과정에서 Redirect 시, 사용자의 요청이 다른 서버 인스턴스로 라우팅되면서
기존 세션에 저장된 Authorization Request를 찾지 못하는 상황이 발생합니다.

====해결 방법====
OAuth2 인증 과정의 상태 정보를 HttpSession에서 분리하여 Redis(중앙 집중식)에서 관리하도록 개선합니다.

@Component
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	private final static String AUTH_REQUEST_PREFIX = "OAUTH2_AUTH_REQUEST:";
	private final RedisCacheService cacheService;
	private final ObjectMapper objectMapper;

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		try {
			String key = getKey(request.getParameter("state"));
			String value = cacheService.get(key);
			return objectMapper.readValue(value, OAuth2AuthorizationRequest.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void saveAuthorizationRequest(
		OAuth2AuthorizationRequest authorizationRequest,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		try {
			String key = getKey(authorizationRequest.getState());
			String authRequest = objectMapper.writeValueAsString(authorizationRequest);
			cacheService.set(key, authRequest, 5L, TimeUnit.MINUTES);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(
		HttpServletRequest request,
		HttpServletResponse response
	) {
		try {
			String key = getKey(request.getParameter("state"));
			String value = cacheService.get(key);
			cacheService.delete(key);
			return objectMapper.readValue(value, OAuth2AuthorizationRequest.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private String getKey(String state) {
		return AUTH_REQUEST_PREFIX + state;
	}
}

- saveAuthorizationRequest: OAuth2 Provider로 리다이렉트하기 직전에 호출됩니다. OAuth2AuthorizationRequest 객체를 JSON으로 직렬화하여, state 값을 포함한 key로 Redis에 5분간 저장합니다.
- loadAuthorizationRequest: 인증 후 콜백으로 돌아왔을 때 호출됩니다. 요청 파라미터로 전달된 state 값을 사용하여 Redis에서 조회하고 역직렬화하여 반환합니다.
- removeAuthorizationRequest: 인증 흐름이 완료되면 데이터를 Redis에서 삭제합니다.

// SecurityConfig.class
.oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(auth -> auth
					.authorizationRequestRepository(redisOAuth2AuthorizationRequestRepository)
				)
			)
```
# ERD
<img width="1490" height="952" alt="image" src="https://github.com/user-attachments/assets/9e574d79-6bd6-4b3a-bc6d-d5edf7c12b92" />
