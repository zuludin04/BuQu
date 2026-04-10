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

val PhosphorImage: ImageVector
    get() {
        if (_PhosphorImage != null) return _PhosphorImage!!

        _PhosphorImage = ImageVector.Builder(
            name = "image",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(216f, 40f)
                horizontalLineTo(40f)
                arcTo(16f, 16f, 0f, false, false, 24f, 56f)
                verticalLineTo(200f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
                horizontalLineTo(216f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, -16f)
                verticalLineTo(56f)
                arcTo(16f, 16f, 0f, false, false, 216f, 40f)
                close()
                moveToRelative(0f, 16f)
                verticalLineTo(158.75f)
                lineToRelative(-26.07f, -26.06f)
                arcToRelative(16f, 16f, 0f, false, false, -22.63f, 0f)
                lineToRelative(-20f, 20f)
                lineToRelative(-44f, -44f)
                arcToRelative(16f, 16f, 0f, false, false, -22.62f, 0f)
                lineTo(40f, 149.37f)
                verticalLineTo(56f)
                close()
                moveTo(40f, 172f)
                lineToRelative(52f, -52f)
                lineToRelative(80f, 80f)
                horizontalLineTo(40f)
                close()
                moveToRelative(176f, 28f)
                horizontalLineTo(194.63f)
                lineToRelative(-36f, -36f)
                lineToRelative(20f, -20f)
                lineTo(216f, 181.38f)
                verticalLineTo(200f)
                close()
                moveTo(144f, 100f)
                arcToRelative(12f, 12f, 0f, true, true, 12f, 12f)
                arcTo(12f, 12f, 0f, false, true, 144f, 100f)
                close()
            }
        }.build()

        return _PhosphorImage!!
    }

private var _PhosphorImage: ImageVector? = null