package com.goiaba.shared.util

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

object TokenManager {
    private val settings = Settings()
    private const val JWT_TOKEN_KEY = "jwt_token"
    private const val USER_ID_KEY = "user_id"
    private const val USER_EMAIL_KEY = "user_email"
    private const val USER_USERNAME_KEY = "user_username"

    fun storeToken(
        jwt: String,
        userId: Int? = null,
        email: String? = null,
        username: String? = null
    ) {
        settings[JWT_TOKEN_KEY] = jwt
        userId?.let { settings[USER_ID_KEY] = it }
        email?.let { settings[USER_EMAIL_KEY] = it }
        username?.let { settings[USER_USERNAME_KEY] = it }
    }

    fun getToken(): String? {
        return settings.getStringOrNull(JWT_TOKEN_KEY)
    }

    fun getUserId(): Int? {
        return settings.getIntOrNull(USER_ID_KEY)
    }

    fun getUserEmail(): String? {
        return settings.getStringOrNull(USER_EMAIL_KEY)
    }

    fun getUserUsername(): String? {
        return settings.getStringOrNull(USER_USERNAME_KEY)
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun clearToken() {
        settings.remove(JWT_TOKEN_KEY)
        settings.remove(USER_ID_KEY)
        settings.remove(USER_EMAIL_KEY)
        settings.remove(USER_USERNAME_KEY)
    }

    fun clearAll() {
        settings.clear()
    }
}