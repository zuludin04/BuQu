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

val PhosphorLightbulb: ImageVector
    get() {
        if (_PhosphorLightbulb != null) return _PhosphorLightbulb!!

        _PhosphorLightbulb = ImageVector.Builder(
            name = "lightbulb",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(176f, 232f)
                arcToRelative(8f, 8f, 0f, false, true, -8f, 8f)
                horizontalLineTo(88f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, -16f)
                horizontalLineToRelative(80f)
                arcTo(8f, 8f, 0f, false, true, 176f, 232f)
                close()
                moveToRelative(40f, -128f)
                arcToRelative(87.55f, 87.55f, 0f, false, true, -33.64f, 69.21f)
                arcTo(16.24f, 16.24f, 0f, false, false, 176f, 186f)
                verticalLineToRelative(6f)
                arcToRelative(16f, 16f, 0f, false, true, -16f, 16f)
                horizontalLineTo(96f)
                arcToRelative(16f, 16f, 0f, false, true, -16f, -16f)
                verticalLineToRelative(-6f)
                arcToRelative(16f, 16f, 0f, false, false, -6.23f, -12.66f)
                arcTo(87.59f, 87.59f, 0f, false, true, 40f, 104.49f)
                curveTo(39.74f, 56.83f, 78.26f, 17.14f, 125.88f, 16f)
                arcTo(88f, 88f, 0f, false, true, 216f, 104f)
                close()
                moveToRelative(-16f, 0f)
                arcToRelative(72f, 72f, 0f, false, false, -73.74f, -72f)
                curveToRelative(-39f, 0.92f, -70.47f, 33.39f, -70.26f, 72.39f)
                arcToRelative(71.65f, 71.65f, 0f, false, false, 27.64f, 56.3f)
                arcTo(32f, 32f, 0f, false, true, 96f, 186f)
                verticalLineToRelative(6f)
                horizontalLineToRelative(64f)
                verticalLineToRelative(-6f)
                arcToRelative(32.15f, 32.15f, 0f, false, true, 12.47f, -25.35f)
                arcTo(71.65f, 71.65f, 0f, false, false, 200f, 104f)
                close()
                moveToRelative(-16.11f, -9.34f)
                arcToRelative(57.6f, 57.6f, 0f, false, false, -46.56f, -46.55f)
                arcToRelative(8f, 8f, 0f, false, false, -2.66f, 15.78f)
                curveToRelative(16.57f, 2.79f, 30.63f, 16.85f, 33.44f, 33.45f)
                arcTo(8f, 8f, 0f, false, false, 176f, 104f)
                arcToRelative(9f, 9f, 0f, false, false, 1.35f, -0.11f)
                arcTo(8f, 8f, 0f, false, false, 183.89f, 94.66f)
                close()
            }
        }.build()

        return _PhosphorLightbulb!!
    }

private var _PhosphorLightbulb: ImageVector? = null