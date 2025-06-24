package com.goiaba.data.models.profile.adress


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddUserToAddressResponse(
    @SerialName("data")
    val `data`: Data = Data(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("city")
        val city: String? = null,
        @SerialName("country")
        val country: String? = null,
        @SerialName("createdAt")
        val createdAt: String = "",
        @SerialName("documentId")
        val documentId: String = "",
        @SerialName("first_line_address")
        val firstLineAddress: String = "",
        @SerialName("first_name")
        val firstName: String = "",
        @SerialName("id")
        val id: Int = 0,
        @SerialName("last_name")
        val lastName: String = "",
        @SerialName("phone_number")
        val phoneNumber: String? = null,
        @SerialName("post_code")
        val postCode: String = "",
        @SerialName("publishedAt")
        val publishedAt: String = "",
        @SerialName("second_line_address")
        val secondLineAddress: String = "",
        @SerialName("updatedAt")
        val updatedAt: String = ""
    )

    @Serializable
    class Meta
}