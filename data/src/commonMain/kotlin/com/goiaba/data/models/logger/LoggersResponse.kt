package com.goiaba.data.models.logger


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "data": [
    {
      "id": 1,
      "attributes": {
        "cowName": "mimosa",
        "date": "2025-06-05",
        "createdAt": "2025-06-05T11:13:01.035Z",
        "updatedAt": "2025-06-05T11:13:01.035Z",
        "image": {
          "data": {
            "id": 1,
            "attributes": {
              "name": "Free-Cow-SVG-Clipart-1.jpg",
              "alternativeText": null,
              "caption": null,
              "width": 1820,
              "height": 1214,
              "formats": {
                "large": {
                  "ext": ".jpg",
                  "url": "https://res.cloudinary.com/roicoroy/image/upload/v1749121977/large_Free_Cow_SVG_Clipart_1_9ddbc51e2f.jpg",
                  "hash": "large_Free_Cow_SVG_Clipart_1_9ddbc51e2f",
                  "mime": "image/jpeg",
                  "name": "large_Free-Cow-SVG-Clipart-1.jpg",
                  "path": null,
                  "size": 35.43,
                  "width": 1000,
                  "height": 667,
                  "sizeInBytes": 35430,
                  "provider_metadata": {
                    "public_id": "large_Free_Cow_SVG_Clipart_1_9ddbc51e2f",
                    "resource_type": "image"
                  }
                },
                "small": {
                  "ext": ".jpg",
                  "url": "https://res.cloudinary.com/roicoroy/image/upload/v1749121976/small_Free_Cow_SVG_Clipart_1_9ddbc51e2f.jpg",
                  "hash": "small_Free_Cow_SVG_Clipart_1_9ddbc51e2f",
                  "mime": "image/jpeg",
                  "name": "small_Free-Cow-SVG-Clipart-1.jpg",
                  "path": null,
                  "size": 13.35,
                  "width": 500,
                  "height": 334,
                  "sizeInBytes": 13348,
                  "provider_metadata": {
                    "public_id": "small_Free_Cow_SVG_Clipart_1_9ddbc51e2f",
                    "resource_type": "image"
                  }
                },
                "medium": {
                  "ext": ".jpg",
                  "url": "https://res.cloudinary.com/roicoroy/image/upload/v1749121977/medium_Free_Cow_SVG_Clipart_1_9ddbc51e2f.jpg",
                  "hash": "medium_Free_Cow_SVG_Clipart_1_9ddbc51e2f",
                  "mime": "image/jpeg",
                  "name": "medium_Free-Cow-SVG-Clipart-1.jpg",
                  "path": null,
                  "size": 23.82,
                  "width": 750,
                  "height": 500,
                  "sizeInBytes": 23817,
                  "provider_metadata": {
                    "public_id": "medium_Free_Cow_SVG_Clipart_1_9ddbc51e2f",
                    "resource_type": "image"
                  }
                },
                "thumbnail": {
                  "ext": ".jpg",
                  "url": "https://res.cloudinary.com/roicoroy/image/upload/v1749121976/thumbnail_Free_Cow_SVG_Clipart_1_9ddbc51e2f.jpg",
                  "hash": "thumbnail_Free_Cow_SVG_Clipart_1_9ddbc51e2f",
                  "mime": "image/jpeg",
                  "name": "thumbnail_Free-Cow-SVG-Clipart-1.jpg",
                  "path": null,
                  "size": 4.3,
                  "width": 234,
                  "height": 156,
                  "sizeInBytes": 4297,
                  "provider_metadata": {
                    "public_id": "thumbnail_Free_Cow_SVG_Clipart_1_9ddbc51e2f",
                    "resource_type": "image"
                  }
                }
              },
              "hash": "Free_Cow_SVG_Clipart_1_9ddbc51e2f",
              "ext": ".jpg",
              "mime": "image/jpeg",
              "size": 75.91,
              "url": "https://res.cloudinary.com/roicoroy/image/upload/v1749121977/Free_Cow_SVG_Clipart_1_9ddbc51e2f.jpg",
              "previewUrl": null,
              "provider": "cloudinary",
              "provider_metadata": {
                "public_id": "Free_Cow_SVG_Clipart_1_9ddbc51e2f",
                "resource_type": "image"
              },
              "createdAt": "2025-06-05T11:12:57.696Z",
              "updatedAt": "2025-06-05T11:12:57.696Z"
            }
          }
        }
      }
    }
  ],
  "meta": {
    "pagination": {
      "page": 1,
      "pageSize": 25,
      "pageCount": 1,
      "total": 1
    }
  }
}
*/
@Serializable
data class LoggersResponse(
    @SerialName("data")
    val `data`: List<Data>,
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
            @SerialName("image")
            val image: Image,
            @SerialName("updatedAt")
            val updatedAt: String
        ) {
            @Serializable
            data class Image(
                @SerialName("data")
                val `data`: Data?
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
                        @SerialName("alternativeText")
                        val alternativeText: String? = null,
                        @SerialName("caption")
                        val caption: String? = null,
                        @SerialName("createdAt")
                        val createdAt: String,
                        @SerialName("ext")
                        val ext: String,
                        @SerialName("formats")
                        val formats: Formats? = null,
                        @SerialName("hash")
                        val hash: String,
                        @SerialName("height")
                        val height: Int,
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
                            ) {
                                @Serializable
                                data class ProviderMetadata(
                                    @SerialName("public_id")
                                    val publicId: String,
                                    @SerialName("resource_type")
                                    val resourceType: String
                                )
                            }

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
                            ) {
                                @Serializable
                                data class ProviderMetadata(
                                    @SerialName("public_id")
                                    val publicId: String,
                                    @SerialName("resource_type")
                                    val resourceType: String
                                )
                            }

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
                            ) {
                                @Serializable
                                data class ProviderMetadata(
                                    @SerialName("public_id")
                                    val publicId: String,
                                    @SerialName("resource_type")
                                    val resourceType: String
                                )
                            }

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
                            ) {
                                @Serializable
                                data class ProviderMetadata(
                                    @SerialName("public_id")
                                    val publicId: String,
                                    @SerialName("resource_type")
                                    val resourceType: String
                                )
                            }
                        }

                        @Serializable
                        data class ProviderMetadata(
                            @SerialName("public_id")
                            val publicId: String,
                            @SerialName("resource_type")
                            val resourceType: String
                        )
                    }
                }
            }
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