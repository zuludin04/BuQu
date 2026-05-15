package com.app.zuludin.buqu.core.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.app.zuludin.buqu.domain.models.NoteCard
import com.app.zuludin.buqu.domain.models.Rope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BoardEngineTest {

    private lateinit var engine: BoardEngine

    @Before
    fun setUp() {
        engine = BoardEngine()
    }

    @Test
    fun `drag updates note and related rope positions`() {
        val note1 = NoteCard("n1", "b1", "Note 1", 0f, 0f, IntSize(100, 100), color = "w", image = "")
        val note2 = NoteCard("n2", "b1", "Note 2", 200f, 200f, IntSize(100, 100), color = "w", image = "")
        val rope = Rope("r1", "n1", "n2", "b1", 0f, 0f, IntSize(100, 100), 200f, 200f, IntSize(100, 100))
        
        val newPos = Offset(50f, 50f)
        val result = engine.drag(note1, listOf(note1, note2), listOf(rope), newPos)
        
        val updatedNote1 = result.notes.first { it.noteId == "n1" }
        assertEquals(50f, updatedNote1.posX)
        assertEquals(50f, updatedNote1.posY)
        
        val updatedRope = result.ropes.first { it.ropeId == "r1" }
        assertEquals(50f, updatedRope.sourceX)
        assertEquals(50f, updatedRope.sourceY)
    }

    @Test
    fun `drag highlights nearest note if within threshold and not connected`() {
        val note1 = NoteCard("n1", "b1", "Note 1", 0f, 0f, IntSize(100, 100), color = "w", image = "")
        val note2 = NoteCard("n2", "b1", "Note 2", 150f, 150f, IntSize(100, 100), color = "w", image = "")
        
        // Dragging n1 to be near n2
        val newPos = Offset(100f, 100f)
        val result = engine.drag(note1, listOf(note1, note2), emptyList(), newPos)
        
        assertNotNull(result.nearestNote)
        assertEquals("n2", result.nearestNote?.noteId)
    }

    @Test
    fun `drag does not highlight if already connected`() {
        val note1 = NoteCard("n1", "b1", "Note 1", 0f, 0f, IntSize(100, 100), color = "w", image = "")
        val note2 = NoteCard("n2", "b1", "Note 2", 150f, 150f, IntSize(100, 100), color = "w", image = "")
        val rope = Rope("r1", "n1", "n2", "b1", 0f, 0f, IntSize(100, 100), 150f, 150f, IntSize(100, 100))
        
        val newPos = Offset(100f, 100f)
        val result = engine.drag(note1, listOf(note1, note2), listOf(rope), newPos)
        
        assertNull(result.nearestNote)
    }

    @Test
    fun `tidyUpNotes repositions notes in a grid`() {
        val notes = (1..4).map { 
            NoteCard("n$it", "b1", "Note $it", 0f, 0f, IntSize(100, 100), color = "w", image = "")
        }
        
        val result = engine.tidyUpNotes(notes, emptyList(), 1000f, 1000f)
        
        // With 3 columns:
        // n1: Col 0, Row 0
        // n2: Col 1, Row 0
        // n3: Col 2, Row 0
        // n4: Col 0, Row 1
        
        val tidied1 = result.notes.first { it.noteId == "n1" }
        val tidied2 = result.notes.first { it.noteId == "n2" }
        val tidied4 = result.notes.first { it.noteId == "n4" }
        
        assertEquals(tidied1.posY, tidied2.posY)
        assertEquals(tidied1.posX, tidied4.posX)
        assertTrue(tidied4.posY > tidied1.posY)
    }
}
