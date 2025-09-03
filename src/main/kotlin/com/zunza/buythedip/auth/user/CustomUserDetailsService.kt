package com.zunza.buythedip.auth.user

import com.zunza.buythedip.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails? {
        if (username.isNullOrBlank()) {
            throw UsernameNotFoundException("사용자 이름은 null 또는 공백일 수 없습니다.")
        }

        val user = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("사용자를 찾을 수 없습니다: $username")

        return CustomUserDetails(user)
    }
}
