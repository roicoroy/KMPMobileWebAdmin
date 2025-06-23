package com.goiaba.data.services.profile

import com.goiaba.data.models.profile.AddUserToAddressResponse
import com.goiaba.data.models.profile.AddressCreateRequest
import com.goiaba.data.models.profile.AddressCreateResponse
import com.goiaba.data.models.profile.AddressUpdateRequest
import com.goiaba.data.models.profile.AddressUpdateResponse
import com.goiaba.data.models.profile.UserUpdateRequest
import com.goiaba.data.models.profile.UserUpdateResponse
import com.goiaba.data.models.profile.strapiUser.PutProfileResponse
import com.goiaba.data.models.profile.strapiUser.StrapiProfile
import com.goiaba.data.models.profile.strapiUser.StrapiUser
import com.goiaba.data.services.profile.domain.ProfileRepository
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileImpl : ProfileRepository {
    // Service instance to communicate with remote APIs
    private val apiService = ProfileService()

    /**
     * Fetches and streams the current user's profile from the server.
     * The flow emits a loading state, followed by the success or error state.
     */
    override fun getUsersMe(): Flow<RequestState<StrapiUser>> = flow {
        emit(RequestState.Loading) // Emit loading state to indicate the operation has started.

        try {
            delay(500) // Simulate latency for better UX (e.g., displaying progress).

            // Fetch user data from the API service.
            val result = apiService.getUsersMe()

            // Emit success or error based on the result.
            if (result.isSuccess()) {
                emit(RequestState.Success(result.getSuccessData()))
            } else {
                emit(RequestState.Error("Unknown error occurred"))
            }
        } catch (e: Exception) {
            // Catch unexpected errors and emit an error message.
            emit(RequestState.Error("Failed to fetch user: ${e.message}"))
        }
    }

    /**
     * Fetches and streams a specific user's profile by their document ID.
     *
     * @param userDocumentId The document ID of the user to fetch.
     * @return A Flow that emits the loading, success, or error states.
     */
    override fun getUserProfile(userDocumentId: String): Flow<RequestState<StrapiProfile>> = flow {
        emit(RequestState.Loading) // Emit loading state at the start of the request.

        try {
            delay(500) // Simulate network latency.

            // Call API service to get the user's profile.
            val result = apiService.getUserProfile(userDocumentId)

            // Evaluate API response and emit the appropriate state.
            if (result.isSuccess()) {
                emit(RequestState.Success(result.getSuccessData()))
            } else {
                emit(RequestState.Error("Unknown error occurred"))
            }
        } catch (e: Exception) {
            // Emit an error state with details in case of an exception.
            emit(RequestState.Error("Failed to fetch user profile: ${e.message}"))
        }
    }

    /**
     * Creates a new address and streams the operation's outcome.
     *
     * @param request The details required to create the address.
     * @return A Flow emitting the success or error state of the operation.
     */
    override suspend fun createAddress(request: AddressCreateRequest): Flow<RequestState<AddressCreateResponse>> =
        flow {
            emit(RequestState.Loading) // Indicate that the process has started.

            try {
                delay(300) // Simulate minimal latency.

                // Forward the request to the API service and emit the result.
                val result = apiService.createAddress(request)
                emit(result)
            } catch (e: Exception) {
                // Emit an error state if the operation fails.
                emit(RequestState.Error("Failed to create address: ${e.message}"))
            }
        }

    /**
     * Updates an existing address with new details.
     *
     * @param addressId The ID of the address to be updated.
     * @param request The updated address details.
     * @return A Flow emitting the success or failure of the operation.
     */
    override suspend fun updateAddress(
        addressId: String,
        request: AddressUpdateRequest
    ): Flow<RequestState<AddressUpdateResponse>> = flow {
        emit(RequestState.Loading) // Emit loading state to represent progress.

        try {
            delay(300) // Simulate latency.

            // Perform the update via the API service and emit the result.
            val result = apiService.updateAddress(addressId, request)
            emit(result)
        } catch (e: Exception) {
            // Handle any exceptions and emit an error message.
            emit(RequestState.Error("Failed to update address: ${e.message}"))
        }
    }

    /**
     * Deletes an address by its ID.
     *
     * @param addressId The ID of the address to delete.
     * @return A Flow emitting whether the deletion was successful or failed.
     */
    override suspend fun deleteAddress(addressId: String): Flow<RequestState<Boolean>> = flow {
        emit(RequestState.Loading) // Signal the start of the deletion process.

        try {
            delay(300) // Simulate a short delay.

            // Request API service to delete the address and emit the result.
            val result = apiService.deleteAddress(addressId)
            emit(result)
        } catch (e: Exception) {
            // Catch errors and notify via an error state.
            emit(RequestState.Error("Failed to delete address: ${e.message}"))
        }
    }

    /**
     * Updates user's information such as username, email, etc.
     *
     * @param userDocumentId The ID of the user to update.
     * @param request The updated user details.
     * @return A Flow emitting the outcome of the update process.
     */
    override suspend fun updateUser(
        userDocumentId: String,
        request: UserUpdateRequest
    ): Flow<RequestState<UserUpdateResponse>> = flow {
        emit(RequestState.Loading) // Notify that the update has started.

        try {
            delay(300) // Simulate minimal latency.

            // Forward the update request and stream the result status.
            val result = apiService.updateUser(userDocumentId, request)
            emit(result)
        } catch (e: Exception) {
            emit(RequestState.Error("Failed to update user: ${e.message}"))
        }
    }

    /**
     * Links a user with a specific address to indicate ownership or usage.
     *
     * @param userId The unique ID of the user.
     * @param addressId The ID of the address being linked to the user.
     * @return A Flow emitting the result of the linking operation.
     */
    override suspend fun addUserToAddress(
        userId: Int,
        addressId: String,
    ): Flow<RequestState<AddUserToAddressResponse>> =
        flow {
            emit(RequestState.Loading) // Emit progress state.

            try {
                delay(300) // Simulate latency.

                // Call the API service to perform the linking and emit the response.
                val result = apiService.addUserToAddress(userId, addressId)
                emit(result)
            } catch (e: Exception) {
                emit(RequestState.Error("Failed to link user to address: ${e.message}"))
            }
        }

    /**
     * Uploads a user's selected image and links it as their profile avatar.
     *
     * @param userId The ID of the user for whom the image is being uploaded.
     * @param uploadedImageId The unique identifier of the image resource.
     * @return A Flow emitting the outcome of the image upload and linking.
     */
    override suspend fun addImageToProfile(
        userId: Int,
        uploadedImageId: String
    ): Flow<RequestState<PutProfileResponse>> {
        // Implementation yet to be done.
        TODO("Not yet implemented")
    }
}