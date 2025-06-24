package com.goiaba.data.models.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PutAddressToProfileRequest(
    @SerialName("data")
    val `data`: Data = Data()
) {
    @Serializable
    data class Data(
        @SerialName("addresses")
        val addresses: List<String> = listOf()
    )
}