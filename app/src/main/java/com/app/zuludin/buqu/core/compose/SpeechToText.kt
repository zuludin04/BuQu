package com.app.zuludin.buqu.core.compose

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.app.zuludin.buqu.core.icons.PhosphorMicrophone
import com.app.zuludin.buqu.core.utils.SpeechRecognizerContract

@Composable
fun SpeechToText(onSpeechResult: (String) -> Unit) {
    val speechRecognizerLauncher =
        rememberLauncherForActivityResult(contract = SpeechRecognizerContract(), onResult = {
            val result = it.toString()
            val trim = it.toString().substring(1, result.length - 1)
            onSpeechResult(trim)
        })

    IconButton(
        onClick = { speechRecognizerLauncher.launch(Unit) },
        content = { Icon(PhosphorMicrophone, null) }
    )
}