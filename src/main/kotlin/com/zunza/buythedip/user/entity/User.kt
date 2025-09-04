package com.zunza.buythedip.user.entity

import com.zunza.buythedip.auth.oauth2.dto.OAuth2Response
import com.zunza.buythedip.common.BaseEntity
import com.zunza.buythedip.user.constant.OAuth2Provider
import com.zunza.buythedip.user.constant.UserRole
import com.zunza.buythedip.user.constant.UserType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column
    val password: String? = null,

    @Column(nullable = false, unique = true)
    val nickname: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: UserType,

    @Column
    @Enumerated(EnumType.STRING)
    val provider: OAuth2Provider? = null,

    @Column
    val providerId: String? = null
) : BaseEntity() {
    companion object {
        fun createNormalUser(
            email: String,
            password: String,
            nickname: String
        ): User {
            return User(
                email = email,
                password = password,
                nickname = nickname,
                type = UserType.NORMAL
            )
        }

        fun createSocialUser(
            oAuth2Response: OAuth2Response,
            randomNickname: String
        ) = User(
            email = oAuth2Response.getEmail(),
            nickname = randomNickname,
            type = UserType.SOCIAL,
            provider = oAuth2Response.getProvider(),
            providerId = oAuth2Response.getProviderId()
        )
    }
}
