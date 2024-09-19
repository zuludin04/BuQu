package com.app.zuludin.buqu

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.zuludin.buqu.navigation.BuquNavGraph
import com.app.zuludin.buqu.navigation.BuquNavigation
import com.app.zuludin.buqu.ui.theme.BuQuTheme
import com.app.zuludin.buqu.util.VoiceToTextParser
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
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
            val cameraPermissionState: PermissionState =
                rememberPermissionState(Manifest.permission.CAMERA)

//            MainContent(
//                hasPermission = cameraPermissionState.status.isGranted,
//                onRequestPermission = cameraPermissionState::launchPermissionRequest
//            )
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
            BuQuTheme {
                val navController: NavHostController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                var buttonsVisible by remember { mutableStateOf(true) }

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    if (buttonsVisible) {
                        NavigationBar {
                            NavigationBarItem(selected = currentRoute == BuquNavigation.QUOTE_NAVIGATION,
                                onClick = {
                                    navController.navigate(BuquNavigation.QUOTE_NAVIGATION)
                                    buttonsVisible = true
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text("Quote")
                                })
                            NavigationBarItem(selected = currentRoute == BuquNavigation.BOOK_NAVIGATION,
                                onClick = {
                                    navController.navigate(BuquNavigation.BOOK_NAVIGATION)
                                    buttonsVisible = true
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text("Book")
                                })
                            NavigationBarItem(selected = currentRoute == BuquNavigation.CATEGORY_NAVIGATION,
                                onClick = {
                                    navController.navigate(BuquNavigation.CATEGORY_NAVIGATION)
                                    buttonsVisible = true
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.AddCircle,
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text("Category")
                                })
                            NavigationBarItem(selected = currentRoute == BuquNavigation.SETTING_NAVIGATION,
                                onClick = {
                                    navController.navigate(BuquNavigation.SETTING_NAVIGATION)
                                    buttonsVisible = true
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text("Settings")
                                })
                        }
                    }
                }
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
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        BuquNavGraph(
                            navController,
                            onOpenDetail = {
                                buttonsVisible = false
                            },
                            onCloseDetail = {
                                buttonsVisible = true
                            },
                        )
                    }
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
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    hasPermission: Boolean, onRequestPermission: () -> Unit
) {

    if (hasPermission) {
        CameraScreen()
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}
