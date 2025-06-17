package com.goiaba.data.models.profile


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PutAddressRequest(
    @SerialName("data")
    val `data`: Data = Data()
) {
    @Serializable
    data class Data(
        @SerialName("user")
        val user: Int? = null
    )
}