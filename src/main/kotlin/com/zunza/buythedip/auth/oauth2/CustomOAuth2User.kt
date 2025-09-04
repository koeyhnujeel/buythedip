package com.zunza.buythedip.auth.oauth2

import com.zunza.buythedip.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    private val user: User
) : OAuth2User {

    companion object {
        fun createOf(user: User): CustomOAuth2User {
            return CustomOAuth2User(user)
        }
    }

    override fun getAttributes(): Map<String?, Any?>? {
        return mapOf()
    }

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return listOf(GrantedAuthority { user.role.value })
    }

    override fun getName(): String? {
        return user.nickname
    }

    val email: String
        get() = user.email

    val id: Long
        get() = user.id
}
