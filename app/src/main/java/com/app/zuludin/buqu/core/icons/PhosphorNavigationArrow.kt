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

val PhosphorNavigationArrow: ImageVector
    get() {
        if (_PhosphorNavigationArrow != null) return _PhosphorNavigationArrow!!

        _PhosphorNavigationArrow = ImageVector.Builder(
            name = "navigation-arrow",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(237.33f, 106.21f)
                lineTo(61.41f, 41f)
                lineToRelative(-0.16f, -0.05f)
                arcTo(16f, 16f, 0f, false, false, 40.9f, 61.25f)
                arcToRelative(1f, 1f, 0f, false, false, 0.05f, 0.16f)
                lineToRelative(65.26f, 175.92f)
                arcTo(15.77f, 15.77f, 0f, false, false, 121.28f, 248f)
                horizontalLineToRelative(0.3f)
                arcToRelative(15.77f, 15.77f, 0f, false, false, 15f, -11.29f)
                lineToRelative(0.06f, -0.2f)
                lineToRelative(21.84f, -78f)
                lineToRelative(78f, -21.84f)
                lineToRelative(0.2f, -0.06f)
                arcToRelative(16f, 16f, 0f, false, false, 0.62f, -30.38f)
                close()
                moveTo(149.84f, 144.3f)
                arcToRelative(8f, 8f, 0f, false, false, -5.54f, 5.54f)
                lineTo(121.3f, 232f)
                lineToRelative(-0.06f, -0.17f)
                lineTo(56f, 56f)
                lineToRelative(175.82f, 65.22f)
                lineToRelative(0.16f, 0.06f)
                close()
            }
        }.build()

        return _PhosphorNavigationArrow!!
    }

private var _PhosphorNavigationArrow: ImageVector? = null