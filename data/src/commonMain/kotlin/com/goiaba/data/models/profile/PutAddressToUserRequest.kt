package com.goiaba.data.models.profile


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//http://localhost:1337/api/addresses/:addressId
@Serializable
data class PutAddressToUserRequest(
    @SerialName("data")
    val `data`: Data = Data()
) {
    @Serializable
    data class Data(
        @SerialName("user")
        val userId: Int = 0
    )
}