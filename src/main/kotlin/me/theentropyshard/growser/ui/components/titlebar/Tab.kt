package me.theentropyshard.growser.ui.components.titlebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Tab(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {},
    title: String
) {
    val surfaceColor = MaterialTheme.colorScheme.surface

    Box(modifier = modifier.size(250.dp, 42.dp)) {
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(surfaceColor)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon()

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = -0.5f
                        }
                        .weight(2f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    text = title,
                    fontSize = 12.sp
                )

                IconButton(
                    modifier = Modifier.size(16.dp),
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null
                    )
                }
            }
        }
    }
}