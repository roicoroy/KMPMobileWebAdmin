package com.goiaba.data.models.profile


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "data": {
    "addresses": [
      "38"
    ]
  }
}
*/
@Serializable
data class Data(
    @SerialName("addresses")
    val addresses: List<String>
)