package com.app.zuludin.buqu.core.icons

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val PhosphorX: ImageVector
    get() {
        if (_PhosphorX != null) return _PhosphorX!!

        _PhosphorX = ImageVector.Builder(
            name = "x",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(205.66f, 194.34f)
                arcToRelative(8f, 8f, 0f, false, true, -11.32f, 11.32f)
                lineTo(128f, 139.31f)
                lineTo(61.66f, 205.66f)
                arcToRelative(8f, 8f, 0f, false, true, -11.32f, -11.32f)
                lineTo(116.69f, 128f)
                lineTo(50.34f, 61.66f)
                arcTo(8f, 8f, 0f, false, true, 61.66f, 50.34f)
                lineTo(128f, 116.69f)
                lineToRelative(66.34f, -66.35f)
                arcToRelative(8f, 8f, 0f, false, true, 11.32f, 11.32f)
                lineTo(139.31f, 128f)
                close()
            }
        }.build()

        return _PhosphorX!!
    }

private var _PhosphorX: ImageVector? = null