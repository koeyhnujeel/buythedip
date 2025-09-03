package com.zunza.buythedip.user.service

import com.zunza.buythedip.auth.jwt.JwtTokenProvider
import com.zunza.buythedip.auth.user.CustomUserDetails
import com.zunza.buythedip.user.constant.UserRole
import com.zunza.buythedip.user.constant.UserType
import com.zunza.buythedip.user.dto.LoginRequest
import com.zunza.buythedip.user.entity.User
import com.zunza.buythedip.user.exception.LoginFailedException
import com.zunza.buythedip.user.repository.RefreshJwtRepository
import com.zunza.buythedip.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest : BehaviorSpec ({
    val userRepository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val jwtTokenProvider = mockk<JwtTokenProvider>()
    val authenticationManager = mockk<AuthenticationManager>()
    val refreshJwtRepository = mockk<RefreshJwtRepository>()

    val authService = AuthService(
        userRepository,
        passwordEncoder,
        jwtTokenProvider,
        authenticationManager,
        refreshJwtRepository
    )

    Given("회원가입 시 이메일, 닉네임 중복 체크할 때") {
        val email = "test@email.com"
        val nickname = "test"
        When("사용할 수 있는 이메일이라면") {
            every { userRepository.existsByEmail(any()) } returns false
            Then("사용 가능을 응답해야한다.") {
                val response = authService.isEmailAvailable(email)
                response.isAvailable shouldBe true
            }
        }

        When("중복된 이메일이라면") {
            every { userRepository.existsByEmail(any()) } returns true
            Then("사용 불가능을 응답해야한다.") {
                val response = authService.isEmailAvailable(email)
                response.isAvailable shouldBe false
            }
        }

        When("잘못된 이메일 형식이라면") {
            val invalidEmail = "testEmail.com"
            Then("예외를 발생시켜야 한다") {
                shouldThrow<IllegalArgumentException> {
                    authService.isEmailAvailable(invalidEmail)
                }
            }
        }

        When("사용할 수 있는 닉네임이라면") {
            every { userRepository.existsByNickname(any()) } returns false
            Then("사용 가능을 응답해야한다.") {
                val response = authService.isNicknameAvailable(nickname)
                response.isAvailable shouldBe true
            }
        }

        When("중복된 닉네임이라면") {
            every { userRepository.existsByNickname(any()) } returns true
            Then("사용 불가능을 응답해야한다.") {
                val response = authService.isNicknameAvailable(nickname)
                response.isAvailable shouldBe false
            }
        }

        When("잘못된 닉네임 형식이라면") {
            val invalidNickname = "12321"
            Then("예외를 발생시켜야 한다") {
                shouldThrow<IllegalArgumentException> {
                    authService.isNicknameAvailable(invalidNickname)
                }
            }
        }
    }

    Given("유저가 올바른 이메일과 패스워드로 로그인 시도할 때") {
        val loginRequest = LoginRequest("test@example.com", "password11!")
        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)

        val user = User(
            1L,
            "test@example.com",
            "encodedPassword",
            "tester",
            UserRole.USER,
            UserType.NORMAL,
            null
        )

        val customUserDetails = CustomUserDetails(user)
        val authentication = mockk<Authentication>()

        every { authentication.principal } returns customUserDetails
        every { authenticationManager.authenticate(authenticationToken) } returns authentication
        every { jwtTokenProvider.generateAccessJwt(1L, any()) } returns "access-jwt"
        every { jwtTokenProvider.generateRefreshJwt(1L) } returns "refresh-jwt"
        every { refreshJwtRepository.save(1L, "refresh-jwt") } just Runs

        When("로그인 메소드를 호출하면") {
            val result = authService.login(loginRequest)

            Then("닉네임과 accessJwt, refreshJwt가 반환된다") {
                result["nickname"] shouldBe "tester"
                result["accessJwt"] shouldBe "access-jwt"
                result["refreshJwt"] shouldBe "refresh-jwt"
                verify(exactly = 1) { refreshJwtRepository.save(any(), any()) }
            }
        }
    }

    Given("비밀번호가 틀려서 인증에 실패할 때") {
        val loginRequest = LoginRequest("wrong@example.com", "wrong")
        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)

        every { authenticationManager.authenticate(authenticationToken) } throws LoginFailedException()

        When("로그인 메소드를 호출하면") {
            Then("LoginFailedException이 발생한다") {
                val exception = shouldThrow<LoginFailedException> {
                    authService.login(loginRequest)
                }
                exception.message shouldBe "아이디 또는 비밀번호를 확인해 주세요."
            }
        }
    }
})
