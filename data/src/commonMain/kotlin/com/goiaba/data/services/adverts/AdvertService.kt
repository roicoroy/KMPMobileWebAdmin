package com.goiaba.data.services.adverts

import com.goiaba.data.models.adverts.AdvertCreateRequest
import com.goiaba.data.models.adverts.AdvertCreateResponse
import com.goiaba.data.models.adverts.AdvertGetResponse
import com.goiaba.data.models.adverts.AdvertUpdateRequest
import com.goiaba.data.models.adverts.AdvertUpdateResponse
import com.goiaba.data.models.adverts.CategoryResponse
import com.goiaba.data.models.adverts.PutAdvertProfileRequest
import com.goiaba.data.models.profile.PutAddressToProfileResponse
import com.goiaba.data.models.profile.strapiUser.StrapiProfile
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

    suspend fun getCategories(): RequestState<CategoryResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.get("api/categories")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val categoriesResponse = response.body<CategoryResponse>()
                    RequestState.Success(categoriesResponse)
                }

                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Invalid API token")
                }

                HttpStatusCode.NotFound -> {
                    RequestState.Error("Categories endpoint not found")
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

    suspend fun createAdvert(request: AdvertCreateRequest): RequestState<AdvertCreateResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.post("api/adverts") {
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val advertResponse = response.body<AdvertCreateResponse>()
                    RequestState.Success(advertResponse)
                }
                HttpStatusCode.BadRequest -> RequestState.Error("Invalid user data. Please check your information.")
                HttpStatusCode.Unauthorized -> RequestState.Error("Unauthorized: Please login to update user.")
                HttpStatusCode.NotFound -> RequestState.Error("User not found.")
                HttpStatusCode.Forbidden -> RequestState.Error("You don't have permission to update this user.")
                HttpStatusCode.InternalServerError -> RequestState.Error("Server error: Please try again later.")
                else -> RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }

    suspend fun addAdvertToProfile(
        profile: StrapiProfile,
        advertId: Int
    ): RequestState<PutAddressToProfileResponse> {
        val profileDocumentId = profile.data.documentId
        val advertIdStr = advertId.toString()
        val advertsIdList: List<String> = profile.data.adverts.map { it.id.toString() } + advertIdStr

        return try {
            val response: HttpResponse = ApiClient.httpClient.put("api/profiles/$profileDocumentId") {
                setBody(
                    PutAdvertProfileRequest(
                        data = PutAdvertProfileRequest.Data(
                            adverts = advertsIdList
                        )
                    )
                )
            }
            when (response.status) {
                HttpStatusCode.OK -> {
                    val userResponse = response.body<PutAddressToProfileResponse>()
                    RequestState.Success(userResponse)
                }
                HttpStatusCode.BadRequest -> RequestState.Error("Invalid user data. Please check your information.")
                HttpStatusCode.Unauthorized -> RequestState.Error("Unauthorized: Please login to update user.")
                HttpStatusCode.NotFound -> RequestState.Error("User not found.")
                HttpStatusCode.Forbidden -> RequestState.Error("You don't have permission to update this user.")
                HttpStatusCode.InternalServerError -> RequestState.Error("Server error: Please try again later.")
                else -> RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
            }

        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }

    suspend fun updateAdvert(advertId: String, request: AdvertUpdateRequest): RequestState<AdvertUpdateResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.put("api/adverts/$advertId") {
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val advertResponse = response.body<AdvertUpdateResponse>()
                    RequestState.Success(advertResponse)
                }

                HttpStatusCode.BadRequest -> {
                    RequestState.Error("Invalid advert data. Please check your information.")
                }

                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Please login to update adverts")
                }

                HttpStatusCode.NotFound -> {
                    RequestState.Error("Advert not found")
                }

                HttpStatusCode.Forbidden -> {
                    RequestState.Error("You don't have permission to update this advert")
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

    suspend fun deleteAdvert(advertId: String): RequestState<Boolean> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.delete("api/adverts/$advertId")

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.NoContent -> {
                    RequestState.Success(true)
                }

                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Please login to delete adverts")
                }

                HttpStatusCode.NotFound -> {
                    RequestState.Error("Advert not found")
                }

                HttpStatusCode.Forbidden -> {
                    RequestState.Error("You don't have permission to delete this advert")
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