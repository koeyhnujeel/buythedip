package com.zunza.buythedip.user.entity

import com.zunza.buythedip.common.BaseEntity
import com.zunza.buythedip.user.constant.OAuthProvider
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

    @Column()
    val password: String? = null,

    @Column(nullable = false, unique = true)
    val nickname: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: UserType,

    @Column()
    @Enumerated(EnumType.STRING)
    val provider: OAuthProvider?
) : BaseEntity() {
    companion object {
        fun of(
            email: String,
            nickname: String,
            password: String,
            type: UserType = UserType.NORMAL,
            provider: OAuthProvider? = null
        ) = User(
            email = email,
            nickname = nickname,
            password = password,
            type = type,
            provider = provider
        )
    }
}
