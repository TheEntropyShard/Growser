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

package me.theentropyshard.growser.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun AppearanceSettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(modifier = modifier.fillMaxSize()) {
        val scope = rememberCoroutineScope()
        val showTableOfContents by remember { mutableStateOf(true) }

        SettingsItem(
            text = "Show table of contents",
            trailingContent = {
                Switch(
                    checked = showTableOfContents,
                    onCheckedChange = {
                        /*scope.launch {
                            settings.saveShowTableOfContents(it)
                        }*/
                    }
                )
            }
        )
    }
}
