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

val PhosphorXCircle: ImageVector
    get() {
        if (_PhosphorXCircle != null) return _PhosphorXCircle!!

        _PhosphorXCircle = ImageVector.Builder(
            name = "x-circle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(164.24f, 100.24f)
                lineTo(136.48f, 128f)
                lineToRelative(27.76f, 27.76f)
                arcToRelative(6f, 6f, 0f, true, true, -8.48f, 8.48f)
                lineTo(128f, 136.48f)
                lineToRelative(-27.76f, 27.76f)
                arcToRelative(6f, 6f, 0f, false, true, -8.48f, -8.48f)
                lineTo(119.52f, 128f)
                lineTo(91.76f, 100.24f)
                arcToRelative(6f, 6f, 0f, false, true, 8.48f, -8.48f)
                lineTo(128f, 119.52f)
                lineToRelative(27.76f, -27.76f)
                arcToRelative(6f, 6f, 0f, false, true, 8.48f, 8.48f)
                close()
                moveTo(230f, 128f)
                arcTo(102f, 102f, 0f, true, true, 128f, 26f)
                arcTo(102.12f, 102.12f, 0f, false, true, 230f, 128f)
                close()
                moveToRelative(-12f, 0f)
                arcToRelative(90f, 90f, 0f, true, false, -90f, 90f)
                arcTo(90.1f, 90.1f, 0f, false, false, 218f, 128f)
                close()
            }
        }.build()

        return _PhosphorXCircle!!
    }

private var _PhosphorXCircle: ImageVector? = null