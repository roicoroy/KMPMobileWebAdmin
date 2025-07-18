package com.goiaba.data.networking

import com.goiaba.shared.util.TokenManager
import io.ktor.client.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = false
                prettyPrint = true
                coerceInputValues = true
            })
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        
        install(DefaultRequest) {
            url(strapiUrl)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            
            // Add Bearer token if available
            val token = TokenManager.getToken()
            if (token != null) {
                header(HttpHeaders.Authorization, "Bearer $token")

            }
        }
    }
    
    /**
     * Updates the HTTP client with a new JWT token
     * This is called after successful login to ensure all subsequent requests use the new token
     */
    fun updateAuthToken(token: String) {
        TokenManager.storeToken(token)
        // Note: The token will be automatically picked up by the DefaultRequest plugin
        // on the next request due to the TokenManager.getToken() call
    }
    
    /**
     * Removes authentication token
     * This is called during logout
     */
    fun clearAuthToken() {
        TokenManager.clearToken()
        // Subsequent requests will fall back to the static token
    }
}