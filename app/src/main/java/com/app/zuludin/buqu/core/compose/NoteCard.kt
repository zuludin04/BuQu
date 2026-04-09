package com.app.zuludin.buqu.core.compose

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    backgroundColor: String,
    book: String,
    quote: String,
    author: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .neumorphicShadow(backgroundColor = Color(backgroundColor.toColorInt()))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = book,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = quote,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = author,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewNoteCard() {
    NoteCard(
        quote = "Hallo",
        author = "123",
        book = "BCD",
        backgroundColor = "#03A9F4",
        onClick = {}
    )
}

fun Modifier.neumorphicShadow(
    radius: Dp = 6.dp,
    backgroundColor: Color,
): Modifier = this
    .drawBehind {
        val cornerRadius = radius.toPx()
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    left = 6f,
                    top = 0f,
                    right = size.width + 6,
                    bottom = size.height + 6,
                    topLeftCornerRadius = CornerRadius.Zero,
                    topRightCornerRadius = CornerRadius.Zero,
                    bottomLeftCornerRadius = CornerRadius(cornerRadius),
                    bottomRightCornerRadius = CornerRadius(cornerRadius)
                )
            )
        }

        val cardPath = Path().apply {
            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    topLeftCornerRadius = CornerRadius.Zero,
                    topRightCornerRadius = CornerRadius.Zero,
                    bottomLeftCornerRadius = CornerRadius(cornerRadius),
                    bottomRightCornerRadius = CornerRadius(cornerRadius)
                )
            )
        }

        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint().apply {
                color = Color.Black.copy(alpha = 0.3f).toArgb()
                maskFilter = BlurMaskFilter(3.dp.toPx(), BlurMaskFilter.Blur.NORMAL)
            }
            canvas.nativeCanvas.drawPath(path.asAndroidPath(), paint)
        }

        drawPath(
            path = cardPath,
            color = backgroundColor,
        )
    }