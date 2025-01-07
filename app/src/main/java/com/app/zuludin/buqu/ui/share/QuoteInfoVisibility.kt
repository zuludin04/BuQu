package com.app.zuludin.buqu.ui.share

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QuoteInfoVisibility(
    modifier: Modifier = Modifier,
    onBookVisibility: (Boolean) -> Unit,
    onAuthorVisibility: (Boolean) -> Unit
) {
    var authorVisibility by remember { mutableStateOf(true) }
    var bookVisibility by remember { mutableStateOf(true) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = "Visibility", style = MaterialTheme.typography.caption)
        Row {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = authorVisibility,
                    onCheckedChange = { isChecked ->
                        authorVisibility = isChecked
                        onAuthorVisibility(isChecked)
                    }
                )
                Text("Author")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = bookVisibility,
                    onCheckedChange = { isChecked ->
                        bookVisibility = isChecked
                        onBookVisibility(isChecked)
                    }
                )
                Text("Book")
            }
        }
    }
}

@Preview
@Composable
private fun QuoteInfoVisibilityPreview() {
    QuoteInfoVisibility(onAuthorVisibility = {}, onBookVisibility = {})
}