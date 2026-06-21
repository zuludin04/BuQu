package com.app.zuludin.buqu.core.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import com.app.zuludin.buqu.ui.board.editor.BoardDialogState
import com.app.zuludin.buqu.ui.board.editor.BoardEditorState
import com.app.zuludin.buqu.ui.board.editor.DragHandler
import com.app.zuludin.buqu.ui.board.editor.SelectedIndicator
import java.util.UUID
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

class BoardEngine {
    fun drag(
        note: NoteCard,
        worldPos: Offset,
        state: BoardEditorState
    ): BoardEditorState {
        val notes = state.notes.filter { it.status == "active" }
        val ropes = state.ropes.filter { it.status == "active" }

        val ns = notes.map { n ->
            if (n.noteId == note.noteId) {
                n.copy(posX = n.posX + worldPos.x, posY = n.posY + worldPos.y)
            } else {
                n
            }
        }
        val n = ns.first { it.noteId == note.noteId }
        val position = Offset(n.posX, n.posY)
        val rs = updateRopePosition(note.noteId, ropes, position)

        return state.copy(
            notes = ns,
            ropes = rs,
            previewRope = null,
            selectedNoteIds = emptyList(),
            noteHighlightId = null,
        )
    }

    private fun updateRopePosition(
        noteId: String,
        ropes: List<Rope>,
        position: Offset
    ): List<Rope> {
        return ropes.map { r ->
            if (r.sourceNoteId == noteId) {
                r.copy(sourceX = position.x, sourceY = position.y)
            } else if (r.targetNoteId == noteId) {
                r.copy(targetX = position.x, targetY = position.y)
            } else {
                r
            }
        }
    }

    fun tidyUpNotes(state: BoardEditorState): BoardEditorState {
        val notes = state.notes
        val ropes = state.ropes
        val boardWidth = state.boardSize.width
        val boardHeight = state.boardSize.height

        if (notes.isEmpty()) return state

        val columnCount = 3
        val padding = 32f

        val columnWidths = FloatArray(columnCount)
        notes.forEachIndexed { index, note ->
            val column = index % columnCount
            val width = if (note.size.width > 0) note.size.width.toFloat() else 300f
            columnWidths[column] = maxOf(columnWidths[column], width)
        }

        val totalWidth = columnWidths.sum() + (columnCount - 1) * padding
        val startX = (boardWidth - totalWidth) / 2

        val columnXOffsets = FloatArray(columnCount)
        columnXOffsets[0] = startX
        for (i in 1 until columnCount) {
            columnXOffsets[i] = columnXOffsets[i - 1] + columnWidths[i - 1] + padding
        }

        val tempHeights = FloatArray(columnCount)
        notes.forEachIndexed { index, note ->
            val column = index % columnCount
            val height = if (note.size.height > 0) note.size.height.toFloat() else 250f
            tempHeights[column] += height + padding
        }
        val totalHeight = tempHeights.maxOrNull() ?: 0f
        val startY = (boardHeight - totalHeight) / 2

        val columnHeights = FloatArray(columnCount) { startY }

        val tidiedNotes = notes.mapIndexed { index, note ->
            val column = index % columnCount
            val x = columnXOffsets[column]
            val y = columnHeights[column]

            val height = if (note.size.height > 0) note.size.height.toFloat() else 250f
            columnHeights[column] += height + padding

            note.copy(posX = x, posY = y)
        }

        val tidiedRopes = ropes.map { rope ->
            val sourceNote = tidiedNotes.find { it.noteId == rope.sourceNoteId }
            val targetNote = tidiedNotes.find { it.noteId == rope.targetNoteId }
            rope.copy(
                sourceX = sourceNote?.posX ?: rope.sourceX,
                sourceY = sourceNote?.posY ?: rope.sourceY,
                targetX = targetNote?.posX ?: rope.targetX,
                targetY = targetNote?.posY ?: rope.targetY
            )
        }

        return state.copy(
            notes = tidiedNotes,
            ropes = tidiedRopes,
            dialogState = BoardDialogState.None
        )
    }

    fun alignSelectedNotes(state: BoardEditorState): BoardEditorState {
        val selectedNoteIds = state.selectedNoteIds
        if (selectedNoteIds.isEmpty()) return state

        val notes = state.notes
        val selectedNotes = notes.filter { it.noteId in selectedNoteIds }

        val startX = selectedNotes.minOfOrNull { it.posX } ?: 0f
        val startY = selectedNotes.minOfOrNull { it.posY } ?: 0f

        val columnCount = 3
        val padding = 32f

        val columnWidths = FloatArray(columnCount)
        selectedNotes.forEachIndexed { index, note ->
            val column = index % columnCount
            val width = if (note.size.width > 0) note.size.width.toFloat() else 300f
            columnWidths[column] = maxOf(columnWidths[column], width)
        }

        val columnXOffsets = FloatArray(columnCount)
        columnXOffsets[0] = startX
        for (i in 1 until columnCount) {
            columnXOffsets[i] = columnXOffsets[i - 1] + columnWidths[i - 1] + padding
        }

        val columnHeights = FloatArray(columnCount) { startY }

        val updatedNotesMap = selectedNotes.mapIndexed { index, note ->
            val column = index % columnCount
            val x = columnXOffsets[column]
            val y = columnHeights[column]

            val height = if (note.size.height > 0) note.size.height.toFloat() else 250f
            columnHeights[column] += height + padding

            note.noteId to note.copy(posX = x, posY = y)
        }.toMap()

        val tidiedNotes = notes.map { note ->
            updatedNotesMap[note.noteId] ?: note
        }

        val tidiedRopes = state.ropes.map { rope ->
            val sourceNote = tidiedNotes.find { it.noteId == rope.sourceNoteId }
            val targetNote = tidiedNotes.find { it.noteId == rope.targetNoteId }
            rope.copy(
                sourceX = sourceNote?.posX ?: rope.sourceX,
                sourceY = sourceNote?.posY ?: rope.sourceY,
                targetX = targetNote?.posX ?: rope.targetX,
                targetY = targetNote?.posY ?: rope.targetY
            )
        }

        return state.copy(
            notes = tidiedNotes,
            ropes = tidiedRopes,
            dialogState = BoardDialogState.None,
            previewRope = null,
            selectedNoteIds = emptyList(),
            noteHighlightId = null,
        )
    }

    fun onTap(tapOffset: Offset, boardId: String, state: BoardEditorState): BoardEditorState {
        val notes = state.notes.filter { it.status == "active" }
        val ropes = state.ropes.filter { it.status == "active" }

        val note = findNote(tapOffset, notes)
        var previewRope: Rope? = null
        var highlightNote = false
        if (note != null) {
            val note = notes.first { it.noteId == note.noteId }
            val noteIds = state.selectedNoteIds + note.noteId

            val nearest = findNearestNote(note.noteId, notes)
            if (nearest != null) {
                val sourceNote = notes.first { it.noteId == note.noteId }
                val connectedRopes =
                    ropes.filter { (sourceNote.noteId == it.sourceNoteId || sourceNote.noteId == it.targetNoteId) && (nearest.second?.noteId == it.sourceNoteId || nearest.second?.noteId == it.targetNoteId) }
                if (connectedRopes.isEmpty()) {
                    previewRope = createPreviewRope(sourceNote, nearest.second, boardId)
                    highlightNote = true
                }
            }

            return state.copy(
                selectedNoteIds = noteIds,
                selectedIndicator = generateSelectedIndicator(
                    notes.filter { it.noteId in noteIds },
                    note.noteId,
                    nearest?.first ?: "",
                ),
                noteHighlightId = if (highlightNote && state.selectedNoteIds.isEmpty()) nearest?.second?.noteId else null,
                previewRope = if (state.selectedNoteIds.isEmpty()) previewRope else null,
                selectedRopeId = null,
            )
        }

        val rope = findRope(tapOffset, ropes)
        if (rope != null) {
            return state.copy(
                selectedRopeId = rope.ropeId,
                selectedNoteIds = emptyList(),
                selectedIndicator = SelectedIndicator(),
                previewRope = null,
            )
        }

        val ropeHandler = findRopeHandler(tapOffset, state.selectedIndicator.handlers)
        if (ropeHandler != null && state.previewRope != null) {
            val rope = state.previewRope
            return state.copy(
                dialogState = BoardDialogState.None,
                selectedRopeId = null,
                selectedNoteIds = emptyList(),
                previewRope = null,
                noteHighlightId = null,
                ropes = state.ropes + rope
            )
        }

        return state.copy(
            dialogState = BoardDialogState.None,
            selectedRopeId = null,
            selectedNoteIds = emptyList(),
            previewRope = null,
            noteHighlightId = null,
        )
    }

    fun findNote(tap: Offset, notes: List<NoteCard>): NoteCard? {
        return notes.asReversed().firstOrNull { note ->
            val left = note.posX
            val top = note.posY
            val right = left + note.size.width
            val bottom = top + note.size.height
            tap.x in left..right && tap.y in top..bottom
        }
    }

    private fun findRope(tap: Offset, ropes: List<Rope>): Rope? {
        return ropes.asReversed().firstOrNull { rope ->
            val sourceSize = rope.sourceSize
            val targetSize = rope.targetSize
            val initialRope = Offset(rope.sourceX, rope.sourceY)
            val targetRope = Offset(rope.targetX, rope.targetY)
            val startCenterOffset = Offset(
                sourceSize.width / 2f, sourceSize.height / 2f
            )
            val targetCenterOffset = Offset(
                targetSize.width / 2f, targetSize.height / 2f
            )
            val start = initialRope + startCenterOffset
            val end = targetRope + targetCenterOffset
            val touchRadius = max(8f * 2f, 24f / 1)
            val distance = distancePointToSegment(tap, start, end)
            distance < touchRadius
        }
    }

    private fun findRopeHandler(tap: Offset, handlers: List<DragHandler>): DragHandler? {
        return handlers.asReversed().firstOrNull { handler ->
            val center = Offset(
                handler.position.x + 53 / 2f,
                handler.position.y + 53 / 2f
            )
            val radius = 53 / 2f
            val dx = tap.x - center.x
            val dy = tap.y - center.y


            dx * dx + dy * dy <= radius * radius
        }
    }

    private fun distancePointToSegment(
        point: Offset, start: Offset, end: Offset
    ): Float {
        val dx = end.x - start.x
        val dy = end.y - start.y

        if (dx == 0f && dy == 0f) {
            return (point - start).getDistance()
        }

        val t = ((point.x - start.x) * dx + (point.y - start.y) * dy) / (dx * dx + dy * dy)

        val clampedT = t.coerceIn(0f, 1f)

        val nearest = Offset(
            start.x + clampedT * dx, start.y + clampedT * dy
        )

        return (point - nearest).getDistance()
    }

    private fun createPreviewRope(source: NoteCard, target: NoteCard?, boardId: String): Rope? {
        if (target != null) {
            return Rope(
                ropeId = UUID.randomUUID().toString(),
                sourceNoteId = source.noteId,
                targetNoteId = target.noteId,
                boardId = boardId,
                sourceX = source.posX,
                sourceY = source.posY,
                targetX = target.posX,
                targetY = target.posY,
                targetSize = target.size,
                sourceSize = source.size
            )
        } else {
            return null
        }
    }

    private fun generateSelectedIndicator(
        notes: List<NoteCard>,
        noteId: String,
        handlerPosition: String,
    ): SelectedIndicator {
        val minX = notes.minOfOrNull { it.posX } ?: 0f
        val minY = notes.minOfOrNull { it.posY } ?: 0f
        val maxX = notes.maxOfOrNull { it.posX + it.size.width } ?: 0f
        val maxY = notes.maxOfOrNull { it.posY + it.size.height } ?: 0f

        val indicatorWidth = maxX - minX
        val indicatorHeight = maxY - minY

        val position = Offset(minX, minY)
        val size = IntSize(indicatorWidth.toInt(), indicatorHeight.toInt())

        val leftIndicator = Offset(minX - 71, minY + (size.height / 2f) - (53 / 2))
        val topIndicator = Offset(minX + (size.width / 2f) - (53 / 2), minY - 71)
        val rightIndicator = Offset(maxX + 18, minY + (size.height / 2f) - (53 / 2))
        val bottomIndicator = Offset(minX + (size.width / 2f) - (53 / 2), maxY + 18)

        val handlers = mutableListOf<DragHandler>()

        when (handlerPosition) {
            "left" -> {
                handlers.add(DragHandler(leftIndicator, 0f, noteId))
            }

            "top" -> {
                handlers.add(DragHandler(topIndicator, 90f, noteId))
            }

            "right" -> {
                handlers.add(DragHandler(rightIndicator, 180f, noteId))
            }

            "bottom" -> {
                handlers.add(DragHandler(bottomIndicator, 270f, noteId))
            }

            else -> {
                handlers.add(DragHandler(leftIndicator, 0f, noteId))
                handlers.add(DragHandler(topIndicator, 90f, noteId))
                handlers.add(DragHandler(rightIndicator, 180f, noteId))
                handlers.add(DragHandler(bottomIndicator, 270f, noteId))
            }
        }

        return SelectedIndicator(position, size, handlers)
    }

    private fun findNearestNote(
        sourceNoteId: String,
        notes: List<NoteCard>
    ): Pair<String, NoteCard?>? {
        val source = notes.first { it.noteId == sourceNoteId }
        val sourceLeft = source.posX
        val sourceTop = source.posY
        val sourceRight = sourceLeft + source.size.width
        val sourceBottom = sourceTop + source.size.height

        var minDistance = Float.MAX_VALUE
        var nearest: NoteCard? = null
        var handlerPosition = ""

        for (n in notes) {
            if (n.noteId == sourceNoteId) continue

            val left = n.posX
            val top = n.posY
            val right = left + n.size.width
            val bottom = top + n.size.height

            val horizontalGap = max(0f, max(left - sourceRight, sourceLeft - right))
            val verticalGap = max(0f, max(top - sourceBottom, sourceTop - bottom))

            val distance = sqrt(horizontalGap * horizontalGap + verticalGap * verticalGap)

            if (distance < minDistance) {
                minDistance = distance
                nearest = n
            }
        }

        if (nearest != null) {
            val dx = nearest.centerX - source.centerX
            val dy = nearest.centerY - source.centerY

            handlerPosition = if (abs(dx) > abs(dy)) {
                if (dx > 0) {
                    "right"
                } else {
                    "left"
                }
            } else {
                if (dy > 0) {
                    "bottom"
                } else {
                    "top"
                }
            }
        }

        val threshold = 150f
        return if (minDistance < threshold) Pair(handlerPosition, nearest) else null
    }
}