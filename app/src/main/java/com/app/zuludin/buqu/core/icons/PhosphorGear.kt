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

val PhosphorGear: ImageVector
    get() {
        if (_PhosphorGear != null) return _PhosphorGear!!
        
        _PhosphorGear = ImageVector.Builder(
            name = "gear",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(128f, 80f)
                arcToRelative(48f, 48f, 0f, true, false, 48f, 48f)
                arcTo(48.05f, 48.05f, 0f, false, false, 128f, 80f)
                close()
                moveToRelative(0f, 80f)
                arcToRelative(32f, 32f, 0f, true, true, 32f, -32f)
                arcTo(32f, 32f, 0f, false, true, 128f, 160f)
                close()
                moveToRelative(88f, -29.84f)
                quadToRelative(0.06f, -2.16f, 0f, -4.32f)
                lineToRelative(14.92f, -18.64f)
                arcToRelative(8f, 8f, 0f, false, false, 1.48f, -7.06f)
                arcToRelative(107.21f, 107.21f, 0f, false, false, -10.88f, -26.25f)
                arcToRelative(8f, 8f, 0f, false, false, -6f, -3.93f)
                lineToRelative(-23.72f, -2.64f)
                quadToRelative(-1.48f, -1.56f, -3f, -3f)
                lineTo(186f, 40.54f)
                arcToRelative(8f, 8f, 0f, false, false, -3.94f, -6f)
                arcToRelative(107.71f, 107.71f, 0f, false, false, -26.25f, -10.87f)
                arcToRelative(8f, 8f, 0f, false, false, -7.06f, 1.49f)
                lineTo(130.16f, 40f)
                quadTo(128f, 40f, 125.84f, 40f)
                lineTo(107.2f, 25.11f)
                arcToRelative(8f, 8f, 0f, false, false, -7.06f, -1.48f)
                arcTo(107.6f, 107.6f, 0f, false, false, 73.89f, 34.51f)
                arcToRelative(8f, 8f, 0f, false, false, -3.93f, 6f)
                lineTo(67.32f, 64.27f)
                quadToRelative(-1.56f, 1.49f, -3f, 3f)
                lineTo(40.54f, 70f)
                arcToRelative(8f, 8f, 0f, false, false, -6f, 3.94f)
                arcToRelative(107.71f, 107.71f, 0f, false, false, -10.87f, 26.25f)
                arcToRelative(8f, 8f, 0f, false, false, 1.49f, 7.06f)
                lineTo(40f, 125.84f)
                quadTo(40f, 128f, 40f, 130.16f)
                lineTo(25.11f, 148.8f)
                arcToRelative(8f, 8f, 0f, false, false, -1.48f, 7.06f)
                arcToRelative(107.21f, 107.21f, 0f, false, false, 10.88f, 26.25f)
                arcToRelative(8f, 8f, 0f, false, false, 6f, 3.93f)
                lineToRelative(23.72f, 2.64f)
                quadToRelative(1.49f, 1.56f, 3f, 3f)
                lineTo(70f, 215.46f)
                arcToRelative(8f, 8f, 0f, false, false, 3.94f, 6f)
                arcToRelative(107.71f, 107.71f, 0f, false, false, 26.25f, 10.87f)
                arcToRelative(8f, 8f, 0f, false, false, 7.06f, -1.49f)
                lineTo(125.84f, 216f)
                quadToRelative(2.16f, 0.06f, 4.32f, 0f)
                lineToRelative(18.64f, 14.92f)
                arcToRelative(8f, 8f, 0f, false, false, 7.06f, 1.48f)
                arcToRelative(107.21f, 107.21f, 0f, false, false, 26.25f, -10.88f)
                arcToRelative(8f, 8f, 0f, false, false, 3.93f, -6f)
                lineToRelative(2.64f, -23.72f)
                quadToRelative(1.56f, -1.48f, 3f, -3f)
                lineTo(215.46f, 186f)
                arcToRelative(8f, 8f, 0f, false, false, 6f, -3.94f)
                arcToRelative(107.71f, 107.71f, 0f, false, false, 10.87f, -26.25f)
                arcToRelative(8f, 8f, 0f, false, false, -1.49f, -7.06f)
                close()
                moveToRelative(-16.1f, -6.5f)
                arcToRelative(73.93f, 73.93f, 0f, false, true, 0f, 8.68f)
                arcToRelative(8f, 8f, 0f, false, false, 1.74f, 5.48f)
                lineToRelative(14.19f, 17.73f)
                arcToRelative(91.57f, 91.57f, 0f, false, true, -6.23f, 15f)
                lineTo(187f, 173.11f)
                arcToRelative(8f, 8f, 0f, false, false, -5.1f, 2.64f)
                arcToRelative(74.11f, 74.11f, 0f, false, true, -6.14f, 6.14f)
                arcToRelative(8f, 8f, 0f, false, false, -2.64f, 5.1f)
                lineToRelative(-2.51f, 22.58f)
                arcToRelative(91.32f, 91.32f, 0f, false, true, -15f, 6.23f)
                lineToRelative(-17.74f, -14.19f)
                arcToRelative(8f, 8f, 0f, false, false, -5f, -1.75f)
                horizontalLineToRelative(-0.48f)
                arcToRelative(73.93f, 73.93f, 0f, false, true, -8.68f, 0f)
                arcToRelative(8f, 8f, 0f, false, false, -5.48f, 1.74f)
                lineTo(100.45f, 215.8f)
                arcToRelative(91.57f, 91.57f, 0f, false, true, -15f, -6.23f)
                lineTo(82.89f, 187f)
                arcToRelative(8f, 8f, 0f, false, false, -2.64f, -5.1f)
                arcToRelative(74.11f, 74.11f, 0f, false, true, -6.14f, -6.14f)
                arcToRelative(8f, 8f, 0f, false, false, -5.1f, -2.64f)
                lineTo(46.43f, 170.6f)
                arcToRelative(91.32f, 91.32f, 0f, false, true, -6.23f, -15f)
                lineToRelative(14.19f, -17.74f)
                arcToRelative(8f, 8f, 0f, false, false, 1.74f, -5.48f)
                arcToRelative(73.93f, 73.93f, 0f, false, true, 0f, -8.68f)
                arcToRelative(8f, 8f, 0f, false, false, -1.74f, -5.48f)
                lineTo(40.2f, 100.45f)
                arcToRelative(91.57f, 91.57f, 0f, false, true, 6.23f, -15f)
                lineTo(69f, 82.89f)
                arcToRelative(8f, 8f, 0f, false, false, 5.1f, -2.64f)
                arcToRelative(74.11f, 74.11f, 0f, false, true, 6.14f, -6.14f)
                arcTo(8f, 8f, 0f, false, false, 82.89f, 69f)
                lineTo(85.4f, 46.43f)
                arcToRelative(91.32f, 91.32f, 0f, false, true, 15f, -6.23f)
                lineToRelative(17.74f, 14.19f)
                arcToRelative(8f, 8f, 0f, false, false, 5.48f, 1.74f)
                arcToRelative(73.93f, 73.93f, 0f, false, true, 8.68f, 0f)
                arcToRelative(8f, 8f, 0f, false, false, 5.48f, -1.74f)
                lineTo(155.55f, 40.2f)
                arcToRelative(91.57f, 91.57f, 0f, false, true, 15f, 6.23f)
                lineTo(173.11f, 69f)
                arcToRelative(8f, 8f, 0f, false, false, 2.64f, 5.1f)
                arcToRelative(74.11f, 74.11f, 0f, false, true, 6.14f, 6.14f)
                arcToRelative(8f, 8f, 0f, false, false, 5.1f, 2.64f)
                lineToRelative(22.58f, 2.51f)
                arcToRelative(91.32f, 91.32f, 0f, false, true, 6.23f, 15f)
                lineToRelative(-14.19f, 17.74f)
                arcTo(8f, 8f, 0f, false, false, 199.87f, 123.66f)
                close()
            }
        }.build()
        
        return _PhosphorGear!!
    }

private var _PhosphorGear: ImageVector? = null