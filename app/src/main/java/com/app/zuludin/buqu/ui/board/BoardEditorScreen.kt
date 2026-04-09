package com.app.zuludin.buqu.ui.board

import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.neumorphicShadow
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorPlus
import com.app.zuludin.buqu.domain.models.Note
import com.app.zuludin.buqu.domain.models.Yarn
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun BoardEditorScreen(
    topAppBarTitle: String,
    onBack: () -> Unit,
) {
    fun randomWord(): String {
        val charPool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val randomNum = Random.nextInt(0, charPool.length)
        return CharArray(randomNum) { charPool.random() }.concatToString()
    }

    val cards = remember { mutableStateListOf<Note>() }
    val yarns = remember { mutableStateListOf<Yarn>() }
    var showSheet by remember { mutableStateOf(false) }
    var sourceTheory by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            BuQuToolbar(
                title = topAppBarTitle,
                backButton = {
                    IconButton(onClick = onBack) {
                        Icon(PhosphorArrowLeft, null)
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {},
                floatingActionButton = {
                    FloatingActionButton(
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        content = { Icon(PhosphorPlus, null) },
                        onClick = {
                            cards.add(
                                Note(
                                    id = UUID.randomUUID().toString(),
                                    content = randomWord(),
                                    xPos = 0f,
                                    yPos = 0f,
                                    size = IntSize.Zero,
                                    color = Color.Cyan
                                )
                            )
                        },
                    )
                }
            )
        }
    ) { paddingValues ->
        BoardEditor(
            modifier = Modifier.padding(paddingValues),
            cards = cards,
            yarns = yarns,
            onDragNote = { note, index ->
                cards[index] = note
            },
            onSelectTheory = {
                sourceTheory = it
                showSheet = true
            }
        )
    }

    if (showSheet) {
        TheoryBottomDialog(
            source = sourceTheory!!,
            theories = cards,
            onDismiss = {
                if (it != null) {
                    yarns.add(it)
                }
                showSheet = false
            }
        )
    }
}

@Composable
fun BoardEditor(
    modifier: Modifier = Modifier,
    cards: MutableList<Note>,
    yarns: MutableList<Yarn>,
    onDragNote: (Note, Int) -> Unit,
    onSelectTheory: (Note) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var popupOffset by remember { mutableStateOf(Offset.Zero) }
    var selectedTheory by remember { mutableStateOf<Note?>(null) }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .transformable(state = state)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale != 1f || offset != Offset.Zero) {
                            scale = 1f
                            offset = Offset.Zero
                        }
                    }
                )
            }
    ) {
        yarns.forEach {
            DraggableYarn(
                initialRope = Offset(it.xSource, it.ySource),
                targetRope = Offset(it.xTarget, it.yTarget),
                targetTheory = it.targetSize,
                startTheory = it.sourceSize,
            )
        }

        cards.forEachIndexed { index, n ->
            DraggableCard(
                note = n,
                currentOffset = Offset(n.xPos, n.yPos),
                onPositionChanged = { note ->
                    if (yarns.isNotEmpty()) {
                        val sourceRope = yarns.filter { it.sourceNoteId == note.id }
                        if (sourceRope.isNotEmpty()) {
                            sourceRope.forEach { rope ->
                                val r = rope.copy(xSource = note.xPos, ySource = note.yPos)
                                val selected = yarns.first { it.id == rope.id }
                                val index = yarns.indexOf(selected)
                                yarns[index] = r
                            }
                        }

                        val targetRope = yarns.filter { it.targetNoteId == note.id }
                        if (targetRope.isNotEmpty()) {
                            targetRope.forEach { rope ->
                                val r = rope.copy(xTarget = note.xPos, yTarget = note.yPos)
                                val selected = yarns.first { it.id == rope.id }
                                val index = yarns.indexOf(selected)
                                yarns[index] = r
                            }
                        }
                    }

                    onDragNote(note, index)
                },
                onGetSize = {
                    val note = n.copy(size = it)
                    cards[index] = note
                },
                onSelect = { t, off ->
                    selectedTheory = t
                    popupOffset = off
                    showMenu = true
                },
            )
        }

        if (showMenu) {
            Popup(
                offset = IntOffset(popupOffset.x.roundToInt(), popupOffset.y.roundToInt()),
                onDismissRequest = { showMenu = false },
                content = {
                    Surface(
                        modifier = Modifier.size(120.dp, 80.dp),
                        color = Color.White,
                        tonalElevation = 8.dp,
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            modifier = Modifier.clickable {
                                onSelectTheory(selectedTheory!!)
                                showMenu = false
                            }
                        ) {
                            Text("Connect Theory", Modifier.align(Alignment.Center))
                        }
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryBottomDialog(
    source: Note,
    theories: List<Note>,
    onDismiss: (rope: Yarn?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onDismiss(null) },
        sheetState = sheetState,
    ) {
        Column {
            theories.filter { it.id != source.id }.forEach { t ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(t.content)
                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                val rope = connectTheoryYarn(source, t)
                                onDismiss(rope)
                            }
                        }
                    }) {
                        Text("Connect")
                    }
                }
            }
        }
    }
}

fun connectTheoryYarn(source: Note, target: Note): Yarn {
    val rope = Yarn(
        id = UUID.randomUUID().toString(),
        xSource = source.xPos,
        ySource = source.yPos,
        xTarget = target.xPos,
        yTarget = target.yPos,
        sourceNoteId = source.id,
        targetNoteId = target.id,
        sourceSize = source.size,
        targetSize = target.size
    )
    return rope
}

@Composable
fun DraggableYarn(
    initialRope: Offset,
    targetRope: Offset,
    startTheory: IntSize,
    targetTheory: IntSize
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val startCenterOffset =
            Offset(
                startTheory.width.pxToDp().toPx() / 2,
                startTheory.height.pxToDp().toPx() / 2
            )
        val targetCenterOffset =
            Offset(
                targetTheory.width.pxToDp().toPx() / 2,
                targetTheory.height.pxToDp().toPx() / 2
            )

        drawLine(
            color = Color(0xFF7D5260),
            start = initialRope + startCenterOffset,
            end = targetRope + targetCenterOffset,
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun DraggableCard(
    note: Note,
    currentOffset: Offset,
    onPositionChanged: (Note) -> Unit,
    onSelect: (Note, Offset) -> Unit,
    onGetSize: (IntSize) -> Unit,
    isDraggable: Boolean = true
) {
    var isDragging by remember { mutableStateOf(false) }
    var newOffset by remember { mutableStateOf(currentOffset) }
    var newTheory by remember { mutableStateOf<Note?>(null) }

    val updatedOnPositionChanged by rememberUpdatedState(onPositionChanged)

    Box(
        modifier = Modifier
            .onSizeChanged {
                onGetSize(it)
            }
            .offset {
                IntOffset(
                    currentOffset.x.roundToInt(),
                    currentOffset.y.roundToInt()
                )
            }
            .graphicsLayer {
                if (isDraggable) {
                    scaleX = if (isDragging) 1.05f else 1f
                    scaleY = if (isDragging) 1.05f else 1f
                }
            }
            .neumorphicShadow(backgroundColor = note.color)
            .padding(16.dp)
            .pointerInput(note.id) {
                detectTapGestures(
                    onTap = { tapOffset ->
                        val absoluteTapPos = Offset(
                            newOffset.x + tapOffset.x,
                            newOffset.y + tapOffset.y
                        )
                        newTheory = note.copy(xPos = newOffset.x, yPos = newOffset.y)
                        onSelect(newTheory!!, absoluteTapPos)
                        isDragging = false
                    }
                )
            }
            .pointerInput(note.id) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        newOffset += dragAmount
                        val n = note.copy(xPos = newOffset.x, yPos = newOffset.y)
                        updatedOnPositionChanged(n)
                    }
                )
            }
    ) {
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = note.content,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

fun Int.pxToDp(): Dp = (this / Resources.getSystem().displayMetrics.density).dp