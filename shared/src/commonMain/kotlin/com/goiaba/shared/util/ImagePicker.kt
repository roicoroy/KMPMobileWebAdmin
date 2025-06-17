package com.goiaba.shared.util

import androidx.compose.runtime.Composable

expect class ImagePicker {
    suspend fun pickImage(): ImagePickerResult?
    suspend fun saveImageToGallery(imageData: ByteArray, fileName: String): Boolean
}

sealed class ImagePickerResult {
    data class Success(
        val imageData: ByteArray,
        val fileName: String,
        val mimeType: String = "image/jpeg"
    ) : ImagePickerResult()
    
    data class Error(val message: String) : ImagePickerResult()
}

@Composable
expect fun rememberImagePicker(): ImagePicker