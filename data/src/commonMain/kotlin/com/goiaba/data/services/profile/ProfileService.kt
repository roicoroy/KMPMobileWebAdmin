package com.goiaba.data.services.profile

import com.goiaba.data.models.profile.PutAddressToProfileRequest
import com.goiaba.data.models.profile.PutAddressToProfileResponse
import com.goiaba.data.models.profile.adress.AddressCreateRequest
import com.goiaba.data.models.profile.adress.AddressCreateResponse
import com.goiaba.data.models.profile.adress.AddressUpdateRequest
import com.goiaba.data.models.profile.adress.AddressUpdateResponse
import com.goiaba.data.models.profile.strapiUser.PutProfileResponse
import com.goiaba.data.models.profile.strapiUser.StrapiProfile
import com.goiaba.data.models.profile.strapiUser.StrapiUser
import com.goiaba.data.models.profile.strapiUser.UserProfilePutResquest
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.networking.apiUsersMe
import com.goiaba.shared.util.RequestState
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * Service class responsible for managing user profiles and related operations.
 */
class ProfileService {
    suspend fun getUsersMe(): RequestState<StrapiUser> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.get(apiUsersMe)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val usersMeResponseHttp = response.body<StrapiUser>()
                    RequestState.Success(usersMeResponseHttp)
                }

                HttpStatusCode.NotFound -> {
                    RequestState.Error("User not found")
                }

                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Invalid API token")
                }

                else -> {
                    RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
                }
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }

    suspend fun getUserProfile(
        userDocumentId: String,
    ): RequestState<StrapiProfile> {
        return try {
//            http://localhost:1337/api/profiles/hx64nkwcpgta4lmtwk7p4lyv?populate=*
//            r21y5s6f16brcbn6a2zwex0f
            val response: HttpResponse = ApiClient.httpClient.get("api/profiles/$userDocumentId?populate=*")
            when (response.status) {
                HttpStatusCode.OK -> {
                    val usersMeResponseHttp = response.body<StrapiProfile>()
                    RequestState.Success(usersMeResponseHttp)
                }

                HttpStatusCode.NotFound -> {
                    RequestState.Error("User not found")
                }

                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Invalid API token")
                }

                else -> {
                    RequestState.Error("HTTP ${response.status.value}: ${response.status.description}")
                }
            }
        } catch (e: Exception) {
            RequestState.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }

    suspend fun createAddress(request: AddressCreateRequest): RequestState<AddressCreateResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.post("api/addresses") {
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> {
                    val addressResponse = response.body<AddressCreateResponse>()
                    RequestState.Success(addressResponse)
                }

                HttpStatusCode.BadRequest -> {
                    RequestState.Error("Invalid address data. Please check your information.")
                }

                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Please login to create addresses")
                }

                HttpStatusCode.Forbidden -> {
                    RequestState.Error("You don't have permission to create addresses")
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

    suspend fun updateAddress(
        addressId: String,
        request: AddressUpdateRequest
    ): RequestState<AddressUpdateResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.put("api/addresses/$addressId") {
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val addressResponse = response.body<AddressUpdateResponse>()
                    RequestState.Success(addressResponse)
                }

                HttpStatusCode.BadRequest -> {
                    RequestState.Error("Invalid address data. Please check your information.")
                }

                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Please login to update addresses")
                }

                HttpStatusCode.NotFound -> {
                    RequestState.Error("Address not found")
                }

                HttpStatusCode.Forbidden -> {
                    RequestState.Error("You don't have permission to update this address")
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

    suspend fun deleteAddress(addressId: String): RequestState<Boolean> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.delete("api/addresses/$addressId")

            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.NoContent -> {
                    RequestState.Success(true)
                }

                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Please login to delete addresses")
                }

                HttpStatusCode.NotFound -> {
                    RequestState.Error("Address not found")
                }

                HttpStatusCode.Forbidden -> {
                    RequestState.Error("You don't have permission to delete this address")
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

    suspend fun updateProfile(
        profileDocumentId: String,
        request: UserProfilePutResquest
    ): RequestState<PutProfileResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.put("api/profiles/$profileDocumentId") {
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val userResponse = response.body<PutProfileResponse>()
                    RequestState.Success(userResponse)
                }

                HttpStatusCode.BadRequest -> {
                    RequestState.Error("Invalid user data. Please check your information.")
                }

                HttpStatusCode.Unauthorized -> {
                    RequestState.Error("Unauthorized: Please login to update user")
                }

                HttpStatusCode.NotFound -> {
                    RequestState.Error("User not found")
                }

                HttpStatusCode.Forbidden -> {
                    RequestState.Error("You don't have permission to update this user")
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

    suspend fun addAddressToProfile(
        profile: StrapiProfile,
        addressId: Int
    ): RequestState<PutAddressToProfileResponse> {
        val profileDocumentId = profile.data.documentId

        val addressIdStr = addressId.toString()
        val addressIdList: List<String> = profile.data.addresses.map { it.id.toString() } + addressIdStr

        return try {
            val response: HttpResponse = ApiClient.httpClient.put("api/profiles/$profileDocumentId") {
                setBody(
                    PutAddressToProfileRequest(
                        data = PutAddressToProfileRequest.Data(
                            addresses = addressIdList
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
}