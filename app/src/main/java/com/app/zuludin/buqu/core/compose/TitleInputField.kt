package com.app.zuludin.buqu.core.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TitleInputField(
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    value: String,
    onChanged: (String) -> Unit,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    keyboardAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        modifier = modifier.padding(vertical = 4.dp),
        value = value,
        singleLine = singleLine,
        label = {
            Text(
                text = label,
                color = Color.Gray,
            )
        },
        onValueChange = { v ->
            onChanged(v)
        },
        placeholder = {
            Text(
                text = label,
                color = Color.Gray,
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardAction,
    )
}