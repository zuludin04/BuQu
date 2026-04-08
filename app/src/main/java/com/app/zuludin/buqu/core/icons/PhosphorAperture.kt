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
package com.app.zuludin.buqu.core.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val PhosphorAperture: ImageVector
    get() {
        if (_PhosphorAperture != null) return _PhosphorAperture!!
        
        _PhosphorAperture = ImageVector.Builder(
            name = "aperture",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(201.54f, 54.46f)
                arcTo(104f, 104f, 0f, false, false, 54.46f, 201.54f)
                arcTo(104f, 104f, 0f, false, false, 201.54f, 54.46f)
                close()
                moveTo(190.23f, 65.78f)
                arcToRelative(88.18f, 88.18f, 0f, false, true, 11f, 13.48f)
                lineTo(167.55f, 119f)
                lineTo(139.63f, 40.78f)
                arcTo(87.34f, 87.34f, 0f, false, true, 190.23f, 65.78f)
                close()
                moveTo(155.59f, 133f)
                lineToRelative(-18.16f, 21.37f)
                lineToRelative(-27.59f, -5f)
                lineTo(100.41f, 123f)
                lineToRelative(18.16f, -21.37f)
                lineToRelative(27.59f, 5f)
                close()
                moveTo(65.77f, 65.78f)
                arcToRelative(87.34f, 87.34f, 0f, false, true, 56.66f, -25.59f)
                lineToRelative(17.51f, 49f)
                lineTo(58.3f, 74.32f)
                arcTo(88f, 88f, 0f, false, true, 65.77f, 65.78f)
                close()
                moveTo(46.65f, 161.54f)
                arcToRelative(88.41f, 88.41f, 0f, false, true, 2.53f, -72.62f)
                lineToRelative(51.21f, 9.35f)
                close()
                moveToRelative(19.12f, 28.68f)
                arcToRelative(88.18f, 88.18f, 0f, false, true, -11f, -13.48f)
                lineTo(88.45f, 137f)
                lineToRelative(27.92f, 78.18f)
                arcTo(87.34f, 87.34f, 0f, false, true, 65.77f, 190.22f)
                close()
                moveToRelative(124.46f, 0f)
                arcToRelative(87.34f, 87.34f, 0f, false, true, -56.66f, 25.59f)
                lineToRelative(-17.51f, -49f)
                lineToRelative(81.64f, 14.91f)
                arcTo(88f, 88f, 0f, false, true, 190.23f, 190.22f)
                close()
                moveToRelative(-34.62f, -32.49f)
                lineToRelative(53.74f, -63.27f)
                arcToRelative(88.41f, 88.41f, 0f, false, true, -2.53f, 72.62f)
                close()
            }
        }.build()
        
        return _PhosphorAperture!!
    }

private var _PhosphorAperture: ImageVector? = null