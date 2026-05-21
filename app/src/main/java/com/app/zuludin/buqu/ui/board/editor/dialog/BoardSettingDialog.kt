package com.app.zuludin.buqu.ui.board.editor.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.core.icons.PhosphorGridFour
import com.app.zuludin.buqu.core.icons.PhosphorWall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardSettingDialog(onDismiss: () -> Unit, onTidyUp: () -> Unit, onToggleGrid: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Board Settings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(
                icon = PhosphorWall,
                title = "Tidy Up Notes",
                onClick = onTidyUp,
            )
            SettingItem(
                icon = PhosphorGridFour,
                title = "Toggle Grid",
                onClick = onToggleGrid,
            )
        }
    }
}

@Composable
private fun SettingItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick
    ) {
        Column {
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                Icon(icon, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title)
            }
            Divider()
        }
    }
}