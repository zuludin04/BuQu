package com.app.zuludin.buqu.ui.addquote

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.roundToInt

@Composable
fun AddQuoteScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: AddQuoteViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onBack()
    }
    Scaffold { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
        ) {
            Toolbar(onBack = onBack, onSave = viewModel::saveQuote)
            TitleInputField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                label = "Quote",
                capitalization = KeyboardCapitalization.Sentences,
                value = uiState.quote,
                onChanged = viewModel::updateQuote
            )
            Row {
                TitleInputField(
                    modifier = Modifier.weight(2f),
                    label = "Book",
                    capitalization = KeyboardCapitalization.Words,
                    value = uiState.book,
                    onChanged = viewModel::updateBook
                )
                TitleInputField(
                    modifier = Modifier.weight(1f),
                    label = "Page",
                    keyboardType = KeyboardType.Number,
                    value = uiState.page.toString(),
                    onChanged = viewModel::updatePage
                )
            }
            TitleInputField(
                modifier = Modifier.fillMaxWidth(),
                label = "Author",
                capitalization = KeyboardCapitalization.Words,
                value = uiState.author,
                onChanged = viewModel::updateAuthor
            )
        }
    }

    LaunchedEffect(uiState.isQuoteSaved) {
        if (uiState.isQuoteSaved) {
            onBack()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Toolbar(onBack: () -> Unit, onSave: () -> Unit) {
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = {},
        actions = {
            FilledTonalButton(
                modifier = Modifier.padding(end = 6.dp),
                onClick = onSave,
            ) {
                Text(text = "Save")
            }
        },
        navigationIcon = {
            FilledTonalIconButton(
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                onClick = onBack,
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        }
    )
}

@Composable
private fun TitleInputField(
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    value: String,
    onChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        singleLine = singleLine,
        label = {
            Text(
                text = label,
                color = Color.Gray,
            )
        },
        onValueChange = onChanged,
        placeholder = {
            Text(
                text = label,
                color = Color.Gray,
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            keyboardType = keyboardType,
        )
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun SwipeableSample() {
    val width = 96.dp
    val squareSize = LocalConfiguration.current.screenWidthDp.dp

    val swipeableState = rememberSwipeableState(1)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal,
            )
            .background(Color.LightGray)
    ) {
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .size(48.dp)
                .background(Color.DarkGray)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableVoiceNoteComponent() {
    // Track drag offset state
    var offsetX by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var swipeable by remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current

    // Animate swipe back to initial position
    val animatedOffsetX = remember { Animatable(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.DarkGray, shape = CircleShape)
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        swipeable = true
                        offsetX = 0f
                    },
                    onDrag = { change, dragAmount ->
                        if (swipeable) {
                            change.consume() // consume the touch event
                            offsetX += dragAmount.x // update drag offset
                        }
                    },
                    onDragEnd = {
                        offsetX = 0f
                        swipeable = false
                        // Reset the drag offset with animation
                    }
                )
            }
            .offset { IntOffset(offsetX.toInt(), 0) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // The voice note play button
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier
                    .combinedClickable(
                        onClick = {
                            Toast
                                .makeText(context, "Click", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onLongClick = {
                            swipeable = true
                            Toast
                                .makeText(context, "Long Click", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                    .size(40.dp)
            )

            // The drag icon or voice note content
            Text(
                text = "Swipe to delete",
                color = Color.White,
                modifier = Modifier
                    .offset { IntOffset(animatedOffsetX.value.toInt(), 0) }
                    .padding(horizontal = 16.dp)
            )
        }
    }
}