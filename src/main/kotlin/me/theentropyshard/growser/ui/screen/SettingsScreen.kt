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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

val SettingsItemHeight = 56.dp

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(modifier = modifier.fillMaxSize()) {
        SettingsItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.FormatPaint,
                    contentDescription = ""
                )
            },
            text = "Appearance",
            onClick = {
                navController.navigate("settings/appearance")
            }
        )
    }
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    leadingContent: @Composable RowScope.() -> Unit = {},
    text: String,
    subText: String = "",
    trailingContent: @Composable RowScope.() -> Unit = {},
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(SettingsItemHeight)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(modifier = Modifier.padding(start = 16.dp), content = leadingContent)

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold
            )

            if (subText.isNotEmpty()) {
                Text(
                    text = text
                )
            }
        }

        Row(modifier = Modifier.padding(end = 16.dp), content = trailingContent)
    }
}