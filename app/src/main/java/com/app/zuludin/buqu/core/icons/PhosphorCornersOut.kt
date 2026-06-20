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

val PhosphorCornersOut: ImageVector
    get() {
        if (_PhosphorCornersOut != null) return _PhosphorCornersOut!!

        _PhosphorCornersOut = ImageVector.Builder(
            name = "corners-out",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(216f, 48f)
                verticalLineTo(88f)
                arcToRelative(8f, 8f, 0f, false, true, -16f, 0f)
                verticalLineTo(56f)
                horizontalLineTo(168f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, -16f)
                horizontalLineToRelative(40f)
                arcTo(8f, 8f, 0f, false, true, 216f, 48f)
                close()
                moveTo(88f, 200f)
                horizontalLineTo(56f)
                verticalLineTo(168f)
                arcToRelative(8f, 8f, 0f, false, false, -16f, 0f)
                verticalLineToRelative(40f)
                arcToRelative(8f, 8f, 0f, false, false, 8f, 8f)
                horizontalLineTo(88f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                close()
                moveToRelative(120f, -40f)
                arcToRelative(8f, 8f, 0f, false, false, -8f, 8f)
                verticalLineToRelative(32f)
                horizontalLineTo(168f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                horizontalLineToRelative(40f)
                arcToRelative(8f, 8f, 0f, false, false, 8f, -8f)
                verticalLineTo(168f)
                arcTo(8f, 8f, 0f, false, false, 208f, 160f)
                close()
                moveTo(88f, 40f)
                horizontalLineTo(48f)
                arcToRelative(8f, 8f, 0f, false, false, -8f, 8f)
                verticalLineTo(88f)
                arcToRelative(8f, 8f, 0f, false, false, 16f, 0f)
                verticalLineTo(56f)
                horizontalLineTo(88f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                close()
            }
        }.build()

        return _PhosphorCornersOut!!
    }

private var _PhosphorCornersOut: ImageVector? = null