package com.goiaba.data.models.profile.strapiUser


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "data": {
    "id": 1,
    "documentId": "hx64nkwcpgta4lmtwk7p4lyv",
    "dob": "2011-10-05",
    "createdAt": "2025-06-22T11:25:12.795Z",
    "updatedAt": "2025-06-22T12:38:24.369Z",
    "publishedAt": "2025-06-22T12:38:24.365Z"
  },
  "meta": {}
}
*/
@Serializable
data class PutProfileResponse(
    @SerialName("data")
    val `data`: Data,
    @SerialName("meta")
    val meta: Meta
)