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

package me.theentropyshard.growser.ui.gemini

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.theentropyshard.growser.gemini.gemtext.document.*

@Composable
fun TableOfContents(
    elements: List<GemtextElement>,
    scrollState: ScrollState,
    offsets: Map<Int, Int>
) {
    val scope = rememberCoroutineScope()

    var expanded by rememberSaveable { mutableStateOf(false) }

    Column {
        TableOfContentsHeader(
            modifier = Modifier.padding(4.dp),
            expanded = expanded,
            onClick = { expanded = !expanded }
        )

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(start = 24.dp)) {
                for (element in elements) {
                    if (!element.isHeader()) {
                        continue
                    }

                    val padding = when (element) {
                        is GemtextH1Element -> 0.dp
                        is GemtextH2Element -> 24.dp
                        is GemtextH3Element -> 48.dp

                        else -> 0.dp
                    }

                    val fontWeight = when (element) {
                        is GemtextH1Element -> FontWeight.Bold
                        is GemtextH2Element -> FontWeight.Normal
                        is GemtextH3Element -> FontWeight.Normal

                        else -> FontWeight.Normal
                    }

                    Text(
                        modifier = Modifier
                            .padding(start = padding)
                            .clickable {
                                scope.launch {
                                    scrollState.animateScrollTo(
                                        offsets[elements.indexOf(element)] ?: 0
                                    )
                                }
                            },
                        text = (element as GemtextTextElement).text,
                        fontWeight = fontWeight
                    )
                }
            }
        }
    }
}

@Composable
private fun TableOfContentsHeader(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onClick() }.then(modifier)
    ) {
        Icon(
            modifier = Modifier.graphicsLayer { rotationZ = if (expanded) 90f else 0f },
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = ""
        )

        Text(text = "Table Of Contents")
    }
}
