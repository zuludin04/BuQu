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

val PhosphorLinkSimpleHorizontal: ImageVector
    get() {
        if (_PhosphorLinkSimpleHorizontal != null) return _PhosphorLinkSimpleHorizontal!!

        _PhosphorLinkSimpleHorizontal = ImageVector.Builder(
            name = "link-simple-horizontal",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(80f, 120f)
                horizontalLineToRelative(96f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, 16f)
                horizontalLineTo(80f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, -16f)
                close()
                moveToRelative(24f, 48f)
                horizontalLineTo(64f)
                arcToRelative(40f, 40f, 0f, false, true, 0f, -80f)
                horizontalLineToRelative(40f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                horizontalLineTo(64f)
                arcToRelative(56f, 56f, 0f, false, false, 0f, 112f)
                horizontalLineToRelative(40f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                close()
                moveToRelative(88f, -96f)
                horizontalLineTo(152f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                horizontalLineToRelative(40f)
                arcToRelative(40f, 40f, 0f, false, true, 0f, 80f)
                horizontalLineTo(152f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                horizontalLineToRelative(40f)
                arcToRelative(56f, 56f, 0f, false, false, 0f, -112f)
                close()
            }
        }.build()

        return _PhosphorLinkSimpleHorizontal!!
    }

private var _PhosphorLinkSimpleHorizontal: ImageVector? = null