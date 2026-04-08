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

val PhosphorStar: ImageVector
    get() {
        if (_PhosphorStar != null) return _PhosphorStar!!
        
        _PhosphorStar = ImageVector.Builder(
            name = "star",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(234.29f, 114.85f)
                lineToRelative(-45f, 38.83f)
                lineTo(203f, 211.75f)
                arcToRelative(16.4f, 16.4f, 0f, false, true, -24.5f, 17.82f)
                lineTo(128f, 198.49f)
                lineTo(77.47f, 229.57f)
                arcTo(16.4f, 16.4f, 0f, false, true, 53f, 211.75f)
                lineToRelative(13.76f, -58.07f)
                lineToRelative(-45f, -38.83f)
                arcTo(16.46f, 16.46f, 0f, false, true, 31.08f, 86f)
                lineToRelative(59f, -4.76f)
                lineToRelative(22.76f, -55.08f)
                arcToRelative(16.36f, 16.36f, 0f, false, true, 30.27f, 0f)
                lineToRelative(22.75f, 55.08f)
                lineToRelative(59f, 4.76f)
                arcToRelative(16.46f, 16.46f, 0f, false, true, 9.37f, 28.86f)
                close()
            }
        }.build()
        
        return _PhosphorStar!!
    }

private var _PhosphorStar: ImageVector? = null