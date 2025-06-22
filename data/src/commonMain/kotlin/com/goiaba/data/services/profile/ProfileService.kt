package com.goiaba.data.services.profile

import com.goiaba.data.models.profile.AddUserToAddressRequest
import com.goiaba.data.models.profile.AddUserToAddressResponse
import com.goiaba.data.models.profile.AddressCreateRequest
import com.goiaba.data.models.profile.AddressCreateResponse
import com.goiaba.data.models.profile.AddressUpdateRequest
import com.goiaba.data.models.profile.AddressUpdateResponse
import com.goiaba.data.models.profile.UserUpdateRequest
import com.goiaba.data.models.profile.UserUpdateResponse
import com.goiaba.data.models.profile.strapiUser.StrapiUser
import com.goiaba.data.networking.ApiClient
import com.goiaba.data.networking.apiUsersMe
import com.goiaba.shared.util.RequestState
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

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

    suspend fun updateUser(
        userDocumentId: String,
        request: UserUpdateRequest
    ): RequestState<UserUpdateResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.put("api/users/$userDocumentId") {
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val userResponse = response.body<UserUpdateResponse>()
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

    suspend fun addUserToAddress(
        userId: Int,
        addressId: String
    ): RequestState<AddUserToAddressResponse> {
        return try {
            val response: HttpResponse = ApiClient.httpClient.put("api/addresses/$addressId") {
                setBody(
                    AddUserToAddressRequest(
                        data = AddUserToAddressRequest.Data(
                            user = userId.toInt()
                        )
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val userResponse = response.body<AddUserToAddressResponse>()
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
}