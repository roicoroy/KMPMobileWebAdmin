package com.goiaba.data.models.logger


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "data": {
    "id": 3,
    "attributes": {
      "cowName": "HHHH",
      "date": "2022-12-27",
      "createdAt": "2025-06-08T14:33:11.515Z",
      "updatedAt": "2025-06-08T14:33:11.515Z"
    }
  },
  "meta": {}
}
*/
@Serializable
data class LoggerPostResponse(
    @SerialName("data")
    val `data`: Data,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class Data(
        @SerialName("attributes")
        val attributes: Attributes,
        @SerialName("id")
        val id: Int
    ) {
        @Serializable
        data class Attributes(
            @SerialName("cowName")
            val cowName: String,
            @SerialName("createdAt")
            val createdAt: String,
            @SerialName("date")
            val date: String,
            @SerialName("updatedAt")
            val updatedAt: String
        )
    }

    @Serializable
    class Meta
}