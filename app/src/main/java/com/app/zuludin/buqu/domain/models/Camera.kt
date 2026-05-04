package com.app.zuludin.buqu.domain.models

import androidx.compose.ui.geometry.Offset

data class Camera(
    var zoom: Float = 1f,
    val offset: Offset = Offset.Zero
)
