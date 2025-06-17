package com.goiaba.shared.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class ImagePicker(private val context: Context) {
    
    private var onImagePicked: ((ImagePickerResult?) -> Unit)? = null
    private var launchPicker by mutableStateOf(false)
    
    fun setOnImagePickedCallback(callback: (ImagePickerResult?) -> Unit) {
        onImagePicked = callback
    }
    
    fun triggerImagePicker() {
        launchPicker = true
    }
    
    fun shouldLaunchImagePicker(): Boolean = launchPicker
    
    suspend fun pickImageInternal(): ImagePickerResult? = suspendCoroutine { continuation ->
        onImagePicked = { result ->
            continuation.resume(result)
        }
        launchPicker = true
    }
    
    actual suspend fun pickImage(): ImagePickerResult? {
        return pickImageInternal()
    }
    
    actual suspend fun saveImageToGallery(imageData: ByteArray, fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/GoiabaKMP")
                }
                
                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                
                uri?.let { imageUri ->
                    context.contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                    }
                    true
                } ?: false
            } catch (e: Exception) {
                false
            }
        }
    }
    
    fun handleImageResult(uri: Uri?) {
        if (uri != null) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val imageData = inputStream?.readBytes()
                inputStream?.close()
                
                if (imageData != null) {
                    val fileName = "image_${System.currentTimeMillis()}.jpg"
                    onImagePicked?.invoke(
                        ImagePickerResult.Success(
                            imageData = imageData,
                            fileName = fileName
                        )
                    )
                } else {
                    onImagePicked?.invoke(ImagePickerResult.Error("Failed to read image data"))
                }
            } catch (e: Exception) {
                onImagePicked?.invoke(ImagePickerResult.Error("Error processing image: ${e.message}"))
            }
        } else {
            onImagePicked?.invoke(null) // User cancelled
        }
        launchPicker = false
    }
}

@Composable
actual fun rememberImagePicker(): ImagePicker {
    val context = LocalContext.current
    val imagePicker = remember { ImagePicker(context) }
    
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        imagePicker.handleImageResult(uri)
    }
    
    // Monitor the picker state and launch when needed
    val shouldLaunch = imagePicker.shouldLaunchImagePicker()
    
    LaunchedEffect(shouldLaunch) {
        if (shouldLaunch) {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    }
    
    return imagePicker
}