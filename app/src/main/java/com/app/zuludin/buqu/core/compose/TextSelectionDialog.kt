package com.app.zuludin.buqu.core.compose

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.magnifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.zuludin.buqu.core.utils.rotateImage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text as VisionText
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.math.min

data class SelectedWord(
    val blockIndex: Int,
    val lineIndex: Int,
    val elementIndex: Int,
    val text: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextSelectionDialog(
    bitmap: Bitmap,
    onDismiss: () -> Unit,
    onTextSelected: (String) -> Unit
) {
    var currentBitmap by remember { mutableStateOf(bitmap) }
    var detectedText by remember { mutableStateOf<VisionText?>(null) }
    var selectedWords by remember { mutableStateOf(setOf<SelectedWord>()) }
    var isLoading by remember { mutableStateOf(true) }
    var magnifierCenter by remember { mutableStateOf(Offset.Unspecified) }

    LaunchedEffect(currentBitmap) {
        isLoading = true
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(currentBitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener {
                detectedText = it
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = currentBitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .magnifier(
                                sourceCenter = { magnifierCenter },
                                magnifierCenter = { magnifierCenter - Offset(0f, 150f) },
                                size = DpSize(120.dp, 120.dp),
                                cornerRadius = 60.dp,
                                elevation = 8.dp
                            ),
                        contentScale = ContentScale.Fit
                    )

                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(detectedText, currentBitmap) {
                                detectTapGestures(
                                    onPress = { offset ->
                                        magnifierCenter = offset
                                        tryAwaitRelease()
                                        magnifierCenter = Offset.Unspecified
                                    },
                                    onTap = { offset ->
                                        val fullWidth = size.width.toFloat()
                                        val fullHeight = size.height.toFloat()
                                        val bitmapWidth = currentBitmap.width.toFloat()
                                        val bitmapHeight = currentBitmap.height.toFloat()
                                        val scale = min(fullWidth / bitmapWidth, fullHeight / bitmapHeight)
                                        val offsetX = (fullWidth - (bitmapWidth * scale)) / 2
                                        val offsetY = (fullHeight - (bitmapHeight * scale)) / 2

                                        detectedText?.textBlocks?.forEachIndexed { bIdx, block ->
                                            block.lines.forEachIndexed { lIdx, line ->
                                                line.elements.forEachIndexed { eIdx, element ->
                                                    val rect = element.boundingBox?.let {
                                                        Rect(
                                                            left = it.left * scale + offsetX,
                                                            top = it.top * scale + offsetY,
                                                            right = it.right * scale + offsetX,
                                                            bottom = it.bottom * scale + offsetY
                                                        )
                                                    }
                                                    if (rect?.contains(offset) == true) {
                                                        val word = SelectedWord(bIdx, lIdx, eIdx, element.text)
                                                        selectedWords = if (selectedWords.contains(word)) {
                                                            selectedWords - word
                                                        } else {
                                                            selectedWords + word
                                                        }
                                                        return@detectTapGestures
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                            .pointerInput(detectedText, currentBitmap) {
                                detectDragGestures(
                                    onDragStart = { offset -> magnifierCenter = offset },
                                    onDragEnd = { magnifierCenter = Offset.Unspecified },
                                    onDragCancel = { magnifierCenter = Offset.Unspecified },
                                    onDrag = { change, _ ->
                                        magnifierCenter = change.position
                                        val fullWidth = size.width.toFloat()
                                        val fullHeight = size.height.toFloat()
                                        val bitmapWidth = currentBitmap.width.toFloat()
                                        val bitmapHeight = currentBitmap.height.toFloat()
                                        val scale = min(fullWidth / bitmapWidth, fullHeight / bitmapHeight)
                                        val offsetX = (fullWidth - (bitmapWidth * scale)) / 2
                                        val offsetY = (fullHeight - (bitmapHeight * scale)) / 2

                                        detectedText?.textBlocks?.forEachIndexed { bIdx, block ->
                                            block.lines.forEachIndexed { lIdx, line ->
                                                line.elements.forEachIndexed { eIdx, element ->
                                                    val rect = element.boundingBox?.let {
                                                        Rect(
                                                            left = it.left * scale + offsetX,
                                                            top = it.top * scale + offsetY,
                                                            right = it.right * scale + offsetX,
                                                            bottom = it.bottom * scale + offsetY
                                                        )
                                                    }
                                                    if (rect?.contains(change.position) == true) {
                                                        selectedWords = selectedWords + SelectedWord(bIdx, lIdx, eIdx, element.text)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                    ) {
                        val fullWidth = size.width
                        val fullHeight = size.height
                        val bitmapWidth = currentBitmap.width.toFloat()
                        val bitmapHeight = currentBitmap.height.toFloat()
                        val scale = min(fullWidth / bitmapWidth, fullHeight / bitmapHeight)
                        val offsetX = (fullWidth - (bitmapWidth * scale)) / 2
                        val offsetY = (fullHeight - (bitmapHeight * scale)) / 2

                        detectedText?.textBlocks?.forEachIndexed { bIdx, block ->
                            block.lines.forEachIndexed { lIdx, line ->
                                line.elements.forEachIndexed { eIdx, element ->
                                    element.boundingBox?.let { box ->
                                        val rect = Rect(
                                            left = box.left * scale + offsetX,
                                            top = box.top * scale + offsetY,
                                            right = box.right * scale + offsetX,
                                            bottom = box.bottom * scale + offsetY
                                        )
                                        
                                        val isSelected = selectedWords.any { it.blockIndex == bIdx && it.lineIndex == lIdx && it.elementIndex == eIdx }
                                        val color = if (isSelected) {
                                            Color.Cyan.copy(alpha = 0.5f)
                                        } else {
                                            Color.White.copy(alpha = 0.2f)
                                        }
                                        
                                        drawRect(
                                            color = color,
                                            topLeft = rect.topLeft,
                                            size = rect.size
                                        )
                                        
                                        if (isSelected) {
                                            drawRect(
                                                color = Color.Cyan,
                                                topLeft = rect.topLeft,
                                                size = rect.size,
                                                style = Stroke(width = 1.dp.toPx())
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (magnifierCenter != Offset.Unspecified) {
                            drawCircle(
                                color = Color.Cyan,
                                radius = 4.dp.toPx(),
                                center = magnifierCenter,
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }
                    }

                    IconButton(
                        onClick = { currentBitmap = currentBitmap.rotateImage(90f) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(Color.Black.copy(alpha = 0.5f), MaterialTheme.shapes.small)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Rotate", tint = Color.White)
                    }

                    if (isLoading) CircularProgressIndicator(color = Color.White)
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val statusText = when {
                            isLoading -> "Detecting text..."
                            selectedWords.isEmpty() -> "Tap or swipe over words to select"
                            else -> "${selectedWords.size} words selected"
                        }
                        
                        Text(text = statusText, style = MaterialTheme.typography.bodyMedium)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = onDismiss) { Text("Cancel") }
                            Box(modifier = Modifier.size(8.dp))
                            Button(
                                onClick = {
                                    val result = selectedWords
                                        .sortedWith(compareBy({ it.blockIndex }, { it.lineIndex }, { it.elementIndex }))
                                        .joinToString(" ") { it.text }
                                    onTextSelected(result)
                                },
                                enabled = selectedWords.isNotEmpty()
                            ) { Text("Confirm Selection") }
                        }
                    }
                }
            }
        }
    }
}
