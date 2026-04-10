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

val PhosphorLinkBreak: ImageVector
    get() {
        if (_PhosphorLinkBreak != null) return _PhosphorLinkBreak!!

        _PhosphorLinkBreak = ImageVector.Builder(
            name = "link-break",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(198.63f, 57.37f)
                arcToRelative(32f, 32f, 0f, false, false, -45.19f, -0.06f)
                lineTo(141.79f, 69.52f)
                arcToRelative(8f, 8f, 0f, false, true, -11.58f, -11f)
                lineToRelative(11.72f, -12.29f)
                arcToRelative(1.59f, 1.59f, 0f, false, true, 0.13f, -0.13f)
                arcToRelative(48f, 48f, 0f, false, true, 67.88f, 67.88f)
                arcToRelative(1.59f, 1.59f, 0f, false, true, -0.13f, 0.13f)
                lineToRelative(-12.29f, 11.72f)
                arcToRelative(8f, 8f, 0f, false, true, -11f, -11.58f)
                lineToRelative(12.21f, -11.65f)
                arcTo(32f, 32f, 0f, false, false, 198.63f, 57.37f)
                close()
                moveTo(114.21f, 186.48f)
                lineToRelative(-11.65f, 12.21f)
                arcToRelative(32f, 32f, 0f, false, true, -45.25f, -45.25f)
                lineToRelative(12.21f, -11.65f)
                arcToRelative(8f, 8f, 0f, false, false, -11f, -11.58f)
                lineTo(46.19f, 141.93f)
                arcToRelative(1.59f, 1.59f, 0f, false, false, -0.13f, 0.13f)
                arcToRelative(48f, 48f, 0f, false, false, 67.88f, 67.88f)
                arcToRelative(1.59f, 1.59f, 0f, false, false, 0.13f, -0.13f)
                lineToRelative(11.72f, -12.29f)
                arcToRelative(8f, 8f, 0f, true, false, -11.58f, -11f)
                close()
                moveTo(216f, 152f)
                horizontalLineTo(192f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                horizontalLineToRelative(24f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                close()
                moveTo(40f, 104f)
                horizontalLineTo(64f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                horizontalLineTo(40f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                close()
                moveToRelative(120f, 80f)
                arcToRelative(8f, 8f, 0f, false, false, -8f, 8f)
                verticalLineToRelative(24f)
                arcToRelative(8f, 8f, 0f, false, false, 16f, 0f)
                verticalLineTo(192f)
                arcTo(8f, 8f, 0f, false, false, 160f, 184f)
                close()
                moveTo(96f, 72f)
                arcToRelative(8f, 8f, 0f, false, false, 8f, -8f)
                verticalLineTo(40f)
                arcToRelative(8f, 8f, 0f, false, false, -16f, 0f)
                verticalLineTo(64f)
                arcTo(8f, 8f, 0f, false, false, 96f, 72f)
                close()
            }
        }.build()

        return _PhosphorLinkBreak!!
    }

private var _PhosphorLinkBreak: ImageVector? = null