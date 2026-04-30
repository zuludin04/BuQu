package com.app.zuludin.buqu.core.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.zuludin.buqu.domain.models.Book

@Composable
fun BookSpinner(
    modifier: Modifier = Modifier,
    currentBookId: String?,
    books: List<Book>,
    onSelectBook: (Book?) -> Unit,
) {
    val selectedBook = books.find { it.bookId == currentBookId }
    var expanded by remember { mutableStateOf(false) }
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val iconRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "iconRotation"
    )

    Box(
        modifier = modifier
            .border(
                border = BorderStroke(1.dp, SolidColor(Color.Gray)),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedBook != null && selectedBook.cover.isNotBlank()) {
                AsyncImage(
                    model = selectedBook.cover,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp, 48.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp, 48.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                )
            }
            
            Text(
                text = selectedBook?.title ?: "No Book Selected",
                color = textColor,
                style = TextStyle(color = MaterialTheme.colors.onBackground),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                "",
                tint = textColor,
                modifier = Modifier.rotate(iconRotation)
            )
        }

        DropdownMenu(
            modifier = Modifier.fillMaxWidth(0.9f),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSelectBook(null)
                }
            ) {
                Text("None", style = MaterialTheme.typography.body1)
            }
            
            books.forEach { book ->
                val isSelected = book.bookId == currentBookId
                val style = if (isSelected) {
                    MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                } else {
                    MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Normal,
                        color = textColor
                    )
                }

                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onSelectBook(book)
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (book.cover.isNotBlank()) {
                            AsyncImage(
                                model = book.cover,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp, 36.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(24.dp, 36.dp)
                                    .background(Color.LightGray, RoundedCornerShape(2.dp))
                            )
                        }
                        Text(
                            text = book.title,
                            style = style,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
