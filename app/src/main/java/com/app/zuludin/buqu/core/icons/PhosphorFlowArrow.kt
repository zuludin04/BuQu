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

val PhosphorFlowArrow: ImageVector
    get() {
        if (_PhosphorFlowArrow != null) return _PhosphorFlowArrow!!

        _PhosphorFlowArrow = ImageVector.Builder(
            name = "flow-arrow",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(245.66f, 74.34f)
                lineToRelative(-32f, -32f)
                arcToRelative(8f, 8f, 0f, false, false, -11.32f, 11.32f)
                lineTo(220.69f, 72f)
                horizontalLineTo(208f)
                curveToRelative(-49.33f, 0f, -61.05f, 28.12f, -71.38f, 52.92f)
                curveToRelative(-9.38f, 22.51f, -16.92f, 40.59f, -49.48f, 42.84f)
                arcToRelative(40f, 40f, 0f, true, false, 0.1f, 16f)
                curveToRelative(43.26f, -2.65f, 54.34f, -29.15f, 64.14f, -52.69f)
                curveTo(161.41f, 107f, 169.33f, 88f, 208f, 88f)
                horizontalLineToRelative(12.69f)
                lineToRelative(-18.35f, 18.34f)
                arcToRelative(8f, 8f, 0f, false, false, 11.32f, 11.32f)
                lineToRelative(32f, -32f)
                arcTo(8f, 8f, 0f, false, false, 245.66f, 74.34f)
                close()
                moveTo(48f, 200f)
                arcToRelative(24f, 24f, 0f, true, true, 24f, -24f)
                arcTo(24f, 24f, 0f, false, true, 48f, 200f)
                close()
            }
        }.build()

        return _PhosphorFlowArrow!!
    }

private var _PhosphorFlowArrow: ImageVector? = null