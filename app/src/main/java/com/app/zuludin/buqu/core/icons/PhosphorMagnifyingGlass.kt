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

val PhosphorMagnifyingGlass: ImageVector
    get() {
        if (_PhosphorMagnifyingGlass != null) return _PhosphorMagnifyingGlass!!

        _PhosphorMagnifyingGlass = ImageVector.Builder(
            name = "magnifying-glass",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(229.66f, 218.34f)
                lineToRelative(-50.07f, -50.06f)
                arcToRelative(88.11f, 88.11f, 0f, true, false, -11.31f, 11.31f)
                lineToRelative(50.06f, 50.07f)
                arcToRelative(8f, 8f, 0f, false, false, 11.32f, -11.32f)
                close()
                moveTo(40f, 112f)
                arcToRelative(72f, 72f, 0f, true, true, 72f, 72f)
                arcTo(72.08f, 72.08f, 0f, false, true, 40f, 112f)
                close()
            }
        }.build()

        return _PhosphorMagnifyingGlass!!
    }

private var _PhosphorMagnifyingGlass: ImageVector? = null