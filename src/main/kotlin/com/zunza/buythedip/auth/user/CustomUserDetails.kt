package com.zunza.buythedip.auth.user

import com.zunza.buythedip.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    private val user: User
) : UserDetails {

    val userId: Long
        get() = user.id

    val nickname: String
        get() = user.nickname

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return mutableListOf(GrantedAuthority { user.role.value })
    }

    override fun getPassword(): String? {
        return user.password
    }

    override fun getUsername(): String? {
        return user.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
