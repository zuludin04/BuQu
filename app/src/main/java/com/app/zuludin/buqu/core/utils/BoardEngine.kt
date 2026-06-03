package com.app.zuludin.buqu.core.utils

import androidx.compose.ui.geometry.Offset
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import kotlin.math.max
import kotlin.math.sqrt

class BoardEngine {
    fun drag(
        note: NoteCard, notes: List<NoteCard>, ropes: List<Rope>, worldPos: Offset
    ): BoardEngineResult {
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
        val nearest = highlightNearestNode(position, ns, rs, note)

        return BoardEngineResult(notes = ns, ropes = rs, nearestNote = nearest)
    }

    private fun updateRopePosition(
        noteId: String, ropes: List<Rope>, position: Offset
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

    private fun highlightNearestNode(
        current: Offset, notes: List<NoteCard>, ropes: List<Rope>, note: NoteCard
    ): NoteCard? {
        val offset = Offset(
            x = current.x + note.size.width / 2f, y = current.y + note.size.height / 2f
        )
        val nearest = findNearestNode(offset, notes, note.noteId)

        return if (nearest != null) {
            val connectedRope =
                ropes.firstOrNull { (it.sourceNoteId == note.noteId && it.targetNoteId == nearest.noteId) || (it.sourceNoteId == nearest.noteId && it.targetNoteId == note.noteId) }
            if (connectedRope == null) {
                nearest
            } else {
                null
            }
        } else {
            null
        }
    }

    private fun findNearestNode(
        current: Offset,
        nodes: List<NoteCard>,
        excludeId: String? = null,
    ): NoteCard? {

        var minDistance = Float.MAX_VALUE
        var nearest: NoteCard? = null

        nodes.forEach { node ->
            if (node.noteId == excludeId) return@forEach

            val nodeCenter = Offset(
                node.posX + node.size.width / 2f, node.posY + node.size.height / 2f
            )

            val dx = nodeCenter.x - current.x
            val dy = nodeCenter.y - current.y
            val distance = sqrt(dx * dx + dy * dy)

            if (distance < minDistance) {
                minDistance = distance
                nearest = node
            }
        }

        val threshold = 600f
        return if (minDistance < threshold) nearest else null
    }

    fun tidyUpNotes(
        notes: List<NoteCard>, ropes: List<Rope>, boardWidth: Float, boardHeight: Float
    ): BoardEngineResult {
        if (notes.isEmpty()) return BoardEngineResult()

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

        return BoardEngineResult(notes = tidiedNotes, ropes = tidiedRopes)
    }

    fun onTap(position: Offset, notes: List<NoteCard>, ropes: List<Rope>): BoardEngineResult? {
        val note = findNote(position, notes)
        if (note != null) {
            return BoardEngineResult(selectedNoteId = note.noteId)
        }

        val rope = findRope(position, ropes)
        if (rope != null) {
            return BoardEngineResult(selectedRopeId = rope.ropeId)
        }

        return null
    }

    private fun findNote(tap: Offset, notes: List<NoteCard>): NoteCard? {
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
}

data class BoardEngineResult(
    val notes: List<NoteCard> = emptyList(),
    val ropes: List<Rope> = emptyList(),
    val nearestNote: NoteCard? = null,
    val selectedNoteId: String? = null,
    val selectedRopeId: String? = null
)