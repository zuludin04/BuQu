package com.app.zuludin.buqu.ui.board.editor.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.app.zuludin.buqu.domain.models.Camera
import com.app.zuludin.buqu.ui.board.editor.BoardDialogState

@Composable
fun BoardInfiniteCanvas(
    modifier: Modifier = Modifier,
    camera: Camera,
    showGrid: Boolean,
    onCameraChange: (Camera) -> Unit,
    onGetBoardSize: (IntSize) -> Unit,
    openDialog: (BoardDialogState) -> Unit,
    onCanvasTap: (Offset) -> Unit,
    backgroundType: BackgroundType,
    content: @Composable BoxScope.(Camera) -> Unit
) {
    val state = rememberTransformableState { zoomChange, panChange, _ ->
        val newZoom = (camera.zoom * zoomChange).coerceIn(0.5f, 3f)
        val newOffset = camera.offset + panChange
        onCameraChange(camera.copy(zoom = newZoom, offset = newOffset))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { onGetBoardSize(it) }
            .transformable(state)
    ) {
        if (showGrid) BoardBackground(backgroundType, camera)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = camera.zoom,
                    scaleY = camera.zoom,
                    translationX = camera.offset.x,
                    translationY = camera.offset.y,
                )
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onCanvasTap(it)
                    })
                }
        ) {
            content(camera)
        }

        BoardTools(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .testTag("BoardTools"),
            onZoomIn = {
                val newZoom = (camera.zoom - 0.1f).coerceAtLeast(0.5f)
                onCameraChange(camera.copy(zoom = newZoom))
            },
            onZoomOut = {
                val newZoom = (camera.zoom + 0.1f).coerceAtMost(3f)
                onCameraChange(camera.copy(zoom = newZoom))
            },
            onResetZoom = { onCameraChange(Camera()) },
            scale = camera.zoom,
            onImportQuotes = { openDialog(BoardDialogState.ImportQuotes) },
            onImportBooks = { openDialog(BoardDialogState.ImportBooks) },
        )
    }
}