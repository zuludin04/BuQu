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

val PhosphorNote: ImageVector
    get() {
        if (_PhosphorNote != null) return _PhosphorNote!!
        
        _PhosphorNote = ImageVector.Builder(
            name = "note",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(88f, 96f)
                arcToRelative(8f, 8f, 0f, false, true, 8f, -8f)
                horizontalLineToRelative(64f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, 16f)
                horizontalLineTo(96f)
                arcTo(8f, 8f, 0f, false, true, 88f, 96f)
                close()
                moveToRelative(8f, 40f)
                horizontalLineToRelative(64f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                horizontalLineTo(96f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                close()
                moveToRelative(32f, 16f)
                horizontalLineTo(96f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                horizontalLineToRelative(32f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                close()
                moveTo(224f, 48f)
                verticalLineTo(156.69f)
                arcTo(15.86f, 15.86f, 0f, false, true, 219.31f, 168f)
                lineTo(168f, 219.31f)
                arcTo(15.86f, 15.86f, 0f, false, true, 156.69f, 224f)
                horizontalLineTo(48f)
                arcToRelative(16f, 16f, 0f, false, true, -16f, -16f)
                verticalLineTo(48f)
                arcTo(16f, 16f, 0f, false, true, 48f, 32f)
                horizontalLineTo(208f)
                arcTo(16f, 16f, 0f, false, true, 224f, 48f)
                close()
                moveTo(48f, 208f)
                horizontalLineTo(152f)
                verticalLineTo(160f)
                arcToRelative(8f, 8f, 0f, false, true, 8f, -8f)
                horizontalLineToRelative(48f)
                verticalLineTo(48f)
                horizontalLineTo(48f)
                close()
                moveToRelative(120f, -40f)
                verticalLineToRelative(28.7f)
                lineTo(196.69f, 168f)
                close()
            }
        }.build()
        
        return _PhosphorNote!!
    }

private var _PhosphorNote: ImageVector? = null