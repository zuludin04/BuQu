package com.app.zuludin.buqu.core.compose

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.app.zuludin.buqu.BuildConfig
import com.app.zuludin.buqu.core.utils.createImageFile
import com.app.zuludin.buqu.core.utils.fixImageRotation
import java.util.Objects

@Composable
fun MediaFileScanner(
    imageVector: ImageVector,
    isOpenCamera: Boolean,
    onTextSelected: (String) -> Unit,
) {
    val context = LocalContext.current

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showTextSelection by remember { mutableStateOf(false) }

    val file = remember { context.createImageFile() }
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                capturedBitmap = context.fixImageRotation(uri)
                showTextSelection = true
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { success ->
            if (success != null) {
                capturedBitmap = context.fixImageRotation(success)
                showTextSelection = true
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