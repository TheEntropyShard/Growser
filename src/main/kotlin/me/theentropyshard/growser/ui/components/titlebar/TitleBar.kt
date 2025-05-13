package me.theentropyshard.growser.ui.components.titlebar

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Minimize
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState

@Composable
fun TitleBar(
    state: WindowState,
    onMinimizeClick: () -> Unit = {},
    onFullscreenClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
) {
    var dragPositionStart = remember { Offset(0f, 0f) }

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(end = 8.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragPositionStart = it
                    }
                ) { change, dragAmount ->
                    state.position = WindowPosition(
                        state.position.x + change.position.x.dp - dragPositionStart.x.dp,
                        state.position.y + change.position.y.dp - dragPositionStart.y.dp
                    )
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .padding(start = 4.dp)
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Tab(
                icon = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Outlined.Language,
                        contentDescription = ""
                    )
                },
                title = "Hello"
            )

            Tab(
                icon = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Outlined.Language,
                        contentDescription = ""
                    )
                },
                title = "Project Gemini"
            )
        }

        Row {
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = onMinimizeClick
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Outlined.Minimize,
                    contentDescription = ""
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = onFullscreenClick
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Outlined.OpenInFull,
                    contentDescription = ""
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = onCloseClick
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Outlined.Close,
                    contentDescription = ""
                )
            }
        }
    }
}