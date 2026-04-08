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

val PhosphorMoon: ImageVector
    get() {
        if (_PhosphorMoon != null) return _PhosphorMoon!!
        
        _PhosphorMoon = ImageVector.Builder(
            name = "moon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(235.54f, 150.21f)
                arcToRelative(104.84f, 104.84f, 0f, false, true, -37f, 52.91f)
                arcTo(104f, 104f, 0f, false, true, 32f, 120f)
                arcTo(103.09f, 103.09f, 0f, false, true, 52.88f, 57.48f)
                arcToRelative(104.84f, 104.84f, 0f, false, true, 52.91f, -37f)
                arcToRelative(8f, 8f, 0f, false, true, 10f, 10f)
                arcToRelative(88.08f, 88.08f, 0f, false, false, 109.8f, 109.8f)
                arcToRelative(8f, 8f, 0f, false, true, 10f, 10f)
                close()
            }
        }.build()
        
        return _PhosphorMoon!!
    }

private var _PhosphorMoon: ImageVector? = null