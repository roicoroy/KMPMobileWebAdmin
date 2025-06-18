package com.goiaba.data.models.adverts


import com.goiaba.data.models.profile.strapiUser.StrapiUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdvertGetResponse(
    @SerialName("data")
    val `data`: List<Advert>,
    @SerialName("meta")
    val meta: Meta
) {
    @Serializable
    data class Advert(
        @SerialName("category")
        val category: Category,
        @SerialName("cover")
        val cover: Cover,
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("description")
        val description: String,
        @SerialName("documentId")
        val documentId: String,
        @SerialName("id")
        val id: Int,
        @SerialName("publishedAt")
        val publishedAt: String,
        @SerialName("slug")
        val slug: String? = null,
        @SerialName("title")
        val title: String,
        @SerialName("updatedAt")
        val updatedAt: String,
        @SerialName("user")
        val user: StrapiUser? = null
    ) {
        @Serializable
        data class Category(
            @SerialName("createdAt")
            val createdAt: String,
            @SerialName("description")
            val description: String,
            @SerialName("documentId")
            val documentId: String,
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("publishedAt")
            val publishedAt: String,
            @SerialName("slug")
            val slug: String? = null,
            @SerialName("updatedAt")
            val updatedAt: String
        )

        @Serializable
        data class Cover(
            @SerialName("alternativeText")
            val alternativeText: String? = null,
            @SerialName("caption")
            val caption: String? = null,
            @SerialName("createdAt")
            val createdAt: String,
            @SerialName("documentId")
            val documentId: String,
            @SerialName("ext")
            val ext: String? = null,
            @SerialName("formats")
            val formats: Formats? = null,
            @SerialName("hash")
            val hash: String? = null,
            @SerialName("height")
            val height: Int,
            @SerialName("id")
            val id: Int,
            @SerialName("mime")
            val mime: String,
            @SerialName("name")
            val name: String,
            @SerialName("previewUrl")
            val previewUrl: String? = null,
            @SerialName("provider")
            val provider: String,
            @SerialName("provider_metadata")
            val providerMetadata: ProviderMetadata? = null,
            @SerialName("publishedAt")
            val publishedAt: String,
            @SerialName("size")
            val size: Double,
            @SerialName("updatedAt")
            val updatedAt: String,
            @SerialName("url")
            val url: String,
            @SerialName("width")
            val width: Int
        ) {
            @Serializable
            data class Formats(
                @SerialName("large")
                val large: Large? = null,
                @SerialName("medium")
                val medium: Medium? = null,
                @SerialName("small")
                val small: Small? = null,
                @SerialName("thumbnail")
                val thumbnail: Thumbnail? = null
            ) {
                @Serializable
                data class Large(
                    @SerialName("ext")
                    val ext: String,
                    @SerialName("hash")
                    val hash: String,
                    @SerialName("height")
                    val height: Int,
                    @SerialName("mime")
                    val mime: String,
                    @SerialName("name")
                    val name: String,
                    @SerialName("path")
                    val path: String? = null,
                    @SerialName("provider_metadata")
                    val providerMetadata: ProviderMetadata? = null,
                    @SerialName("size")
                    val size: Double,
                    @SerialName("sizeInBytes")
                    val sizeInBytes: Int,
                    @SerialName("url")
                    val url: String,
                    @SerialName("width")
                    val width: Int
                )

                @Serializable
                data class Medium(
                    @SerialName("ext")
                    val ext: String,
                    @SerialName("hash")
                    val hash: String,
                    @SerialName("height")
                    val height: Int,
                    @SerialName("mime")
                    val mime: String,
                    @SerialName("name")
                    val name: String,
                    @SerialName("path")
                    val path: String? = null,
                    @SerialName("provider_metadata")
                    val providerMetadata: ProviderMetadata? = null,
                    @SerialName("size")
                    val size: Double,
                    @SerialName("sizeInBytes")
                    val sizeInBytes: Int,
                    @SerialName("url")
                    val url: String,
                    @SerialName("width")
                    val width: Int
                )

                @Serializable
                data class Small(
                    @SerialName("ext")
                    val ext: String,
                    @SerialName("hash")
                    val hash: String,
                    @SerialName("height")
                    val height: Int,
                    @SerialName("mime")
                    val mime: String,
                    @SerialName("name")
                    val name: String,
                    @SerialName("path")
                    val path: String? = null,
                    @SerialName("provider_metadata")
                    val providerMetadata: ProviderMetadata? = null,
                    @SerialName("size")
                    val size: Double,
                    @SerialName("sizeInBytes")
                    val sizeInBytes: Int,
                    @SerialName("url")
                    val url: String,
                    @SerialName("width")
                    val width: Int
                )

                @Serializable
                data class Thumbnail(
                    @SerialName("ext")
                    val ext: String,
                    @SerialName("hash")
                    val hash: String,
                    @SerialName("height")
                    val height: Int,
                    @SerialName("mime")
                    val mime: String,
                    @SerialName("name")
                    val name: String,
                    @SerialName("path")
                    val path: String? = null,
                    @SerialName("provider_metadata")
                    val providerMetadata: ProviderMetadata? = null,
                    @SerialName("size")
                    val size: Double,
                    @SerialName("sizeInBytes")
                    val sizeInBytes: Int,
                    @SerialName("url")
                    val url: String,
                    @SerialName("width")
                    val width: Int
                )
            }
            
            @Serializable
            data class ProviderMetadata(
                @SerialName("public_id")
                val publicId: String? = null,
                @SerialName("resource_type")
                val resourceType: String? = null
            )
        }
    }

    @Serializable
    data class Meta(
        @SerialName("pagination")
        val pagination: Pagination
    ) {
        @Serializable
        data class Pagination(
            @SerialName("page")
            val page: Int,
            @SerialName("pageCount")
            val pageCount: Int,
            @SerialName("pageSize")
            val pageSize: Int,
            @SerialName("total")
            val total: Int
        )
    }
}