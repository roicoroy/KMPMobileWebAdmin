package com.goiaba.data.models.logger


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "data": {
    "cowName": "HHHH",
    "date": "2022-12-27 08:26:49.219717",
    "image": 3
  }
}
*/
@Serializable
data class LoggerPostRequest(
    @SerialName("data")
    val `data`: Data
) {
    @Serializable
    data class Data(
        @SerialName("cowName")
        val cowName: String,
        @SerialName("date")
        val date: String,
        @SerialName("image")
        val image: Int
    )
}