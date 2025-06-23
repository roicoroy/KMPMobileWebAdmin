package com.goiaba.data.services.profile.domain

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
import com.goiaba.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    /**
     * Retrieves the profile of the currently authenticated user.
     *
     * @return A Flow that emits the current state of the request, which contains
     * the user profile details on success.
     */
    fun getUsersMe(): Flow<RequestState<StrapiUser>>

    /**
     * Retrieves the profile details of a user by their document ID.
     *
     * @param userDocumentId The unique identifier of the user's document.
     * @return A Flow that emits the current state of the request, including the user's profile on success.
     */
    fun getUserProfile(userDocumentId: String): Flow<RequestState<StrapiProfile>>

    /**
     * Creates an address in the system.
     *
     * @param request The details of the address to be created.
     * @return A Flow that emits the result of the address creation operation.
     */
    suspend fun createAddress(request: AddressCreateRequest): Flow<RequestState<AddressCreateResponse>>

    /**
     * Updates an existing address by its ID.
     *
     * @param addressId The identifier of the address to update.
     * @param request The updated details for the address.
     * @return A Flow that emits the result of the address update operation.
     */
    suspend fun updateAddress(
        addressId: String,
        request: AddressUpdateRequest
    ): Flow<RequestState<AddressUpdateResponse>>

    /**
     * Deletes an address by its ID.
     *
     * @param addressId The unique identifier of the address to delete.
     * @return A Flow that emits the result of the address deletion operation.
     */
    suspend fun deleteAddress(addressId: String): Flow<RequestState<Boolean>>

    /**
     * Updates a user's details in the system.
     *
     * @param userDocumentId The unique identifier of the user's document.
     * @param request The updated details for the user.
     * @return A Flow that emits the result of the user update operation.
     */
    suspend fun updateUser(
        userDocumentId: String,
        request: UserUpdateRequest
    ): Flow<RequestState<UserUpdateResponse>>

    /**
     * Associates a user with an address in the system.
     *
     * @param userId The ID of the user to associate with the address.
     * @param addressId The ID of the address to which the user is being assigned.
     * @return A Flow that emits the result of linking the user and address.
     */
    suspend fun addUserToAddress(
        userId: Int,
        addressId: String
    ): Flow<RequestState<AddUserToAddressResponse>>

    /**
     * Uploads a user-selected image to the server and links it to the user's profile as an avatar.
     * On successful operation, the image is immediately reflected in the user's profile.
     *
     * @param userId The ID of the user for whom the image is being uploaded.
     * @param uploadedImageId The unique ID of the uploaded image resource.
     * @return A Flow that emits the result of the image upload and profile association process.
     */
    suspend fun addImageToProfile(
        userId: Int,
        uploadedImageId: String
    ): Flow<RequestState<PutProfileResponse>>
}