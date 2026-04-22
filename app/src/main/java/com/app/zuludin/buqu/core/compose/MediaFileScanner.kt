package com.app.zuludin.buqu.core.compose

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.app.zuludin.buqu.BuildConfig
import com.app.zuludin.buqu.core.utils.createImageFile
import com.app.zuludin.buqu.core.utils.fixImageRotation
import java.io.File
import java.io.FileOutputStream
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaFileScanner(
    imageVector: ImageVector,
    isOpenCamera: Boolean,
    onTextSelected: (String) -> Unit,
    onSaveImage: (String) -> Unit
) {
    val context = LocalContext.current

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showTextSelection by remember { mutableStateOf(false) }
    var showImageResult by remember { mutableStateOf(false) }
    var galleryUri by remember { mutableStateOf<Uri?>(null) }

    val file = remember { context.createImageFile() }
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
    )
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                capturedBitmap = context.fixImageRotation(uri)
                showImageResult = true
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { success ->
            if (success != null) {
                capturedBitmap = context.fixImageRotation(success)
                galleryUri = success
                showImageResult = true
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    if (showTextSelection && capturedBitmap != null) {
        TextSelectionDialog(
            bitmap = capturedBitmap!!,
            onDismiss = { showTextSelection = !showTextSelection },
            onTextSelected = { selectedText ->
                onTextSelected(selectedText)
                showTextSelection = !showTextSelection
            }
        )
    }

    if (showImageResult && capturedBitmap != null) {
        AlertDialog(
            onDismissRequest = { showImageResult = !showImageResult },
            content = {
                Column {
                    Image(capturedBitmap!!.asImageBitmap(), null)
                    Row {
                        TextButton(
                            onClick = {
                                if (galleryUri != null) {
                                    val path = saveImageToInternalStorage(context, galleryUri!!)
                                    if (path != null) {
                                        onSaveImage(path)
                                    }
                                } else {
                                    val path = saveImageToInternalStorage(context, uri)
                                    if (path != null) {
                                        onSaveImage(path)
                                    }
                                }
                                showImageResult = !showImageResult
                            },
                            content = { Text("Save") }
                        )
                        TextButton(
                            onClick = {
                                showTextSelection = true
                                showImageResult = !showImageResult
                            },
                            content = { Text("Scan Text") }
                        )
                    }
                }
            }
        )
    }

    IconButton(
        onClick = {
            if (isOpenCamera) {
                val permissionCheckResult = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                )
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            } else {
                galleryLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        },
        content = { Icon(imageVector, null) }
    )
}

private fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        // Use a unique name to avoid collisions
        val fileName = "img_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4 * 1024) // 4KB buffer
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush() // Ensure all bytes are written
            }
        }

        // Final check: if file is 0 bytes, something went wrong
        if (file.exists() && file.length() > 0) {
            file.absolutePath
        } else {
            file.delete() // Clean up empty file
            null
        }
    } catch (e: Exception) {
        Log.e("SaveImage", "Error saving image", e)
        null
    }
}