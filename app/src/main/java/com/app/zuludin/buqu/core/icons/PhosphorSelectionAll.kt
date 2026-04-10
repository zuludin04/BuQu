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

val PhosphorSelectionAll: ImageVector
    get() {
        if (_PhosphorSelectionAll != null) return _PhosphorSelectionAll!!

        _PhosphorSelectionAll = ImageVector.Builder(
            name = "selection-all",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(104f, 40f)
                arcToRelative(8f, 8f, 0f, false, true, 8f, -8f)
                horizontalLineToRelative(32f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, 16f)
                horizontalLineTo(112f)
                arcTo(8f, 8f, 0f, false, true, 104f, 40f)
                close()
                moveToRelative(40f, 168f)
                horizontalLineTo(112f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                horizontalLineToRelative(32f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                close()
                moveTo(208f, 32f)
                horizontalLineTo(184f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                horizontalLineToRelative(24f)
                verticalLineTo(72f)
                arcToRelative(8f, 8f, 0f, false, false, 16f, 0f)
                verticalLineTo(48f)
                arcTo(16f, 16f, 0f, false, false, 208f, 32f)
                close()
                moveToRelative(8f, 72f)
                arcToRelative(8f, 8f, 0f, false, false, -8f, 8f)
                verticalLineToRelative(32f)
                arcToRelative(8f, 8f, 0f, false, false, 16f, 0f)
                verticalLineTo(112f)
                arcTo(8f, 8f, 0f, false, false, 216f, 104f)
                close()
                moveToRelative(0f, 72f)
                arcToRelative(8f, 8f, 0f, false, false, -8f, 8f)
                verticalLineToRelative(24f)
                horizontalLineTo(184f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, 16f)
                horizontalLineToRelative(24f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, -16f)
                verticalLineTo(184f)
                arcTo(8f, 8f, 0f, false, false, 216f, 176f)
                close()
                moveTo(40f, 152f)
                arcToRelative(8f, 8f, 0f, false, false, 8f, -8f)
                verticalLineTo(112f)
                arcToRelative(8f, 8f, 0f, false, false, -16f, 0f)
                verticalLineToRelative(32f)
                arcTo(8f, 8f, 0f, false, false, 40f, 152f)
                close()
                moveToRelative(32f, 56f)
                horizontalLineTo(48f)
                verticalLineTo(184f)
                arcToRelative(8f, 8f, 0f, false, false, -16f, 0f)
                verticalLineToRelative(24f)
                arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
                horizontalLineTo(72f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                close()
                moveTo(40f, 80f)
                arcToRelative(8f, 8f, 0f, false, false, 8f, -8f)
                verticalLineTo(48f)
                horizontalLineTo(72f)
                arcToRelative(8f, 8f, 0f, false, false, 0f, -16f)
                horizontalLineTo(48f)
                arcTo(16f, 16f, 0f, false, false, 32f, 48f)
                verticalLineTo(72f)
                arcTo(8f, 8f, 0f, false, false, 40f, 80f)
                close()
                moveTo(176f, 184f)
                horizontalLineTo(80f)
                arcToRelative(8f, 8f, 0f, false, true, -8f, -8f)
                verticalLineTo(80f)
                arcToRelative(8f, 8f, 0f, false, true, 8f, -8f)
                horizontalLineToRelative(96f)
                arcToRelative(8f, 8f, 0f, false, true, 8f, 8f)
                verticalLineToRelative(96f)
                arcTo(8f, 8f, 0f, false, true, 176f, 184f)
                close()
                moveToRelative(-8f, -96f)
                horizontalLineTo(88f)
                verticalLineToRelative(80f)
                horizontalLineToRelative(80f)
                close()
            }
        }.build()

        return _PhosphorSelectionAll!!
    }

private var _PhosphorSelectionAll: ImageVector? = null