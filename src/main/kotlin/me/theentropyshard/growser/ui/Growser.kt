/*
 * Growser - https://github.com/TheEntropyShard/Growser
 * Copyright (C) 2023-2025 TheEntropyShard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.theentropyshard.growser.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndSelectAll
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.theentropyshard.growser.ui.components.GrowserTopBar
import me.theentropyshard.growser.ui.components.MenuButton
import me.theentropyshard.growser.ui.gemini.ExceptionView
import me.theentropyshard.growser.ui.gemini.GemtextView
import me.theentropyshard.growser.ui.gemini.PermanentFailure
import me.theentropyshard.growser.ui.gemini.TemporaryFailure
import me.theentropyshard.growser.ui.screen.AppearanceSettingsScreen
import me.theentropyshard.growser.ui.screen.SettingsScreen
import me.theentropyshard.growser.viewmodel.MainViewModel
import me.theentropyshard.growser.viewmodel.PageState

@Composable
fun Growser() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val mainViewModel: MainViewModel = viewModel()

    val exception by mainViewModel.exception.collectAsState()

    val pageState by mainViewModel.pageState.collectAsState()
    val currentUrl by mainViewModel.currentUrl.collectAsState()
    val page by mainViewModel.document.collectAsState()

    val statusCode by mainViewModel.statusCode.collectAsState()
    val statusLine by mainViewModel.statusLine.collectAsState()

    val navController = rememberNavController()

    val state = remember(currentUrl) { TextFieldState(initialText = currentUrl) }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            NavHost(
                navController = navController,
                startDestination = "browser",
                enterTransition = {
                    if (initialState.destination.route == "browser") {
                        fadeIn(tween(250))
                    } else {
                        fadeIn(tween(250)) + slideInHorizontally { it / 2 }
                    }
                },
                exitTransition = {
                    if (initialState.destination.route == "browser") {
                        fadeOut(tween(200))
                    } else {
                        fadeOut(tween(200)) + slideOutHorizontally { -it / 2 }
                    }
                },
                popEnterTransition = {
                    if (targetState.destination.route == "browser") {
                        fadeIn(tween(250))
                    } else {
                        fadeIn(tween(250)) + slideInHorizontally { -it / 2 }
                    }
                },
                popExitTransition = {
                    if (targetState.destination.route == "browser") {
                        fadeOut(tween(200))
                    } else {
                        fadeOut(tween(200)) + slideOutHorizontally { it / 2 }
                    }
                },
            ) {
                composable("browser") {
                    Column {
                        GrowserTopBar(
                            modifier = Modifier
                                .onFocusChanged {
                                    if (it.isFocused) {
                                        state.setTextAndSelectAll(state.text.toString())
                                    }
                                },
                            isLoading = pageState == PageState.Loading,
                            state = state,
                            onSearch = {
                                keyboardController?.hide()
                                mainViewModel.loadPage(it)
                            },
                            onBackClick = {

                            },
                            onForwardClick = {

                            },
                            onRefreshClick = {
                                mainViewModel.refresh()
                            },
                            onMenuItemClick = { button ->
                                when (button) {
                                    MenuButton.NewTab -> {

                                    }

                                    MenuButton.SavePage -> {

                                    }

                                    MenuButton.History -> {

                                    }

                                    MenuButton.Downloads -> {

                                    }

                                    MenuButton.Bookmarks -> {

                                    }

                                    MenuButton.Settings -> {
                                        navController.navigate("settings")
                                    }

                                    MenuButton.About -> {

                                    }
                                }
                            }
                        )

                        //Box(modifier = Modifier.background(Color.DarkGray).fillMaxSize())

                        if (pageState == PageState.Ready) {
                            val digit = statusCode / 10

                            val scrollState = rememberScrollState()

                            SelectionContainer {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .verticalScroll(scrollState),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(0.75f)
                                    ) {
                                        if (exception.isNotEmpty()) {
                                            ExceptionView(
                                                message = "Could not fetch $currentUrl",
                                                stacktrace = exception
                                            )
                                        } else {
                                            if (digit == 2) {
                                                GemtextView(
                                                    elements = page.elements,
                                                    scrollState = scrollState,
                                                    onUrlClick = {
                                                        if (it.startsWith("gemini://")) {
                                                            mainViewModel.loadPage(it)
                                                        } else {
                                                            mainViewModel.loadRelativePage(it)
                                                        }
                                                    }
                                                )
                                            } else {
                                                when (digit) {
                                                    4 -> {
                                                        TemporaryFailure(
                                                            statusCode = statusCode,
                                                            statusLine = statusLine
                                                        )
                                                    }

                                                    5 -> {
                                                        PermanentFailure(
                                                            statusCode = statusCode,
                                                            statusLine = statusLine
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }

                }

                composable("settings") {
                    SettingsScreen(navController = navController)
                }

                composable("settings/appearance") {
                    AppearanceSettingsScreen(
                        navController = navController
                    )
                }
            }
        }
    }
}
