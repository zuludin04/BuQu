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

val PhosphorLinkSimple: ImageVector
    get() {
        if (_PhosphorLinkSimple != null) return _PhosphorLinkSimple!!

        _PhosphorLinkSimple = ImageVector.Builder(
            name = "link-simple",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(165.66f, 90.34f)
                arcToRelative(8f, 8f, 0f, false, true, 0f, 11.32f)
                lineToRelative(-64f, 64f)
                arcToRelative(8f, 8f, 0f, false, true, -11.32f, -11.32f)
                lineToRelative(64f, -64f)
                arcTo(8f, 8f, 0f, false, true, 165.66f, 90.34f)
                close()
                moveTo(215.6f, 40.4f)
                arcToRelative(56f, 56f, 0f, false, false, -79.2f, 0f)
                lineTo(106.34f, 70.45f)
                arcToRelative(8f, 8f, 0f, false, false, 11.32f, 11.32f)
                lineToRelative(30.06f, -30f)
                arcToRelative(40f, 40f, 0f, false, true, 56.57f, 56.56f)
                lineToRelative(-30.07f, 30.06f)
                arcToRelative(8f, 8f, 0f, false, false, 11.31f, 11.32f)
                lineTo(215.6f, 119.6f)
                arcToRelative(56f, 56f, 0f, false, false, 0f, -79.2f)
                close()
                moveTo(138.34f, 174.22f)
                lineToRelative(-30.06f, 30.06f)
                arcToRelative(40f, 40f, 0f, true, true, -56.56f, -56.57f)
                lineToRelative(30.05f, -30.05f)
                arcToRelative(8f, 8f, 0f, false, false, -11.32f, -11.32f)
                lineTo(40.4f, 136.4f)
                arcToRelative(56f, 56f, 0f, false, false, 79.2f, 79.2f)
                lineToRelative(30.06f, -30.07f)
                arcToRelative(8f, 8f, 0f, false, false, -11.32f, -11.31f)
                close()
            }
        }.build()

        return _PhosphorLinkSimple!!
    }

private var _PhosphorLinkSimple: ImageVector? = null