package com.goiaba.shared.util

import androidx.compose.runtime.Composable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ImagePicker {
    suspend fun pickImage(): ImagePickerResult?
    suspend fun saveImageToGallery(imageData: ByteArray, fileName: String): Boolean
}

sealed class ImagePickerResult {
    data class Success(
        val imageData: ByteArray,
        val fileName: String,
        val mimeType: String = "image/jpeg"
    ) : ImagePickerResult() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Success

            if (!imageData.contentEquals(other.imageData)) return false
            if (fileName != other.fileName) return false
            if (mimeType != other.mimeType) return false

            return true
        }

        override fun hashCode(): Int {
            var result = imageData.contentHashCode()
            result = 31 * result + fileName.hashCode()
            result = 31 * result + mimeType.hashCode()
            return result
        }
    }

    data class Error(val message: String) : ImagePickerResult()
}

@Composable
expect fun rememberImagePicker(): ImagePicker