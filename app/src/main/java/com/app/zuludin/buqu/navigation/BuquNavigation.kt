package com.app.zuludin.buqu.navigation

import androidx.navigation.NavController
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.QUOTE_ID_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.SHARE_AUTHOR_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.SHARE_BOOK_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.SHARE_QUOTE_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.TITLE_ARG
import com.app.zuludin.buqu.navigation.BuquScreens.CATEGORY_SELECT_SCREEN
import com.app.zuludin.buqu.navigation.BuquScreens.QUOTES_SCREEN
import com.app.zuludin.buqu.navigation.BuquScreens.SETTING_SCREEN
import com.app.zuludin.buqu.navigation.BuquScreens.SHARE_SCREEN
import com.app.zuludin.buqu.navigation.BuquScreens.UPSERT_QUOTE_SCREEN

private object BuquScreens {
    const val QUOTES_SCREEN = "quotes"
    const val UPSERT_QUOTE_SCREEN = "upsertQuote"
    const val SETTING_SCREEN = "setting"
    const val CATEGORY_SELECT_SCREEN = "categorySelect"
    const val SHARE_SCREEN = "share"
}

object BuquDestinationArgs {
    const val QUOTE_ID_ARG = "quoteId"
    const val TITLE_ARG = "title"
    const val SHARE_QUOTE_ARG = "shareQuote"
    const val SHARE_AUTHOR_ARG = "shareAuthor"
    const val SHARE_BOOK_ARG = "shareBook"
}

object BuquDestinations {
    const val QUOTES_ROUTE = QUOTES_SCREEN
    const val UPSERT_QUOTE_ROUTE =
        "${UPSERT_QUOTE_SCREEN}/{${TITLE_ARG}}?${QUOTE_ID_ARG}={${QUOTE_ID_ARG}}"
    const val SETTING_ROUTE = SETTING_SCREEN
    const val CATEGORY_SELECT_ROUTE = CATEGORY_SELECT_SCREEN
    const val SHARE_ROUTE =
        "${SHARE_SCREEN}?${SHARE_QUOTE_ARG}={${SHARE_QUOTE_ARG}}&${SHARE_AUTHOR_ARG}={${SHARE_AUTHOR_ARG}}&${SHARE_BOOK_ARG}={${SHARE_BOOK_ARG}}"
}

class BuquNavigationActions(private val navController: NavController) {
    fun navigateToUpsertQuote(title: String, quoteId: String?) {
        navController.navigate("$UPSERT_QUOTE_SCREEN/$title".let {
            if (quoteId != null) "$it?$QUOTE_ID_ARG=$quoteId" else it
        })
    }

    fun navigateToCategorySelect() {
        navController.navigate(BuquDestinations.CATEGORY_SELECT_ROUTE)
    }

    fun navigateToShareQuote(quote: String, author: String, book: String) {
        navController.navigate("${SHARE_SCREEN}?$SHARE_QUOTE_ARG=$quote&$SHARE_AUTHOR_ARG=$author&$SHARE_BOOK_ARG=$book")
    }
}