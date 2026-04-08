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

val PhosphorChalkboardSimple: ImageVector
    get() {
        if (_PhosphorChalkboardSimple != null) return _PhosphorChalkboardSimple!!
        
        _PhosphorChalkboardSimple = ImageVector.Builder(
            name = "chalkboard-simple",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(240f, 192f)
                horizontalLineToRelative(-8f)
                verticalLineTo(168f)
                arcToRelative(8f, 8f, 0f, false, false, -8f, -8f)
                horizontalLineTo(160f)
                arcToRelative(8f, 8f, 0f, false, false, -8f, 8f)
                verticalLineToRelative(24f)
                horizontalLineTo(40f)
                verticalLineTo(56f)
                horizontalLineTo(216f)
                verticalLineToRelative(80f)
                arcToRelative(8f, 8f, 0f, false, false, 16f, 0f)
                verticalLineTo(56f)
                arcToRelative(16f, 16f, 0f, false, false, -16f, -16f)
                horizontalLineTo(40f)
                arcTo(16f, 16f, 0f, false, false, 24f, 56f)
                verticalLineTo(192f)
                horizontalLineTo(16f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                horizontalLineTo(240f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                close()
                moveToRelative(-72f, -16f)
                horizontalLineToRelative(48f)
                verticalLineToRelative(16f)
                horizontalLineTo(168f)
                close()
            }
        }.build()
        
        return _PhosphorChalkboardSimple!!
    }

private var _PhosphorChalkboardSimple: ImageVector? = null