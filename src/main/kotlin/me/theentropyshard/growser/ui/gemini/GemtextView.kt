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

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import me.theentropyshard.growser.gemini.gemtext.document.GemtextBlockquoteElement
import me.theentropyshard.growser.gemini.gemtext.document.GemtextElement
import me.theentropyshard.growser.gemini.gemtext.document.GemtextElementType
import me.theentropyshard.growser.gemini.gemtext.document.GemtextH1Element
import me.theentropyshard.growser.gemini.gemtext.document.GemtextH2Element
import me.theentropyshard.growser.gemini.gemtext.document.GemtextH3Element
import me.theentropyshard.growser.gemini.gemtext.document.GemtextLinkElement
import me.theentropyshard.growser.gemini.gemtext.document.GemtextListElement
import me.theentropyshard.growser.gemini.gemtext.document.GemtextPreformattedElement
import me.theentropyshard.growser.gemini.gemtext.document.GemtextTextElement
import kotlin.math.roundToInt

@Composable
fun GemtextView(
    elements: List<GemtextElement>,
    scrollState: ScrollState,
    onUrlClick: (String) -> Unit = {}
) {
    val offsets = remember { mutableStateMapOf<Int, Int>() }

    val showTableOfContents by remember { mutableStateOf(true) }

    if (showTableOfContents) {
        TableOfContents(
            elements = elements,
            scrollState = scrollState,
            offsets = offsets
        )
    }

    for (element in elements) {
        when (element.type) {
            GemtextElementType.PARAGRAPH -> {
                GemtextParagraph(text = (element as GemtextTextElement).text)
            }

            GemtextElementType.LINK -> {
                val linkElement = (element as GemtextLinkElement)

                GemtextLink(
                    label = linkElement.label,
                    url = linkElement.link,
                    onClick = { onUrlClick(it) }
                )
            }

            GemtextElementType.LIST -> {
                GemtextList(items = (element as GemtextListElement).items)
            }

            GemtextElementType.H1, GemtextElementType.H2, GemtextElementType.H3 -> {
                val modifier = Modifier.onGloballyPositioned {
                    offsets[elements.indexOf(element)] = it.positionInParent().y.roundToInt()
                }

                when (element) {
                    is GemtextH1Element -> {
                        GemtextH1(modifier = modifier, text = element.text)
                    }

                    is GemtextH2Element -> {
                        GemtextH2(modifier = modifier, text = element.text)
                    }

                    is GemtextH3Element -> {
                        GemtextH3(modifier = modifier, text = element.text)
                    }
                }
            }

            GemtextElementType.BLOCKQUOTE -> {
                GemtextBlockquote(text = (element as GemtextBlockquoteElement).text)
            }

            GemtextElementType.PREFORMATTED -> {
                val preformatted = (element as GemtextPreformattedElement)

                GemtextPreformatted(
                    caption = preformatted.caption,
                    text = preformatted.text
                )
            }

            null -> {}
        }
    }
}
