package com.app.zuludin.buqu.ui.quote

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.zuludin.buqu.domain.models.Quote
import com.app.zuludin.buqu.ui.addquote.AddQuoteScreen
import com.app.zuludin.buqu.util.LoadingContent
import kotlin.random.Random

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: QuoteViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(scaffoldState = scaffoldState, modifier = modifier.fillMaxSize()) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            HotContent(
                loading = uiState.isLoading,
                quotes = uiState.quotes,
                onQuoteClick = { }
            )
            FabContainer(
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }

        uiState.userMessage?.let { message ->
            LaunchedEffect(scaffoldState, viewModel, message) {
                scaffoldState.snackbarHostState.showSnackbar(message)
                viewModel.snackbarMessageShown()
            }
        }
    }
}

@Composable
private fun FabContainer(modifier: Modifier = Modifier) {
    var containerState by remember {
        mutableStateOf(ContainerState.Fab)
    }
    val transition = updateTransition(containerState, label = "container transform")
    val animatedColor by transition.animateColor(label = "color") { state ->
        when (state) {
            ContainerState.Fab -> MaterialTheme.colorScheme.primaryContainer
            ContainerState.Fullscreen -> MaterialTheme.colorScheme.surface
        }
    }
    val cornerRadius by transition.animateDp(
        label = "corner radius",
        transitionSpec = {
            when (targetState) {
                ContainerState.Fab -> tween(durationMillis = 400, easing = EaseOutCubic)
                ContainerState.Fullscreen -> tween(durationMillis = 200, easing = EaseOutCubic)
            }
        }
    ) { state ->
        when (state) {
            ContainerState.Fab -> 22.dp
            ContainerState.Fullscreen -> 0.dp
        }
    }
    val elevation by transition.animateDp(
        label = "elevation",
        transitionSpec = {
            when (targetState) {
                ContainerState.Fab -> tween(durationMillis = 400, easing = EaseOutCubic)
                ContainerState.Fullscreen -> tween(durationMillis = 200, easing = EaseOutCubic)
            }
        }
    ) { state ->
        when (state) {
            ContainerState.Fab -> 6.dp
            ContainerState.Fullscreen -> 0.dp
        }
    }
    val padding by transition.animateDp(label = "padding") { state ->
        when (state) {
            ContainerState.Fab -> 16.dp
            ContainerState.Fullscreen -> 0.dp
        }
    }

    transition.AnimatedContent(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(padding)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius)
            )
            .drawBehind { drawRect(animatedColor) },
        transitionSpec = {
            (
                    fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                            scaleIn(
                                initialScale = 0.92f,
                                animationSpec = tween(220, delayMillis = 90)
                            )
                    )
                .togetherWith(fadeOut(animationSpec = tween(90)))
                .using(
                    SizeTransform(clip = false, sizeAnimationSpec = { _, _ ->
                        tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    })
                )
        }
    ) { state ->
        when (state) {
            ContainerState.Fab -> {
                Fab(modifier = Modifier, onClick = { containerState = ContainerState.Fullscreen })
            }

            ContainerState.Fullscreen -> AddQuoteScreen(
                modifier = Modifier,
                onBack = { containerState = ContainerState.Fab }
            )
        }
    }
}

@Composable
private fun HotContent(
    loading: Boolean,
    quotes: List<Quote>,
    onQuoteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        LoadingContent(loading = loading, empty = quotes.isEmpty() && !loading, emptyContent = {}) {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp
                ) {
                    items(quotes) {
                        HotTake(hotTake = it) {
                            onQuoteClick(it.quoteId)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HotTake(hotTake: Quote, onClick: () -> Unit) {
    val color = Color(Random.nextLong(0xffffffff)).copy(alpha = 0.1f)
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .background(color)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = hotTake.book,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = hotTake.quote,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = hotTake.author,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun Fab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = 76.dp,
                minHeight = 76.dp,
            )
            .clickable(
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = rememberVectorPainter(Icons.Filled.Add),
            contentDescription = null,
        )
    }
}

enum class ContainerState {
    Fab,
    Fullscreen,
}