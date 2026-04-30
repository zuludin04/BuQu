package com.app.zuludin.buqu.core.compose

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.app.zuludin.buqu.core.utils.ImagePicker
import com.app.zuludin.buqu.core.utils.fixImageRotation
import com.app.zuludin.buqu.core.utils.getImageBitmap
import java.io.File
import java.io.FileOutputStream

@Composable
fun MediaFileScanner(
    imageVector: ImageVector,
    isOpenCamera: Boolean,
    onTextSelected: (String) -> Unit,
    onSaveImage: (String, String) -> Unit
) {
    val context = LocalContext.current

    var showTextSelection by remember { mutableStateOf(false) }
    var showImageResult by remember { mutableStateOf(false) }
    var uri by remember { mutableStateOf<Uri?>(null) }

    ImagePicker(
        child = {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        },
        isOpenCamera = isOpenCamera,
        onImageResult = {
            uri = it
            showImageResult = true
        })

    if (showTextSelection && uri != null) {
        TextSelectionDialog(
            bitmap = context.fixImageRotation(uri!!)!!,
            onDismiss = { showTextSelection = !showTextSelection },
            onTextSelected = { selectedText ->
                onTextSelected(selectedText)
                showTextSelection = !showTextSelection
            })
    }

    if (showImageResult && uri != null) {
        ImagePickDialog(
            uri = uri,
            onDismiss = { showImageResult = !showImageResult },
            onSaveImage = { selectedColor, uri ->
                val path = saveImageToInternalStorage(context, uri)
                if (path != null) {
                    onSaveImage(path, selectedColor)
                }
                showImageResult = !showImageResult
            },
            onScanText = {
                showTextSelection = true
                showImageResult = !showImageResult
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickDialog(
    uri: Uri?,
    onDismiss: () -> Unit,
    onSaveImage: (String, Uri) -> Unit,
    onScanText: () -> Unit,
    showScanText: Boolean = true,
    color: String = "E1F5FE"
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val colors = listOf(
        "E1F5FE", "FFF9C4", "F1F8E9", "FFEBEE", "F3E5F5", "EFEBE9"
    )
    var selectedColor by remember { mutableStateOf(color) }
    var imageUri by remember { mutableStateOf(uri) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        content = {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Save Image",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ImagePicker(
                        child = {
                            Image(
                                bitmap = context.getImageBitmap(imageUri!!),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        },
                        isOpenCamera = false, onImageResult = { imageUri = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Select Background Color",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        colors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color("#${color}".toColorInt()))
                                    .border(
                                        width = if (selectedColor == color) 3.dp else 1.dp,
                                        color = if (selectedColor == color) MaterialTheme.colorScheme.primary
                                        else Color.LightGray.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = color })
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        if (showScanText) {
                            TextButton(onClick = onScanText) {
                                Text("Scan Text")
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onSaveImage(selectedColor, imageUri!!) },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        })
}

fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val fileName = "img_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }
        }

        if (file.exists() && file.length() > 0) {
            file.absolutePath
        } else {
            file.delete()
            null
        }
    } catch (e: Exception) {
        Log.e("SaveImage", "Error saving image", e)
        null
    }
}
