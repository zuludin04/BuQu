package com.app.zuludin.buqu.domain.models

import androidx.compose.ui.geometry.Offset

data class Camera(
    var zoom: Float = 1f,
    val offset: Offset = Offset.Zero
) {
    /** Convert a point in world coordinates to screen coordinates. */
    fun worldToScreen(world: Offset): Offset {
        return Offset(
            x = world.x * zoom + offset.x,
            y = world.y * zoom + offset.y,
        )
    }

    /** Convert a point in screen coordinates to world coordinates. */
    fun screenToWorld(screen: Offset): Offset {
        return Offset(
            x = (screen.x - offset.x) / zoom,
            y = (screen.y - offset.y) / zoom,
        )
    }
}
