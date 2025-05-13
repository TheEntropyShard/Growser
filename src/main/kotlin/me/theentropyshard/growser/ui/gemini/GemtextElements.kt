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

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.theentropyshard.growser.gemini.gemtext.document.GemtextElement
import me.theentropyshard.growser.gemini.gemtext.document.GemtextH1Element
import me.theentropyshard.growser.gemini.gemtext.document.GemtextH2Element
import me.theentropyshard.growser.gemini.gemtext.document.GemtextH3Element
import me.theentropyshard.growser.ui.theme.jetbrainsMonoFamily
import me.theentropyshard.growser.ui.theme.robotoFamily

fun GemtextElement.isHeader(): Boolean {
    return this is GemtextH1Element || this is GemtextH2Element || this is GemtextH3Element
}

@Composable
fun GemtextHeader(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit = TextUnit.Unspecified
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontWeight = fontWeight,
            fontSize = fontSize,
            lineHeight = lineHeight
        )
    }
}

@Composable
fun GemtextH1(
    modifier: Modifier = Modifier,
    text: String
) {
    GemtextHeader(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 32.sp
    )
}

@Composable
fun GemtextH2(
    modifier: Modifier = Modifier,
    text: String
) {
    GemtextHeader(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    )
}

@Composable
fun GemtextH3(
    modifier: Modifier = Modifier,
    text: String
) {
    GemtextHeader(
        modifier = modifier,
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    )
}

@Composable
fun GemtextParagraph(
    modifier: Modifier = Modifier,
    text: String,
    indent: Boolean = false
) {
    val textStyle = if (indent) {
        LocalTextStyle.current.copy(textIndent = TextIndent(firstLine = 24.sp))
    } else {
        LocalTextStyle.current.copy(fontFamily = robotoFamily)
    }

    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Justify,
        style = textStyle
    )
}

@Composable
fun GemtextLink(
    modifier: Modifier = Modifier,
    label: String? = null,
    url: String,
    onClick: (String) -> Unit
) {
    val defaultUriHandler = LocalUriHandler.current

    val annotatedString = buildAnnotatedString {
        withLink(
            LinkAnnotation.Url(
                url = url,
                linkInteractionListener = {
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        defaultUriHandler.openUri(url)
                    } else {
                        onClick(url)
                    }
                }
            )
        ) {
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(label ?: url)
            }
        }
    }

    Text(
        modifier = modifier.fillMaxWidth(),
        text = annotatedString
    )
}

private const val LIST_BULLET = "\u2022"

@Composable
fun GemtextList(
    modifier: Modifier = Modifier,
    items: List<String>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (item in items) {
            Row {
                Text(text = LIST_BULLET)

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = item,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Composable
fun GemtextPreformatted(
    modifier: Modifier = Modifier,
    caption: String? = null,
    text: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        if (!caption.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 6.dp)
            ) {
                Text(
                    text = caption,
                    fontFamily = jetbrainsMonoFamily,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .horizontalScroll(rememberScrollState()),
            text = text,
            fontFamily = jetbrainsMonoFamily,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun GemtextBlockquote(
    modifier: Modifier = Modifier,
    text: String
) {
    val primary = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = primary,
                    size = Size(4.dp.toPx(), size.height),
                )
            }
    ) {
        Row {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                text = "❞"
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                text = text,
                textAlign = TextAlign.Justify,
            )
        }
    }
}
