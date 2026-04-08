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

val PhosphorInfo: ImageVector
    get() {
        if (_PhosphorInfo != null) return _PhosphorInfo!!
        
        _PhosphorInfo = ImageVector.Builder(
            name = "info",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(128f, 24f)
                arcTo(104f, 104f, 0f, true, false, 232f, 128f)
                arcTo(104.11f, 104.11f, 0f, false, false, 128f, 24f)
                close()
                moveToRelative(-4f, 48f)
                arcToRelative(12f, 12f, 0f, true, true, -12f, 12f)
                arcTo(12f, 12f, 0f, false, true, 124f, 72f)
                close()
                moveToRelative(12f, 112f)
                arcToRelative(16f, 16f, 0f, false, true, -16f, -16f)
                verticalLineTo(128f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, -16f)
                arcToRelative(16f, 16f, 0f, false, true, 16f, 16f)
                verticalLineToRelative(40f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, 16f)
                close()
            }
        }.build()
        
        return _PhosphorInfo!!
    }

private var _PhosphorInfo: ImageVector? = null