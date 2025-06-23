package com.goiaba.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import org.koin.core.component.getScopeName
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.writeToFile
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class ImagePicker {

    actual suspend fun pickImage(): ImagePickerResult? {
        // iOS implementation would require UIImagePickerController
        // For now, return a placeholder implementation
        return suspendCoroutine { continuation ->
            // This would need to be implemented with UIImagePickerController
            // or PHPickerViewController for iOS 14+
            continuation.resume(null)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun saveImageToGallery(imageData: ByteArray, fileName: String): Boolean {
        return try {
//            val nsData: NSData = imageData.usePinned { pinned ->
////                NSData.create(bytes = pinned.addressOf(0), length = imageData.size.toULong())
//            }

//            val image = UIImage.imageWithData(nsData)
//            if (image != null) {
//                UIImageWriteToSavedPhotosAlbum(image, null, null, null)
//                true
//            } else {
//                false
//            }
            return true
        } catch (e: Exception) {
            false
        }
    }
}

@Composable
actual fun rememberImagePicker(): ImagePicker {
    return remember { ImagePicker() }
}