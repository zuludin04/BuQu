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

val PhosphorArrowClockwise: ImageVector
    get() {
        if (_PhosphorArrowClockwise != null) return _PhosphorArrowClockwise!!
        
        _PhosphorArrowClockwise = ImageVector.Builder(
            name = "arrow-clockwise",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(240f, 56f)
                verticalLineToRelative(48f)
                arcToRelative(8f, 8f, 0f, false, true, -8f, 8f)
                horizontalLineTo(184f)
                arcToRelative(8f, 8f, 0f, false, true, -5.66f, -13.66f)
                lineToRelative(17f, -17f)
                lineToRelative(-10.55f, -9.65f)
                lineToRelative(-0.25f, -0.24f)
                arcToRelative(80f, 80f, 0f, true, false, -1.67f, 114.78f)
                arcToRelative(8f, 8f, 0f, true, true, 11f, 11.63f)
                arcTo(95.44f, 95.44f, 0f, false, true, 128f, 224f)
                horizontalLineToRelative(-1.32f)
                arcTo(96f, 96f, 0f, true, true, 195.75f, 60f)
                lineToRelative(10.93f, 10f)
                lineTo(226.34f, 50.3f)
                arcTo(8f, 8f, 0f, false, true, 240f, 56f)
                close()
            }
        }.build()
        
        return _PhosphorArrowClockwise!!
    }

private var _PhosphorArrowClockwise: ImageVector? = null