package com.zunza.buythedip.chat.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.zunza.buythedip.chat.dto.ChatMessageDto;
import com.zunza.buythedip.security.JwtTokenProvider;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.repository.UserRepository;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerTest {

	private WebSocketStompClient stompClient;
	private StompSession stompSession;
	private User testUser;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@LocalServerPort
	private int port;

	@Container
	static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
		.withDatabaseName("testdb")
		.withUsername("testuser")
		.withPassword("testpass");

	@DynamicPropertySource
	static void overrideProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mysqlContainer::getUsername);
		registry.add("spring.datasource.password", mysqlContainer::getPassword);
		registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
	}

	@Container
	static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.0")
		.withExposedPorts(6379);

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", redisContainer::getHost);
		registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
	}

	@BeforeEach
	void setUp() throws Exception {
		List<Transport> transports = new ArrayList<>();
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		SockJsClient sockJsClient = new SockJsClient((transports));
		stompClient = new WebSocketStompClient(sockJsClient);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());


		String testToken = createTestToken();
		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.add("Authorization", "Bearer " + testToken);
		WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + testToken);


		stompSession = stompClient
			.connectAsync("ws://localhost:" + port + "/ws-chat", httpHeaders, stompHeaders, new StompSessionHandlerAdapter() {})
			.get(2, TimeUnit.SECONDS);
	}

	User createTestUser() {
		testUser = User.builder()
			.accountId("test321")
			.password(passwordEncoder.encode("@password1"))
			.nickname("user1")
			.build();
		return userRepository.save(testUser);
	}

	String createTestToken() {
		User user = createTestUser();

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
			user.getAccountId(), "@password1");
		Authentication authenticate = authenticationManager.authenticate(token);

		return jwtTokenProvider.generateAccessToken(authenticate);
	}

	@Test
	void 메세지_전송_성공() throws Exception {
		//given
		CompletableFuture<ChatMessageDto> future = new CompletableFuture<>();

		stompSession.subscribe("/topic/chat/room/public", new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return ChatMessageDto.class;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				future.complete((ChatMessageDto) payload);
			}
		});

		ChatMessageDto chatMessageDto = ChatMessageDto.of(testUser.getNickname(), "안녕하세요?", 3232323223L);

		//when
		stompSession.send("/app/chat/room/public", chatMessageDto);

		//then
		ChatMessageDto received = future.get(5, TimeUnit.SECONDS);
		assertEquals(chatMessageDto.getSender(), received.getSender());
		assertEquals(chatMessageDto.getContent(), received.getContent());
		assertEquals(chatMessageDto.getTimestamp(), received.getTimestamp());
	}
}
