package com.goiaba.data.models.profile.adress


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddUserToAddressRequest(
    @SerialName("data")
    val `data`: Data = Data()
) {
    @Serializable
    data class Data(
        @SerialName("user")
        // UserId
        val user: Int = 0
    )
}