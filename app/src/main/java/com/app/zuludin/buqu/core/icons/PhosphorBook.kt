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

val PhosphorBookOpen: ImageVector
    get() {
        if (_PhosphorBookOpen != null) return _PhosphorBookOpen!!

        _PhosphorBookOpen = ImageVector.Builder(
            name = "book-open",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(232f, 48f)
                horizontalLineTo(160f)
                arcToRelative(40f, 40f, 0f, false, false, -32f, 16f)
                arcTo(40f, 40f, 0f, false, false, 96f, 48f)
                horizontalLineTo(24f)
                arcToRelative(8f, 8f, 0f, false, false, -8f, 8f)
                verticalLineTo(200f)
                arcToRelative(8f, 8f, 0f, false, false, 8f, 8f)
                horizontalLineTo(96f)
                arcToRelative(24f, 24f, 0f, false, true, 24f, 24f)
                arcToRelative(8f, 8f, 0f, false, false, 16f, 0f)
                arcToRelative(24f, 24f, 0f, false, true, 24f, -24f)
                horizontalLineToRelative(72f)
                arcToRelative(8f, 8f, 0f, false, false, 8f, -8f)
                verticalLineTo(56f)
                arcTo(8f, 8f, 0f, false, false, 232f, 48f)
                close()
                moveTo(96f, 192f)
                horizontalLineTo(32f)
                verticalLineTo(64f)
                horizontalLineTo(96f)
                arcToRelative(24f, 24f, 0f, false, true, 24f, 24f)
                verticalLineTo(200f)
                arcTo(39.81f, 39.81f, 0f, false, false, 96f, 192f)
                close()
                moveToRelative(128f, 0f)
                horizontalLineTo(160f)
                arcToRelative(39.81f, 39.81f, 0f, false, false, -24f, 8f)
                verticalLineTo(88f)
                arcToRelative(24f, 24f, 0f, false, true, 24f, -24f)
                horizontalLineToRelative(64f)
                close()
            }
        }.build()

        return _PhosphorBookOpen!!
    }

private var _PhosphorBookOpen: ImageVector? = null