package com.zunza.buythedip.auth.oauth2

import com.zunza.buythedip.auth.oauth2.dto.GoogleOAuth2Response
import com.zunza.buythedip.auth.oauth2.dto.KakaoOAuth2Response
import com.zunza.buythedip.auth.oauth2.dto.NaverOAuth2Response
import com.zunza.buythedip.auth.oauth2.dto.OAuth2Response
import com.zunza.buythedip.auth.oauth2.exception.DuplicateEmailWithDifferentProviderException
import com.zunza.buythedip.auth.oauth2.exception.SocialEmailAlreadyRegisteredException
import com.zunza.buythedip.user.constant.UserType
import com.zunza.buythedip.user.entity.User
import com.zunza.buythedip.user.repository.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User? {
        val oAuth2User = super.loadUser(userRequest)
        val provider = userRequest?.clientRegistration?.registrationId
        val oAuth2Response = getOAuth2Response(oAuth2User, provider)

        return userRepository.findByEmail(oAuth2Response.getEmail())
            ?.let { user ->
                validateExistingUserForOAuth2Login(user, oAuth2Response)
                CustomOAuth2User.createOf(user)
            }
            ?: run {
                val user = User.createSocialUser(oAuth2Response, createRandomNickname())
                val savedUser = userRepository.save(user)
                CustomOAuth2User.createOf(savedUser)
            }
    }

    private fun getOAuth2Response(
        oAuth2User: OAuth2User,
        provider: String?
    ): OAuth2Response {
        return when (provider) {
            "google" -> GoogleOAuth2Response(oAuth2User.attributes)
            "kakao" -> KakaoOAuth2Response(oAuth2User.attributes)
            "naver" -> NaverOAuth2Response(oAuth2User.attributes)
            else -> throw IllegalArgumentException("지원하지 않는 Provider 입니다.")
        }
    }

    private fun validateExistingUserForOAuth2Login(user: User, oAuth2Response: OAuth2Response) {
        when {
            user.type == UserType.NORMAL ->
                throw SocialEmailAlreadyRegisteredException()
            user.provider != oAuth2Response.getProvider() ->
                throw DuplicateEmailWithDifferentProviderException()
        }
    }

    private fun createRandomNickname(): String {
        return "user${UUID.randomUUID().toString().substring(0, 8)}"
    }
}
