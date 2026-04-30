package com.app.zuludin.buqu.ui.board.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.app.zuludin.buqu.core.compose.MediaFileScanner
import com.app.zuludin.buqu.core.compose.SpeechToText
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.icons.PhosphorDotsThreeVertical
import com.app.zuludin.buqu.core.icons.PhosphorImage
import com.app.zuludin.buqu.core.icons.PhosphorPlus
import kotlin.math.roundToInt

@Composable
fun BottomBarEditor(
    onTextResult: (String) -> Unit,
    onAddNote: () -> Unit,
    onSaveImage: (String, String) -> Unit,
    onTidyUp: () -> Unit
) {
    var showOverflowMenu by remember { mutableStateOf(false) }
    var overflowMenuPosition by remember { mutableStateOf(Offset.Zero) }

    BottomAppBar(
        actions = {
            MediaFileScanner(
                imageVector = PhosphorAperture,
                isOpenCamera = true,
                onTextSelected = { onTextResult(it) },
                onSaveImage = onSaveImage
            )
            MediaFileScanner(
                imageVector = PhosphorImage,
                isOpenCamera = false,
                onTextSelected = { onTextResult(it) },
                onSaveImage = onSaveImage
            )
            SpeechToText { onTextResult(it) }
            IconButton(
                modifier = Modifier.onGloballyPositioned {
                    overflowMenuPosition = it.positionOnScreen()
                },
                onClick = { showOverflowMenu = true },
                content = { Icon(PhosphorDotsThreeVertical, null) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                content = { Icon(PhosphorPlus, null) },
                onClick = { onAddNote() },
            )
        }
    )

    if (showOverflowMenu) {
        Popup(
            offset = IntOffset(
                overflowMenuPosition.x.roundToInt() + 80,
                overflowMenuPosition.y.roundToInt() - 180
            ),
            onDismissRequest = { showOverflowMenu = !showOverflowMenu }
        ) {
            Column(modifier = Modifier.widthIn(max = 120.dp)) {
                OverflowMenuItem(
                    title = "Tidy Up Notes",
                    onClick = {
                        onTidyUp()
                        showOverflowMenu = !showOverflowMenu
                    }
                )
                OverflowMenuItem(
                    title = "Settings",
                    onClick = { showOverflowMenu = !showOverflowMenu }
                )
            }
        }
    }
}