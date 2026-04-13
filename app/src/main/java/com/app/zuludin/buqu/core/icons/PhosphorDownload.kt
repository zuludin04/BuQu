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

val PhosphorDownload: ImageVector
    get() {
        if (_PhosphorDownload != null) return _PhosphorDownload!!

        _PhosphorDownload = ImageVector.Builder(
            name = "download",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(240f, 136f)
                verticalLineToRelative(64f)
                arcToRelative(16f, 16f, 0f, false, true, -16f, 16f)
                horizontalLineTo(32f)
                arcToRelative(16f, 16f, 0f, false, true, -16f, -16f)
                verticalLineTo(136f)
                arcToRelative(16f, 16f, 0f, false, true, 16f, -16f)
                horizontalLineTo(72f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, 16f)
                horizontalLineTo(32f)
                verticalLineToRelative(64f)
                horizontalLineTo(224f)
                verticalLineTo(136f)
                horizontalLineTo(184f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, -16f)
                horizontalLineToRelative(40f)
                arcTo(16f, 16f, 0f, false, true, 240f, 136f)
                close()
                moveToRelative(-117.66f, -2.34f)
                arcToRelative(8f, 8f, 0f, false, false, 11.32f, 0f)
                lineToRelative(48f, -48f)
                arcToRelative(8f, 8f, 0f, false, false, -11.32f, -11.32f)
                lineTo(136f, 108.69f)
                verticalLineTo(24f)
                arcToRelative(8f, 8f, 0f, false, false, -16f, 0f)
                verticalLineToRelative(84.69f)
                lineTo(85.66f, 74.34f)
                arcTo(8f, 8f, 0f, false, false, 74.34f, 85.66f)
                close()
                moveTo(200f, 168f)
                arcToRelative(12f, 12f, 0f, true, false, -12f, 12f)
                arcTo(12f, 12f, 0f, false, false, 200f, 168f)
                close()
            }
        }.build()

        return _PhosphorDownload!!
    }

private var _PhosphorDownload: ImageVector? = null