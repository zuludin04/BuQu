package com.app.zuludin.buqu.core.compose

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.utils.ImagePicker
import com.app.zuludin.buqu.core.utils.fixImageRotation

@Composable
fun TextScanner(
    onTextSelected: (String) -> Unit,
    imageVector: ImageVector = PhosphorAperture
) {
    val context = LocalContext.current
    var showTextSelection by remember { mutableStateOf(false) }
    var uri by remember { mutableStateOf<Uri?>(null) }

    ImagePicker(
        child = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.shapes.small)
                        .padding(4.dp)
                )
            }
        },
        isOpenCamera = true,
        onImageResult = {
            uri = it
            showTextSelection = true
        })

    if (showTextSelection && uri != null) {
        val bitmap = context.fixImageRotation(uri!!)
        if (bitmap != null) {
            TextSelectionDialog(
                bitmap = bitmap,
                onDismiss = { showTextSelection = !showTextSelection },
                onTextSelected = { selectedText ->
                    onTextSelected(selectedText)
                    showTextSelection = !showTextSelection
                })
        }
    }
}
