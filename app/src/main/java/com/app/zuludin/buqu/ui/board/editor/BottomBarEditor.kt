package com.app.zuludin.buqu.ui.board.editor

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.zuludin.buqu.core.compose.MediaFileScanner
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.icons.PhosphorDotsThreeVertical
import com.app.zuludin.buqu.core.icons.PhosphorImage
import com.app.zuludin.buqu.core.icons.PhosphorMicrophone
import com.app.zuludin.buqu.core.icons.PhosphorPlus

@Composable
fun BottomBarEditor(
    onScanText: (String) -> Unit,
    onSpeechToText: () -> Unit,
    onAddNote: () -> Unit,
    onOpenOverflowMenu: () -> Unit,
    @SuppressLint("ModifierParameter") overflowModifier: Modifier
) {
    BottomAppBar(
        actions = {
            MediaFileScanner(
                imageVector = PhosphorAperture,
                isOpenCamera = true,
                onTextSelected = { onScanText(it) }
            )
            MediaFileScanner(
                imageVector = PhosphorImage,
                isOpenCamera = false,
                onTextSelected = { onScanText(it) }
            )
            IconButton(
                onClick = { onSpeechToText() },
                content = { Icon(PhosphorMicrophone, null) }
            )
            IconButton(
                modifier = overflowModifier,
                onClick = { onOpenOverflowMenu() },
                content = { Icon(PhosphorDotsThreeVertical, null) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                content = { Icon(PhosphorPlus, null) },
                onClick = { onAddNote() },
            )
        }
    )
}