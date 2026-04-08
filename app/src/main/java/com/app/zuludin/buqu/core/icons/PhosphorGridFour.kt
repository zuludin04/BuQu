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

val PhosphorGridFour: ImageVector
    get() {
        if (_PhosphorGridFour != null) return _PhosphorGridFour!!
        
        _PhosphorGridFour = ImageVector.Builder(
            name = "grid-four",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(216f, 56f)
                verticalLineToRelative(60f)
                arcToRelative(4f, 4f, 0f, false, true, -4f, 4f)
                horizontalLineTo(136f)
                verticalLineTo(44f)
                arcToRelative(4f, 4f, 0f, false, true, 4f, -4f)
                horizontalLineToRelative(60f)
                arcTo(16f, 16f, 0f, false, true, 216f, 56f)
                close()
                moveTo(116f, 40f)
                horizontalLineTo(56f)
                arcTo(16f, 16f, 0f, false, false, 40f, 56f)
                verticalLineToRelative(60f)
                arcToRelative(4f, 4f, 0f, false, false, 4f, 4f)
                horizontalLineToRelative(76f)
                verticalLineTo(44f)
                arcTo(4f, 4f, 0f, false, false, 116f, 40f)
                close()
                moveToRelative(96f, 96f)
                horizontalLineTo(136f)
                verticalLineToRelative(76f)
                arcToRelative(4f, 4f, 0f, false, false, 4f, 4f)
                horizontalLineToRelative(60f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, -16f)
                verticalLineTo(140f)
                arcTo(4f, 4f, 0f, false, false, 212f, 136f)
                close()
                moveTo(40f, 140f)
                verticalLineToRelative(60f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
                horizontalLineToRelative(60f)
                arcToRelative(4f, 4f, 0f, false, false, 4f, -4f)
                verticalLineTo(136f)
                horizontalLineTo(44f)
                arcTo(4f, 4f, 0f, false, false, 40f, 140f)
                close()
            }
        }.build()
        
        return _PhosphorGridFour!!
    }

private var _PhosphorGridFour: ImageVector? = null