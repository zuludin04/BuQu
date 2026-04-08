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

val PhosphorMicrophone: ImageVector
    get() {
        if (_PhosphorMicrophone != null) return _PhosphorMicrophone!!
        
        _PhosphorMicrophone = ImageVector.Builder(
            name = "microphone",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(128f, 176f)
                arcToRelative(48.05f, 48.05f, 0f, false, false, 48f, -48f)
                verticalLineTo(64f)
                arcToRelative(48f, 48f, 0f, false, false, -96f, 0f)
                verticalLineToRelative(64f)
                arcTo(48.05f, 48.05f, 0f, false, false, 128f, 176f)
                close()
                moveTo(96f, 64f)
                arcToRelative(32f, 32f, 0f, false, true, 64f, 0f)
                verticalLineToRelative(64f)
                arcToRelative(32f, 32f, 0f, false, true, -64f, 0f)
                close()
                moveToRelative(40f, 143.6f)
                verticalLineTo(240f)
                arcToRelative(8f, 8f, 0f, false, true, -16f, 0f)
                verticalLineTo(207.6f)
                arcTo(80.11f, 80.11f, 0f, false, true, 48f, 128f)
                arcToRelative(8f, 8f, 0f, false, true, 16f, 0f)
                arcToRelative(64f, 64f, 0f, false, false, 128f, 0f)
                arcToRelative(8f, 8f, 0f, false, true, 16f, 0f)
                arcTo(80.11f, 80.11f, 0f, false, true, 136f, 207.6f)
                close()
            }
        }.build()
        
        return _PhosphorMicrophone!!
    }

private var _PhosphorMicrophone: ImageVector? = null