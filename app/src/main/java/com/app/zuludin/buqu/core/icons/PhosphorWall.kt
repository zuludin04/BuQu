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

val PhosphorWall: ImageVector
    get() {
        if (_PhosphorWall != null) return _PhosphorWall!!

        _PhosphorWall = ImageVector.Builder(
            name = "wall",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(224f, 48f)
                horizontalLineTo(32f)
                arcToRelative(8f, 8f, 0f, false, false, -8f, 8f)
                verticalLineTo(200f)
                arcToRelative(8f, 8f, 0f, false, false, 8f, 8f)
                horizontalLineTo(224f)
                arcToRelative(8f, 8f, 0f, false, false, 8f, -8f)
                verticalLineTo(56f)
                arcTo(8f, 8f, 0f, false, false, 224f, 48f)
                close()
                moveTo(88f, 144f)
                verticalLineTo(112f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(32f)
                close()
                moveToRelative(-48f, 0f)
                verticalLineTo(112f)
                horizontalLineTo(72f)
                verticalLineToRelative(32f)
                close()
                moveToRelative(144f, -32f)
                horizontalLineToRelative(32f)
                verticalLineToRelative(32f)
                horizontalLineTo(184f)
                close()
                moveToRelative(32f, -16f)
                horizontalLineTo(136f)
                verticalLineTo(64f)
                horizontalLineToRelative(80f)
                close()
                moveTo(120f, 64f)
                verticalLineTo(96f)
                horizontalLineTo(40f)
                verticalLineTo(64f)
                close()
                moveTo(40f, 160f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(32f)
                horizontalLineTo(40f)
                close()
                moveToRelative(96f, 32f)
                verticalLineTo(160f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(32f)
                close()
            }
        }.build()

        return _PhosphorWall!!
    }

private var _PhosphorWall: ImageVector? = null