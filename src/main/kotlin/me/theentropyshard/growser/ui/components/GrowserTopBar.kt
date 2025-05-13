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

package me.theentropyshard.growser.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.ArrowLeft
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

enum class MenuButton {
    NewTab,
    SavePage,
    History,
    Downloads,
    Bookmarks,
    Settings,
    About
}

@Composable
fun GrowserTopBar(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    state: TextFieldState,
    onSearch: (String) -> Unit = {},
    canClickBack: Boolean = false,
    canClickForward: Boolean = false,
    onBackClick: () -> Unit = {},
    onForwardClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {},
    onMenuItemClick: (MenuButton) -> Unit = {}
) {
    val density = LocalDensity.current

    val darkTheme = isSystemInDarkTheme()

    var menuShown by remember { mutableStateOf(false) }

    var offsetX by remember {
        mutableStateOf(0.dp)
    }

    var parentWidth by remember {
        mutableIntStateOf(0)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onPlaced {
                parentWidth = it.size.width
            }
            .drawBehind {
                drawLine(
                    if (darkTheme) Color.DarkGray else Color.LightGray,
                    Offset(0f, size.height),
                    Offset(size.width, size.height),
                    1f
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopBarIconButton(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Click to go back",
                enabled = canClickBack,
                onClick = onBackClick
            )

            Spacer(modifier = Modifier.width(6.dp))

            TopBarIconButton(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = "Click to go forward",
                enabled = canClickForward,
                onClick = onForwardClick
            )

            Spacer(modifier = Modifier.width(6.dp))

            TopBarIconButton(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = "Click to refresh the page",
                enabled = true,
                onClick = onRefreshClick
            )

            Spacer(modifier = Modifier.width(6.dp))

            SearchField(
                modifier = Modifier.weight(1f),
                hint = "Enter Gemini address",
                state = state,
                onSearch = onSearch
            )

            Spacer(modifier = Modifier.width(6.dp))

            TopBarIconButton(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "Click to open menu",
                enabled = true,
                iconSize = 20.dp,
                onClick = { menuShown = true }
            )
        }

        DropdownMenu(
            expanded = menuShown,
            onDismissRequest = { menuShown = false },
            offset = DpOffset(offsetX - 8.dp, 8.dp),
            modifier = Modifier
                .width(175.dp)
                .onPlaced {
                    offsetX = with(density) { (parentWidth - it.size.width).toDp() }
                }
        ) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AddBox,
                        contentDescription = ""
                    )
                },
                text = {
                    Text("New Tab")
                },
                onClick = {
                    menuShown = false
                    onMenuItemClick(MenuButton.NewTab)
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Save,
                        contentDescription = ""
                    )
                },
                text = {
                    Text("Save Page")
                },
                onClick = {
                    menuShown = false
                    onMenuItemClick(MenuButton.SavePage)
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.History,
                        contentDescription = ""
                    )
                },
                text = {
                    Text("History")
                },
                onClick = {
                    menuShown = false
                    onMenuItemClick(MenuButton.History)
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.DownloadDone,
                        contentDescription = ""
                    )
                },
                text = {
                    Text("Downloads")
                },
                onClick = {
                    menuShown = false
                    onMenuItemClick(MenuButton.Downloads)
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = ""
                    )
                },
                text = {
                    Text("Bookmarks")
                },
                onClick = {
                    menuShown = false
                    onMenuItemClick(MenuButton.Bookmarks)
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = ""
                    )
                },
                text = {
                    Text("Settings")
                },
                onClick = {
                    menuShown = false
                    onMenuItemClick(MenuButton.Settings)
                }
            )
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                        contentDescription = ""
                    )
                },
                text = {
                    Text("About")
                },
                onClick = {
                    menuShown = false
                    onMenuItemClick(MenuButton.About)
                }
            )
        }
    }

    if (isLoading) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        )
    }
}

@Composable
private fun TopBarIconButton(
    imageVector: ImageVector,
    contentDescription: String = "",
    enabled: Boolean = true,
    iconButtonSize: Dp = 32.dp,
    iconSize: Dp = 18.dp,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier.size(iconButtonSize),
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}
