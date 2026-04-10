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

val PhosphorLineSegments: ImageVector
    get() {
        if (_PhosphorLineSegments != null) return _PhosphorLineSegments!!

        _PhosphorLineSegments = ImageVector.Builder(
            name = "line-segments",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(238.64f, 33.36f)
                arcToRelative(32f, 32f, 0f, false, false, -45.26f, 0f)
                horizontalLineToRelative(0f)
                arcToRelative(32f, 32f, 0f, false, false, 0f, 45.26f)
                curveToRelative(0.29f, 0.29f, 0.6f, 0.57f, 0.9f, 0.85f)
                lineToRelative(-26.63f, 49.46f)
                arcToRelative(32.19f, 32.19f, 0f, false, false, -23.9f, 3.5f)
                lineToRelative(-20.18f, -20.18f)
                arcToRelative(32f, 32f, 0f, false, false, -50.2f, -38.89f)
                horizontalLineToRelative(0f)
                arcToRelative(32f, 32f, 0f, false, false, 0f, 45.26f)
                curveToRelative(0.29f, 0.29f, 0.59f, 0.57f, 0.89f, 0.85f)
                lineTo(47.63f, 168.94f)
                arcToRelative(32f, 32f, 0f, false, false, -30.27f, 8.44f)
                horizontalLineToRelative(0f)
                arcToRelative(32f, 32f, 0f, true, false, 45.26f, 0f)
                curveToRelative(-0.29f, -0.29f, -0.6f, -0.57f, -0.9f, -0.85f)
                lineToRelative(26.63f, -49.46f)
                arcTo(32.4f, 32.4f, 0f, false, false, 96f, 128f)
                arcToRelative(32f, 32f, 0f, false, false, 16.25f, -4.41f)
                lineToRelative(20.18f, 20.18f)
                arcToRelative(32f, 32f, 0f, true, false, 50.2f, -6.38f)
                curveToRelative(-0.29f, -0.29f, -0.59f, -0.57f, -0.89f, -0.85f)
                lineToRelative(26.63f, -49.46f)
                arcTo(32.33f, 32.33f, 0f, false, false, 216f, 88f)
                arcToRelative(32f, 32f, 0f, false, false, 22.63f, -54.62f)
                close()
                moveTo(51.3f, 211.33f)
                arcToRelative(16f, 16f, 0f, false, true, -22.63f, -22.64f)
                horizontalLineToRelative(0f)
                arcTo(16f, 16f, 0f, true, true, 51.3f, 211.33f)
                close()
                moveToRelative(33.38f, -104f)
                arcToRelative(16f, 16f, 0f, false, true, 0f, -22.63f)
                horizontalLineToRelative(0f)
                arcToRelative(16f, 16f, 0f, true, true, 0f, 22.63f)
                close()
                moveToRelative(86.64f, 64f)
                arcToRelative(16f, 16f, 0f, false, true, -22.63f, -22.63f)
                horizontalLineToRelative(0f)
                arcToRelative(16f, 16f, 0f, false, true, 22.63f, 22.63f)
                close()
                moveToRelative(56f, -104f)
                arcTo(16f, 16f, 0f, true, true, 204.7f, 44.67f)
                horizontalLineToRelative(0f)
                arcToRelative(16f, 16f, 0f, false, true, 22.63f, 22.64f)
                close()
            }
        }.build()

        return _PhosphorLineSegments!!
    }

private var _PhosphorLineSegments: ImageVector? = null