package com.app.zuludin.buqu

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.app.zuludin.buqu.ui.theme.BuQuTheme
import com.app.zuludin.buqu.util.VoiceToTextParser
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val voiceToTextParser by lazy {
        VoiceToTextParser(application)
    }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)

            MainContent(
                hasPermission = cameraPermissionState.status.isGranted,
                onRequestPermission = cameraPermissionState::launchPermissionRequest
            )
//            var canRecord by remember {
//                mutableStateOf(false)
//            }
//
//            val recordAudioLauncher = rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.RequestPermission(),
//                onResult = { isGranted ->
//                    canRecord = isGranted
//                }
//            )
//
//            LaunchedEffect(key1 = recordAudioLauncher) {
//                recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
//            }
//
//            val state by voiceToTextParser.state.collectAsState()
//
//            BuQuTheme {
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                    floatingActionButton = {
//                        FloatingActionButton(onClick = {
//                            if (state.isSpeaking) {
//                                voiceToTextParser.stopListening()
//                            } else {
//                                voiceToTextParser.startListening(languageCode = "id")
//                            }
//                        }) {
//                            AnimatedContent(
//                                targetState = state.isSpeaking,
//                                label = "Record Icon "
//                            ) { isSpeaking ->
//                                if (isSpeaking) {
//                                    Icon(
//                                        imageVector = Icons.Rounded.Clear,
//                                        contentDescription = null
//                                    )
//                                } else {
//                                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
//                                }
//                            }
//                        }
//                    }
//                ) { innerPadding ->
////                    HomeScreen(
////                        modifier = Modifier
////                            .padding(innerPadding)
////                    )
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding)
//                    ) {
//                        AnimatedContent(
//                            label = "Speech Recognizer Button",
//                            targetState = state.isSpeaking
//                        ) { isSpeaking ->
//                            if (isSpeaking) {
//                                Text(text = "Speaking ...")
//                            } else {
//                                Text(text = state.spokenText.ifEmpty { "Click on mic to record audio" })
//                            }
//                        }
//                    }
//                }
//            }
        }
    }
}

@Composable
private fun MainContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {

    if (hasPermission) {
        CameraScreen()
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}
