package com.zunza.buythedip.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.user.dto.SignupRequestDto;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.repository.UserRepository;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

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

	@BeforeEach
	void setUp() throws SQLException {
		truncateTable();
	}

	private void truncateTable() throws SQLException {
		try (Connection connection = dataSource.getConnection();
			 Statement statement = connection.createStatement()) {
			statement.execute("TRUNCATE TABLE user");
		}
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DataSource dataSource;

	@Test
	void 회원가입_성공() throws Exception {
		// given
		SignupRequestDto signupRequestDto = new SignupRequestDto("test12", "password1!", "tester");
		String content = objectMapper.writeValueAsString(signupRequestDto);

		// when - then
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content))
			.andExpect(status().isCreated());

		assertTrue(userRepository.existsByAccountId("test12"));
	}

	@Test
	void 회원가입_실패_아이디_중복() throws Exception {
		// given
		userRepository.save(
			User.builder()
				.accountId("test123")
				.password("password1!")
				.nickname("tester1")
				.build()
		);

		SignupRequestDto signupRequestDto = new SignupRequestDto("test123", "password2!", "tester2");
		String content = objectMapper.writeValueAsString(signupRequestDto);

		// when - then
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.data.message").value("이미 사용 중인 아이디 입니다."));

		assertEquals(1, userRepository.findAll().size());
	}

	@Test
	void 회원가입_실패_닉네임_중복() throws Exception {
		// given
		userRepository.save(
			User.builder()
				.accountId("test123")
				.password("password1!")
				.nickname("tester1")
				.build()
		);

		SignupRequestDto signupRequestDto = new SignupRequestDto("test1234", "password2!", "tester1");
		String content = objectMapper.writeValueAsString(signupRequestDto);

		// when - then
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.data.message").value("이미 사용 중인 닉네임 입니다."));

		assertEquals(1, userRepository.findAll().size());
	}

	@Test
	void 회원가입_아이디_유효성_검사_실패() throws Exception {
		// given
		SignupRequestDto signupRequestDto = new SignupRequestDto("te@@@@", "password2!", "tester1");
		String content = objectMapper.writeValueAsString(signupRequestDto);

		// when - then
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data.message").value("아이디는 영문 소문자와 숫자만 사용 가능합니다."));

		assertEquals(0, userRepository.findAll().size());
	}

	@Test
	void 회원가입_비밀번호_유효성_검사_실패() throws Exception {
		// given
		SignupRequestDto signupRequestDto = new SignupRequestDto("test123", "password", "tester1");
		String content = objectMapper.writeValueAsString(signupRequestDto);

		// when - then
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data.message").value("비밀번호는 8자 이상이며, 영문자, 숫자, 특수문자를 최소 1개 이상 포함해야 합니다."));

		assertEquals(0, userRepository.findAll().size());
	}

	@Test
	void 회원가입_닉네임_유효성_검사_실패() throws Exception {
		// given
		SignupRequestDto signupRequestDto = new SignupRequestDto("test123", "password1!", "t@");
		String content = objectMapper.writeValueAsString(signupRequestDto);

		// when - then
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.data.message").value("닉네임은 한글, 영문자, 숫자만 사용 가능합니다."));

		assertEquals(0, userRepository.findAll().size());
	}

	@Test
	void 회원가입_시_아이디_중복_확인_성공() throws Exception {
		// given
		String accountId = "test123";

		// when - then
		mockMvc.perform(get("/api/auth/check/account-id?accountId=" + accountId))
			.andExpect(status().isOk());
	}

	@Test
	void 회원가입_시_아이디_중복_확인_실패() throws Exception {
		// given
		userRepository.save(
			User.builder()
				.accountId("test123")
				.password("password1!")
				.nickname("tester1")
				.build()
		);

		String accountId = "test123";

		// when - then
		mockMvc.perform(get("/api/auth/check/account-id?accountId=" + accountId))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.data.message").value("이미 사용 중인 아이디 입니다."));
	}

	@Test
	void 회원가입_시_닉네임_중복_확인_성공() throws Exception {
		// given
		String nickname = "tester";

		// when - then
		mockMvc.perform(get("/api/auth/check/nickname?nickname=" + nickname))
			.andExpect(status().isOk());
	}

	@Test
	void 회원가입_시_닉네임_중복_확인_실패() throws Exception {
		// given
		userRepository.save(
			User.builder()
				.accountId("test123")
				.password("password1!")
				.nickname("tester")
				.build()
		);

		String nickname = "tester";

		// when - then
		mockMvc.perform(get("/api/auth/check/nickname?nickname=" + nickname))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.data.message").value("이미 사용 중인 닉네임 입니다."));
	}
}
