package com.goiaba.shared.util

import androidx.compose.runtime.Composable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ImagePicker {
    actual suspend fun pickImage(): ImagePickerResult? {
        TODO("Not yet implemented")
    }

    actual suspend fun saveImageToGallery(imageData: ByteArray, fileName: String): Boolean {
        TODO("Not yet implemented")
    }
}

@Composable
actual fun rememberImagePicker(): ImagePicker {
    TODO("Not yet implemented")
}