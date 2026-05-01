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

val PhosphorPencil: ImageVector
    get() {
        if (_PhosphorPencil != null) return _PhosphorPencil!!

        _PhosphorPencil = ImageVector.Builder(
            name = "pencil",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(227.31f, 73.37f)
                lineTo(182.63f, 28.68f)
                arcToRelative(16f, 16f, 0f, false, false, -22.63f, 0f)
                lineTo(36.69f, 152f)
                arcTo(15.86f, 15.86f, 0f, false, false, 32f, 163.31f)
                verticalLineTo(208f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
                horizontalLineTo(92.69f)
                arcTo(15.86f, 15.86f, 0f, false, false, 104f, 219.31f)
                lineTo(227.31f, 96f)
                arcToRelative(16f, 16f, 0f, false, false, 0f, -22.63f)
                close()
                moveTo(51.31f, 160f)
                lineTo(136f, 75.31f)
                lineTo(152.69f, 92f)
                lineTo(68f, 176.68f)
                close()
                moveTo(48f, 179.31f)
                lineTo(76.69f, 208f)
                horizontalLineTo(48f)
                close()
                moveToRelative(48f, 25.38f)
                lineTo(79.31f, 188f)
                lineTo(164f, 103.31f)
                lineTo(180.69f, 120f)
                close()
                moveToRelative(96f, -96f)
                lineTo(147.31f, 64f)
                lineToRelative(24f, -24f)
                lineTo(216f, 84.68f)
                close()
            }
        }.build()

        return _PhosphorPencil!!
    }

private var _PhosphorPencil: ImageVector? = null