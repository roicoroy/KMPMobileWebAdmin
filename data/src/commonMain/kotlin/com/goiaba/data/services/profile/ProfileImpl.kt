package com.goiaba.data.services.profile

import com.goiaba.data.models.profile.AddUserToAddressResponse
import com.goiaba.data.models.profile.AddressCreateRequest
import com.goiaba.data.models.profile.AddressCreateResponse
import com.goiaba.data.models.profile.AddressUpdateRequest
import com.goiaba.data.models.profile.AddressUpdateResponse
import com.goiaba.data.models.profile.UserUpdateRequest
import com.goiaba.data.models.profile.UserUpdateResponse
import com.goiaba.data.models.profile.strapiUser.StrapiUser
import com.goiaba.data.services.profile.domain.ProfileRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileImpl : ProfileRepository {
    private val apiService = ProfileService()

    override fun getUsersMe(): Flow<RequestState<StrapiUser>> = flow {
        emit(RequestState.Loading)

        try {
            // Add a small delay to show loading state
            delay(500)

            val result = apiService.getUsersMe()

            if (result.isSuccess()) {
                emit(RequestState.Success(result.getSuccessData()))
            } else {
                emit(RequestState.Error("Unknown error occurred"))
            }
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to fetch posts: ${e.message}"))
        }
    }

    override suspend fun createAddress(request: AddressCreateRequest): Flow<RequestState<AddressCreateResponse>> =
        flow {
            emit(RequestState.Loading)

            try {
                delay(300)
                val result = apiService.createAddress(request)
                emit(result)
            } catch (e: Exception) {
                emit(RequestState.Error("Failed to create address: ${e.message}"))
            }
        }

    override suspend fun updateAddress(
        addressId: String,
        request: AddressUpdateRequest
    ): Flow<RequestState<AddressUpdateResponse>> = flow {
        emit(RequestState.Loading)

        try {
            delay(300)
            val result = apiService.updateAddress(addressId, request)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to update address: ${e.message}"))
        }
    }

    override suspend fun deleteAddress(addressId: String): Flow<RequestState<Boolean>> = flow {
        emit(RequestState.Loading)

        try {
            delay(300)
            val result = apiService.deleteAddress(addressId)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to delete address: ${e.message}"))
        }
    }

    override suspend fun updateUser(
        userDocumentId: String,
        request: UserUpdateRequest
    ): Flow<RequestState<UserUpdateResponse>> = flow {
        emit(RequestState.Loading)

        try {
            delay(300)
            val result = apiService.updateUser(userDocumentId, request)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to update user: ${e.message}"))
        }
    }

    override suspend fun addUserToAddress(
        userId: Int,
        addressId: String,
    ): Flow<RequestState<AddUserToAddressResponse>> = flow {
        emit(RequestState.Loading)
        try {
            delay(300)
            val result = apiService.addUserToAddress(userId, addressId)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to update user: ${e.message}"))
        }
    }

}