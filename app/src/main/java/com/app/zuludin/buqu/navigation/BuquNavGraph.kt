package com.app.zuludin.buqu.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.BOOK_ID_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.QUOTE_ID_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.SHARE_AUTHOR_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.SHARE_BOOK_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.SHARE_QUOTE_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.TITLE_ARG
import com.app.zuludin.buqu.ui.board.editor.BoardEditorScreen
import com.app.zuludin.buqu.ui.board.list.BoardListScreen
import com.app.zuludin.buqu.ui.book.list.BookScreen
import com.app.zuludin.buqu.ui.book.scan.CoverScanScreen
import com.app.zuludin.buqu.ui.book.search.BookSearchScreen
import com.app.zuludin.buqu.ui.book.upsert.UpsertBookScreen
import com.app.zuludin.buqu.ui.category.CategorySelectScreen
import com.app.zuludin.buqu.ui.quote.list.QuoteScreen
import com.app.zuludin.buqu.ui.quote.upsert.UpsertQuoteScreen
import com.app.zuludin.buqu.ui.settings.SettingsScreen
import com.app.zuludin.buqu.ui.share.ShareScreen

@Composable
fun BuquNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navActions: BuquNavigationActions = remember(navController) {
        BuquNavigationActions(navController)
    }
) {
    NavHost(
        navController,
        startDestination = BuquDestinations.QUOTES_ROUTE,
        enterTransition = { fadeIn(tween(0)) },
        exitTransition = { fadeOut(tween(0)) }
    ) {
        composable(BuquDestinations.QUOTES_ROUTE) {
            QuoteScreen(
                onQuoteClick = { navActions.navigateToUpsertQuote("Update Quote", it) },
            )
        }
        composable(BuquDestinations.BOARD_ROUTE) {
            BoardListScreen(onBoardClick = {
                navActions.navigateToBoardEditor(it)
            })
        }
        composable(BuquDestinations.SETTING_ROUTE) {
            SettingsScreen(onOpenCategorySelectScreen = { navActions.navigateToCategorySelect() })
        }

        composable(
            BuquDestinations.UPSERT_QUOTE_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.StringType },
                navArgument(QUOTE_ID_ARG) {
                    type = NavType.StringType; nullable = true
                })
        ) { entry ->
            val title = entry.arguments?.getString(TITLE_ARG)
            UpsertQuoteScreen(
                onBack = { navController.popBackStack() },
                topAppBarTitle = title ?: "",
                onShareQuote = {
                    navActions.navigateToShareQuote(
                        quote = it.quote, author = it.author, book = it.book
                    )
                })
        }

        composable(BuquDestinations.CATEGORY_SELECT_ROUTE) {
            CategorySelectScreen(onBack = { navController.popBackStack() })
        }

        composable(
            BuquDestinations.SHARE_ROUTE, arguments = listOf(
                navArgument(SHARE_QUOTE_ARG) { type = NavType.StringType },
                navArgument(SHARE_AUTHOR_ARG) { type = NavType.StringType },
                navArgument(SHARE_BOOK_ARG) { type = NavType.StringType },
            )
        ) {
            val quote = it.arguments?.getString(SHARE_QUOTE_ARG)
            val book = it.arguments?.getString(SHARE_BOOK_ARG)
            val author = it.arguments?.getString(SHARE_AUTHOR_ARG)

            ShareScreen(
                book = book ?: "",
                quote = quote ?: "",
                author = author ?: "",
                onBack = { navController.popBackStack() }
            )
        }

        composable(BuquDestinations.BOARD_EDITOR_ROUTE) {
            BoardEditorScreen(
                topAppBarTitle = "Board Editor",
                onBack = { navController.popBackStack() }
            )
        }

        composable(BuquDestinations.BOOKS_ROUTE) {
            BookScreen(
                onBookClick = { navActions.navigateToUpsertBook("Update Book", it) },
                onAddOnlineBookClick = { navActions.navigateToUpsertBook("Add Book", it) },
                onScanClick = { navActions.navigateToCoverScan() }
            )
        }

        composable(
            BuquDestinations.UPSERT_BOOK_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.StringType },
                navArgument(BOOK_ID_ARG) { type = NavType.StringType; nullable = true }
            )
        ) { entry ->
            val title = entry.arguments?.getString(TITLE_ARG)
            UpsertBookScreen(
                onBack = { navController.popBackStack() },
                onSearchWeb = { navActions.navigateToBookSearch() },
                topAppBarTitle = title ?: "",
                savedStateHandle = entry.savedStateHandle
            )
        }

        composable(BuquDestinations.BOOK_SEARCH_ROUTE) {
            BookSearchScreen(
                onBack = { navController.popBackStack() },
                onBookSelected = { book ->
                    // Return to the list first, then open Add flow with selected Google Books id.
                    navController.popBackStack()
                    navActions.navigateToUpsertBook("Add Book", book.bookId)
                }
            )
        }

        composable(BuquDestinations.COVER_SCAN_ROUTE) {
            CoverScanScreen(
                onBack = { navController.popBackStack() },
                onEditBook = { bookId -> navActions.navigateToUpsertBook("Add Book", bookId) },
                onSaved = { navController.popBackStack() }
            )
        }
    }
}

val bottomBarRoutes = setOf(
    BuquDestinations.QUOTES_ROUTE,
    BuquDestinations.BOARD_ROUTE,
    BuquDestinations.BOOKS_ROUTE,
    BuquDestinations.SETTING_ROUTE
)
