package com.goiaba.data.services.adverts

import com.goiaba.data.models.adverts.AdvertGetResponse
import com.goiaba.data.networking.ApiClient
import com.goiaba.shared.util.RequestState
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class AdvertService {
    
    suspend fun getAdverts(): RequestState<AdvertGetResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.get("api/adverts?populate=*")
            
            when (response.status) {
                HttpStatusCode.OK -> {
                    val advertsResponse = response.body<AdvertGetResponse>()
                    println("::::: $advertsResponse")
                    RequestState.Success(advertsResponse)
                }
                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Invalid API token")
                }
                HttpStatusCode.NotFound -> {
                    RequestState.Error("Adverts endpoint not found")
                }
                HttpStatusCode.InternalServerError -> {
                    RequestState.Error("Server error: Please try again later")
                }
                else -> {
                    RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
                }
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }
}