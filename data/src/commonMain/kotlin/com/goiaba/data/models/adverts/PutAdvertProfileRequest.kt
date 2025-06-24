// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.goiaba.data.models.adverts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PutAdvertProfileRequest(
    @SerialName("data")
    val `data`: Data = Data()
) {
    @Serializable
    data class Data(
        @SerialName("adverts")
        val adverts: List<String> = listOf()
    )
}
