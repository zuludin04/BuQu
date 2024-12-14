package com.app.zuludin.buqu.navigation

import androidx.navigation.NavController
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.CATEGORY_TITLE_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.QUOTE_ID_ARG
import com.app.zuludin.buqu.navigation.BuquDestinationArgs.TITLE_ARG
import com.app.zuludin.buqu.navigation.BuquScreens.CATEGORY_SELECT_SCREEN
import com.app.zuludin.buqu.navigation.BuquScreens.CATEGORY_UPSERT_SCREEN
import com.app.zuludin.buqu.navigation.BuquScreens.QUOTES_SCREEN
import com.app.zuludin.buqu.navigation.BuquScreens.SETTING_SCREEN
import com.app.zuludin.buqu.navigation.BuquScreens.UPSERT_QUOTE_SCREEN

private object BuquScreens {
    const val QUOTES_SCREEN = "quotes"
    const val UPSERT_QUOTE_SCREEN = "upsertQuote"
    const val SETTING_SCREEN = "setting"
    const val CATEGORY_SELECT_SCREEN = "categorySelect"
    const val CATEGORY_UPSERT_SCREEN = "upsertCategory"
}

object BuquDestinationArgs {
    const val QUOTE_ID_ARG = "quoteId"
    const val TITLE_ARG = "title"
    const val CATEGORY_TITLE_ARG = "categoryTitle"
}

object BuquDestinations {
    const val QUOTES_ROUTE = QUOTES_SCREEN
    const val UPSERT_QUOTE_ROUTE =
        "${UPSERT_QUOTE_SCREEN}/{${TITLE_ARG}}?${QUOTE_ID_ARG}={${QUOTE_ID_ARG}}"
    const val SETTING_ROUTE = SETTING_SCREEN
    const val CATEGORY_SELECT_ROUTE = CATEGORY_SELECT_SCREEN
    const val CATEGORY_UPSERT_ROUTE = "${CATEGORY_UPSERT_SCREEN}/{${CATEGORY_TITLE_ARG}}"
}

class BuquNavigationActions(private val navController: NavController) {
    fun navigateToUpsertQuote(title: String, quoteId: String?) {
        navController.navigate("$UPSERT_QUOTE_SCREEN/$title".let {
            if (quoteId != null) "$it?$QUOTE_ID_ARG=$quoteId" else it
        })
    }

    fun navigateToSetting() {
        navController.navigate(BuquDestinations.SETTING_ROUTE)
    }

    fun navigateToCategorySelect() {
        navController.navigate(BuquDestinations.CATEGORY_SELECT_ROUTE)
    }

    fun navigateToUpsertCategory(title: String) {
        navController.navigate("$CATEGORY_UPSERT_SCREEN/$title")
    }
}