package com.goiaba.data.models.profile


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "data": {
    "id": 1,
    "documentId": "hx64nkwcpgta4lmtwk7p4lyv",
    "dob": "2001-01-01",
    "createdAt": "2025-06-22T11:25:12.795Z",
    "updatedAt": "2025-06-23T20:43:26.546Z",
    "publishedAt": "2025-06-23T20:43:26.544Z"
  },
  "meta": {}
}
*/
@Serializable
data class PutAddressToProfileResponse(
    @SerialName("data")
    val `data`: DataXX,
    @SerialName("meta")
    val meta: Meta
)
@Serializable
data class DataXX(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("dob")
    val dob: String,
    @SerialName("documentId")
    val documentId: String,
    @SerialName("id")
    val id: Int,
    @SerialName("publishedAt")
    val publishedAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)
