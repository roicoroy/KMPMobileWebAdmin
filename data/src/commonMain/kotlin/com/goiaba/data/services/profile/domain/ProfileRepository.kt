package com.goiaba.data.services.profile.domain

import com.goiaba.data.models.profile.AddUserToAddressResponse
import com.goiaba.data.models.profile.AddressCreateRequest
import com.goiaba.data.models.profile.AddressCreateResponse
import com.goiaba.data.models.profile.AddressUpdateRequest
import com.goiaba.data.models.profile.AddressUpdateResponse
import com.goiaba.data.models.profile.UserUpdateRequest
import com.goiaba.data.models.profile.UserUpdateResponse
import com.goiaba.data.models.profile.UsersMeResponse
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUsersMe(): Flow<RequestState<UsersMeResponse>>
    suspend fun createAddress(request: AddressCreateRequest): Flow<RequestState<AddressCreateResponse>>
    suspend fun updateAddress(addressId: String, request: AddressUpdateRequest): Flow<RequestState<AddressUpdateResponse>>
    suspend fun deleteAddress(addressId: String): Flow<RequestState<Boolean>>
    suspend fun updateUser(userDocumentId: String, request: UserUpdateRequest): Flow<RequestState<UserUpdateResponse>>
    suspend fun addUserToAddress(userId: Int, addressId: String): Flow<RequestState<AddUserToAddressResponse>>
}