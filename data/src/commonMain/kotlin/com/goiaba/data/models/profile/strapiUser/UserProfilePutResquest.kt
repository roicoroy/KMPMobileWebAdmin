package com.goiaba.data.models.profile.strapiUser

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfilePutResquest(
    @SerialName("data")
    val `data`: Data = Data()
) {
    @Serializable
    data class Data(
        @SerialName("addresses")
        val addresses: String = "",
        @SerialName("adverts")
        val adverts: List<Int> = listOf(),
        @SerialName("avatar")
        val avatar: String = "",
        @SerialName("dob")
        val dob: String = "",
        @SerialName("user")
        val user: String = ""
    )
}