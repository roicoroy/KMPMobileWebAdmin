package com.goiaba.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goiaba.data.models.profile.AddressCreateRequest
import com.goiaba.data.models.profile.AddressUpdateRequest
import com.goiaba.data.models.profile.UserUpdateRequest
import com.goiaba.data.models.profile.strapiUser.StrapiProfile
import com.goiaba.data.models.profile.strapiUser.StrapiUser
import com.goiaba.data.models.profile.strapiUser.UserProfilePutResquest
import com.goiaba.data.services.logger.domain.LoggerRepository
import com.goiaba.data.services.profile.domain.ProfileRepository
import com.goiaba.shared.util.RequestState
import com.goiaba.shared.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileViewModel : ViewModel(), KoinComponent {

    private val profileRepository: ProfileRepository by inject()
    private val loggerRepository: LoggerRepository by inject()

    private val _welcomeText = MutableStateFlow("Hello View")
    val welcomeText: StateFlow<String> = _welcomeText.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(TokenManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    private val _userEmail = MutableStateFlow(TokenManager.getUserEmail())
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _user = MutableStateFlow<RequestState<StrapiUser>>(RequestState.Loading)
    val user: StateFlow<RequestState<StrapiUser>> = _user.asStateFlow()

    private val _strapiProfile = MutableStateFlow<RequestState<StrapiProfile>>(RequestState.Loading)
    val strapiProfile: StateFlow<RequestState<StrapiProfile>> = _strapiProfile.asStateFlow()

    // Address editing state
    private val _isUpdatingAddress = MutableStateFlow(false)
    val isUpdatingAddress: StateFlow<Boolean> = _isUpdatingAddress.asStateFlow()

    private val _updateMessage = MutableStateFlow<String?>(null)
    val updateMessage: StateFlow<String?> = _updateMessage.asStateFlow()

    // Image upload state
    private val _uploadedImageId = MutableStateFlow<Int?>(null)
    val uploadedImageId: StateFlow<Int?> = _uploadedImageId.asStateFlow()

    init {
        updateAuthState()
        if (_isLoggedIn.value) {
            loadUserProfile()
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _strapiProfile.value = RequestState.Loading
            _user.value = RequestState.Loading

            try {
                profileRepository.getUsersMe().collect { result ->
                    _user.value = result
                }

                _userRole.value = _user.value.getSuccessData().role.name
                val userProfiledocId = _user.value.getSuccessData().profile.documentId

                profileRepository.getUserProfile(userProfiledocId).collect { result ->
                    _strapiProfile.value = result
                }

            } catch (e: Exception) {
                _user.value = RequestState.Error("Failed to load profile: ${e.message}")
            }
        }
    }

    fun uploadProfileImage(imageData: ByteArray, fileName: String) {
        viewModelScope.launch {
            try {
                loggerRepository.uploadImageToStrapi(imageData, fileName).collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            // Loading state is handled in the UI
                        }

                        is RequestState.Success -> {
                            // Get the first uploaded file ID
                            val uploadedFile = result.data.files.firstOrNull()
                            if (uploadedFile != null) {
                                _uploadedImageId.value = uploadedFile.id
                                if (uploadedFile.id != null) {
                                    updateUserProfile(uploadedFile.id.toString())
                                }

                                _updateMessage.value = "Image uploaded successfully!"

                            } else {
                                _updateMessage.value = "Image upload failed: No file returned"
                            }
                        }

                        is RequestState.Error -> {
                            _updateMessage.value = "Image upload failed: ${result.message}"
                        }

                        else -> {
                            _updateMessage.value = "Image upload failed: Unknown error"
                        }
                    }
                }
            } catch (e: Exception) {
                _updateMessage.value = "Image upload error: ${e.message}"
            }
        }
    }

    fun updateUserProfile(uploadedFileId: String) {
        viewModelScope.launch {
            try {
                val userProfiledocId =
                    _user.value.getSuccessData().profile.documentId
                if (userProfiledocId.isNotEmpty()) {
                    profileRepository
                        .updateProfile(
                            userProfiledocId,
                            UserProfilePutResquest(
                                data = UserProfilePutResquest.Data(
                                    avatar = uploadedFileId,
                                )
                            )
                        ).collect { result ->
                        when (result) {
                            is RequestState.Loading -> {
                                // Loading state is handled in the UI
                            }
                            is RequestState.Success -> {
                                _updateMessage.value = "Profile Updated successfully!"
                                refreshProfile()
                            }

                            is RequestState.Error -> {
                                _updateMessage.value = "Image upload failed: ${result.message}"
                            }

                            else -> {
                                _updateMessage.value = "Image upload failed: Unknown error"
                            }
                        }
                    }




                }
            } catch (e: Exception) {
                _updateMessage.value = "ERROR: ${e.message}"
            }
        }
    }

    fun createAddress(
        firstName: String,
        lastName: String,
        firstLineAddress: String,
        secondLineAddress: String?,
        postCode: String,
        city: String?,
        country: String?,
        phoneNumber: String?
    ) {
        viewModelScope.launch {
            _isUpdatingAddress.value = true
            _updateMessage.value = null

            try {
                // Step 1: Create the address
                val request = AddressCreateRequest(
                    data = AddressCreateRequest.AddressCreateData(
                        firstName = firstName,
                        lastName = lastName,
                        firstLineAddress = firstLineAddress,
                        secondLineAddress = secondLineAddress,
                        postCode = postCode,
                        city = city,
                        country = country,
                        phoneNumber = phoneNumber
                    )
                )

                profileRepository.createAddress(request).collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            _isUpdatingAddress.value = true
                        }

                        is RequestState.Success -> {
                            // Step 2: Get current user data to update the addresses relation
                            val currentUser = _user.value.getSuccessDataOrNull()
                            if (currentUser != null) {
                                // Step 3: Update user with new address relation
                                addUserToAddress(currentUser.id, result.data.data.documentId)
                            } else {
                                _isUpdatingAddress.value = false
                                _updateMessage.value =
                                    "Address created but failed to link to user profile"
                                loadUserProfile() // Still refresh to show the address
                            }
                        }

                        is RequestState.Error -> {
                            _isUpdatingAddress.value = false
                            _updateMessage.value = "Failed to create address: ${result.message}"
                        }

                        else -> {
                            _isUpdatingAddress.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _isUpdatingAddress.value = false
                _updateMessage.value = "Error creating address: ${e.message}"
            }
        }
    }

    private suspend fun addUserToAddress(userId: Int, addressId: String) {
        try {
            profileRepository.addUserToAddress(userId, addressId).collect { result ->
                when (result) {
                    is RequestState.Success -> {
                        _isUpdatingAddress.value = false
                        _updateMessage.value = "Address created and linked successfully!"
                        // Refresh profile to get updated data
                        loadUserProfile()
                    }

                    is RequestState.Error -> {
                        _isUpdatingAddress.value = false
                        _updateMessage.value =
                            "Address created but failed to link: ${result.message}"
                        // Still refresh to show the address
                        loadUserProfile()
                    }

                    else -> {
                        // Keep loading state
                    }
                }
            }
        } catch (e: Exception) {
            _isUpdatingAddress.value = false
            _updateMessage.value = "Address created but linking failed: ${e.message}"
            loadUserProfile()
        }
    }

    fun updateAddress(
        addressId: String,
        firstName: String,
        lastName: String,
        firstLineAddress: String,
        secondLineAddress: String?,
        postCode: String,
        city: String?,
        country: String?,
        phoneNumber: String?
    ) {
        viewModelScope.launch {
            _isUpdatingAddress.value = true
            _updateMessage.value = null

            try {
                val request = AddressUpdateRequest(
                    data = AddressUpdateRequest.AddressData(
                        firstName = firstName,
                        lastName = lastName,
                        firstLineAddress = firstLineAddress,
                        secondLineAddress = secondLineAddress,
                        postCode = postCode,
                        city = city,
                        country = country,
                        phoneNumber = phoneNumber
                    )
                )

                profileRepository.updateAddress(addressId, request).collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            _isUpdatingAddress.value = true
                        }

                        is RequestState.Success -> {
                            _isUpdatingAddress.value = false
                            _updateMessage.value = "Address updated successfully!"
                            // Refresh profile to get updated data
                            loadUserProfile()
                        }

                        is RequestState.Error -> {
                            _isUpdatingAddress.value = false
                            _updateMessage.value = "Failed to update address: ${result.message}"
                        }

                        else -> {
                            _isUpdatingAddress.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _isUpdatingAddress.value = false
                _updateMessage.value = "Error updating address: ${e.message}"
            }
        }
    }

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            _isUpdatingAddress.value = true
            _updateMessage.value = null

            try {
                // Step 1: Delete the address
                profileRepository.deleteAddress(addressId).collect { result ->
                    when (result) {
                        is RequestState.Loading -> {
                            _isUpdatingAddress.value = true
                        }

                        is RequestState.Success -> {
                            // Step 2: Update user to remove the address relation
                            val currentUser = _user.value.getSuccessDataOrNull()
                            if (currentUser != null) {
//                                val updatedAddressIds = currentUser.addresses
//                                    .filter { it.documentId != addressId }
//                                    .map { it.documentId }
//
//                                // Step 3: Update user with removed address relation
//                                updateUserAddressesAfterDelete(currentUser.documentId, updatedAddressIds)
                            } else {
                                _isUpdatingAddress.value = false
                                _updateMessage.value = "Address deleted successfully!"
                                loadUserProfile()
                            }
                        }

                        is RequestState.Error -> {
                            _isUpdatingAddress.value = false
                            _updateMessage.value = "Failed to delete address: ${result.message}"
                        }

                        else -> {
                            _isUpdatingAddress.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _isUpdatingAddress.value = false
                _updateMessage.value = "Error deleting address: ${e.message}"
            }
        }
    }

    fun refreshProfile() {
        if (_isLoggedIn.value) {
            loadUserProfile()
        }
    }

    fun updateAuthState() {
        _isLoggedIn.value = TokenManager.isLoggedIn()
        _userEmail.value = TokenManager.getUserEmail()

        // Load profile if user is logged in
        if (_isLoggedIn.value && _user.value is RequestState.Idle) {
            loadUserProfile()
        }
    }

    fun clearUpdateMessage() {
        _updateMessage.value = null
    }
}