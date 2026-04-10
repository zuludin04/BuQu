package com.app.zuludin.buqu.ui.board

import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.app.zuludin.buqu.core.compose.BuQuToolbar
import com.app.zuludin.buqu.core.compose.neumorphicShadow
import com.app.zuludin.buqu.core.icons.PhosphorAperture
import com.app.zuludin.buqu.core.icons.PhosphorArrowLeft
import com.app.zuludin.buqu.core.icons.PhosphorDotsThreeVertical
import com.app.zuludin.buqu.core.icons.PhosphorImage
import com.app.zuludin.buqu.core.icons.PhosphorLineSegments
import com.app.zuludin.buqu.core.icons.PhosphorLinkBreak
import com.app.zuludin.buqu.core.icons.PhosphorMagnifyingGlass
import com.app.zuludin.buqu.core.icons.PhosphorMicrophone
import com.app.zuludin.buqu.core.icons.PhosphorPlus
import com.app.zuludin.buqu.core.icons.PhosphorSelectionAll
import com.app.zuludin.buqu.core.icons.PhosphorXCircle
import com.app.zuludin.buqu.domain.models.Note
import com.app.zuludin.buqu.domain.models.Yarn
import java.util.UUID
import kotlin.math.roundToInt

@Composable
fun BoardEditorScreen(
    topAppBarTitle: String,
    onBack: () -> Unit,
) {
    val cards = remember { mutableStateListOf<Note>() }
    val yarns = remember { mutableStateListOf<Yarn>() }
    var showConnectionSheet by remember { mutableStateOf(false) }
    var showAddNoteSheet by remember { mutableStateOf(false) }
    var showOverflowMenu by remember { mutableStateOf(false) }
    var sourceTheory by remember { mutableStateOf<Note?>(null) }
    var overflowMenuPosition by remember { mutableStateOf(Offset.Zero) }
    var zoomInfo by remember { mutableStateOf("1") }

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
                actions = {
                    IconButton(
                        onClick = {},
                        content = { Icon(PhosphorAperture, null) }
                    )
                    IconButton(
                        onClick = {},
                        content = { Icon(PhosphorImage, null) }
                    )
                    IconButton(
                        onClick = {},
                        content = { Icon(PhosphorMicrophone, null) }
                    )
                    IconButton(
                        modifier = Modifier.onGloballyPositioned {
                            overflowMenuPosition = it.positionOnScreen()
                        },
                        onClick = {
                            showOverflowMenu = true
                        },
                        content = { Icon(PhosphorDotsThreeVertical, null) }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        content = { Icon(PhosphorPlus, null) },
                        onClick = { showAddNoteSheet = true },
                    )
                }
            )
        }
    ) { paddingValues ->
        Box {
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(
                    onClick = {},
                    content = { Icon(PhosphorLineSegments, null) }
                )
                IconButton(
                    onClick = {},
                    content = { Icon(PhosphorSelectionAll, null) }
                )
            }

            Row {
                Text(zoomInfo)
                IconButton(
                    onClick = {},
                    content = { Icon(PhosphorMagnifyingGlass, null) }
                )
            }

            BoardEditor(
                modifier = Modifier.padding(paddingValues),
                cards = cards,
                yarns = yarns,
                onDragNote = { note, index ->
                    cards[index] = note
                },
                onSelectTheory = {
                    sourceTheory = it
                    showConnectionSheet = true
                }
            )
        }
    }

    if (showConnectionSheet) {
        TheoryBottomDialog(
            source = sourceTheory!!,
            theories = cards,
            onDismiss = {
                showConnectionSheet = false
                if (it != null) {
                    yarns.add(it)
                }
            }
        )
    }

    if (showAddNoteSheet) {
        NoteInputDialog(
            onDismiss = { showAddNoteSheet = false },
            onConfirm = { content, color ->
                cards.add(
                    Note(
                        id = UUID.randomUUID().toString(),
                        content = content,
                        xPos = 100f,
                        yPos = 100f,
                        size = IntSize.Zero,
                        color = color
                    )
                )
                showAddNoteSheet = false
            }
        )
    }

    if (showOverflowMenu) {
        Popup(
            offset = IntOffset(
                overflowMenuPosition.x.roundToInt() + 80,
                overflowMenuPosition.y.roundToInt() - 180
            ),
            onDismissRequest = { showOverflowMenu = false }
        ) {
            Column(modifier = Modifier.widthIn(max = 120.dp)) {
                Surface(
                    color = Color.White,
                    tonalElevation = 8.dp,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier.clickable {
                            showOverflowMenu = false
                        }
                    ) {
                        Text(
                            "Auto Layout",
                            Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        )
                    }
                }
                Surface(
                    color = Color.White,
                    tonalElevation = 8.dp,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier.clickable {
                            showOverflowMenu = false
                        }
                    ) {
                        Text(
                            "Settings",
                            Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Color) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val colors = listOf(
        Color(0xFFE1F5FE), Color(0xFFFFF9C4), Color(0xFFF1F8E9),
        Color(0xFFFFEBEE), Color(0xFFF3E5F5), Color(0xFFEFEBE9)
    )
    var selectedColor by remember { mutableStateOf(colors[0]) }
    val sheetState = rememberModalBottomSheetState()

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
                "Add New Card",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("What's on your mind?") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                minLines = 3,
                maxLines = 5,
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = { text = "" }) {
                            Icon(PhosphorXCircle, null)
                        }
                    }
                }
            )

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InputHelperChip(
                    label = "Voice",
                    icon = PhosphorMicrophone,
                    onClick = { }
                )

                InputHelperChip(
                    label = "Scan",
                    icon = PhosphorAperture,
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Select Color", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColor == color) 3.dp else 1.dp,
                                color = if (selectedColor == color)
                                    MaterialTheme.colorScheme.primary
                                else Color.LightGray.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .clickable { selectedColor = color }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { if (text.isNotBlank()) onConfirm(text, selectedColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = text.isNotBlank()
            ) {
                Text("Create Card", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun InputHelperChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
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
    onDismiss: (Yarn?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val potentialTargets = theories.filter { it.id != source.id }

    ModalBottomSheet(
        onDismissRequest = { onDismiss(null) },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Connect to...",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Linking from: \"${source.content.take(20)}${if (source.content.length > 20) "..." else ""}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (potentialTargets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No other cards to connect to.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(potentialTargets) { target ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(target.color.copy(alpha = 0.3f))
                                .border(1.dp, target.color, RoundedCornerShape(16.dp))
                                .clickable {
                                    val yarn = Yarn(
                                        id = UUID.randomUUID().toString(),
                                        sourceNoteId = source.id,
                                        targetNoteId = target.id,
                                        xSource = source.xPos,
                                        ySource = source.yPos,
                                        xTarget = target.xPos,
                                        yTarget = target.yPos,
                                        sourceSize = source.size,
                                        targetSize = target.size
                                    )
                                    onDismiss(yarn)
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(target.color)
                                    .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = target.content,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                imageVector = PhosphorLinkBreak,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
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