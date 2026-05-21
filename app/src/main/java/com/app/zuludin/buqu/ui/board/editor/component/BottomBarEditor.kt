package com.app.zuludin.buqu.ui.board.editor.component

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.app.zuludin.buqu.core.compose.MediaFileScanner
import com.app.zuludin.buqu.core.compose.SpeechToText
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.icons.PhosphorDotsThreeVertical
import com.app.zuludin.buqu.core.icons.PhosphorImage
import com.app.zuludin.buqu.core.icons.PhosphorPlus
import com.app.zuludin.buqu.core.icons.PhosphorTrash

@Composable
fun BottomBarEditor(
    onTextResult: (String) -> Unit,
    onAddNote: () -> Unit,
    onSaveImage: (String, String) -> Unit,
    showDelete: Boolean,
    onDeleteBoard: () -> Unit,
    onBoardSettings: () -> Unit
) {
    BottomAppBar(
        actions = {
            if (showDelete)
                IconButton(
                    onClick = onDeleteBoard,
                    content = { Icon(PhosphorTrash, null) }
                )
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
                onClick = { onBoardSettings() },
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
}