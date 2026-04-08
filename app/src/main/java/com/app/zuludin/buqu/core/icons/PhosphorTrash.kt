/*
MIT License

Copyright (c) 2020 Phosphor Icons

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.app.zuludin.buqu.core.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val PhosphorTrash: ImageVector
    get() {
        if (_PhosphorTrash != null) return _PhosphorTrash!!
        
        _PhosphorTrash = ImageVector.Builder(
            name = "trash",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(216f, 48f)
                horizontalLineTo(176f)
                verticalLineTo(40f)
                arcToRelative(24f, 24f, 0f, false, false, -24f, -24f)
                horizontalLineTo(104f)
                arcTo(24f, 24f, 0f, false, false, 80f, 40f)
                verticalLineToRelative(8f)
                horizontalLineTo(40f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                horizontalLineToRelative(8f)
                verticalLineTo(208f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
                horizontalLineTo(192f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, -16f)
                verticalLineTo(64f)
                horizontalLineToRelative(8f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                close()
                moveTo(96f, 40f)
                arcToRelative(8f, 8f, 0f, false, true, 8f, -8f)
                horizontalLineToRelative(48f)
                arcToRelative(8f, 8f, 0f, false, true, 8f, 8f)
                verticalLineToRelative(8f)
                horizontalLineTo(96f)
                close()
                moveToRelative(96f, 168f)
                horizontalLineTo(64f)
                verticalLineTo(64f)
                horizontalLineTo(192f)
                close()
                moveTo(112f, 104f)
                verticalLineToRelative(64f)
                arcToRelative(8f, 8f, 0f, false, true, -16f, 0f)
                verticalLineTo(104f)
                arcToRelative(8f, 8f, 0f, false, true, 16f, 0f)
                close()
                moveToRelative(48f, 0f)
                verticalLineToRelative(64f)
                arcToRelative(8f, 8f, 0f, false, true, -16f, 0f)
                verticalLineTo(104f)
                arcToRelative(8f, 8f, 0f, false, true, 16f, 0f)
                close()
            }
        }.build()
        
        return _PhosphorTrash!!
    }

private var _PhosphorTrash: ImageVector? = null