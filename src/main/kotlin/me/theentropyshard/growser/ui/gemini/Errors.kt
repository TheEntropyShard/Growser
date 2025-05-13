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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.theentropyshard.growser.ui.theme.jetbrainsMonoFamily

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    headingText: String,
    mainText: String,
    fontFamily: FontFamily = jetbrainsMonoFamily,
    textAlign: TextAlign = TextAlign.Justify,
    fontWeight: FontWeight = FontWeight.Normal,
    horizontalScroll: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.error)
            .fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
            ) {
                Text(text = headingText)
            }

            Text(
                modifier = Modifier.padding(horizontal = 6.dp).then(
                    if (horizontalScroll) Modifier.horizontalScroll(rememberScrollState()) else Modifier
                ),
                text = mainText,
                color = MaterialTheme.colorScheme.onError,
                textAlign = textAlign,
                fontFamily = fontFamily,
                fontWeight = fontWeight
            )
        }
    }
}

@Composable
fun ExceptionView(
    modifier: Modifier = Modifier,
    message: String,
    stacktrace: String
) {
    ErrorView(
        modifier = modifier,
        headingText = message,
        mainText = stacktrace,
        textAlign = TextAlign.Start,
        horizontalScroll = true
    )
}

@Composable
fun GeminiError(
    modifier: Modifier = Modifier,
    headerText: String,
    statusCode: Int,
    statusCodeDetail: String = "",
    statusLine: String,
) {
    ErrorView(
        modifier = modifier,
        headingText = headerText,
        mainText = "Status code: $statusCode$statusCodeDetail\nError message: $statusLine"
    )
}

@Composable
fun TemporaryFailure(
    modifier: Modifier = Modifier,
    statusCode: Int,
    statusLine: String
) {
    val detailedCodeDescription = when (statusCode) {
        41 -> " (The server is unavailable due to overload or maintenance)"
        42 -> " (CGI Error. A CGI process, or similar system for generating dynamic content, died unexpectedly or timed out)"
        43 -> " (Proxy Error. The server was unable to successfully complete a transaction with the remote host)"
        44 -> " (Slow down. Do not send requests that frequently)"
        else -> ""
    }

    GeminiError(
        modifier = modifier,
        headerText = "Temporary failure. The server cannot process the request at the moment." +
                " Please try again later.",
        statusCode = statusCode,
        statusCodeDetail = detailedCodeDescription,
        statusLine = statusLine
    )
}

@Composable
fun PermanentFailure(
    modifier: Modifier = Modifier,
    statusCode: Int,
    statusLine: String
) {
    val detailedCodeDescription = when (statusCode) {
        51 -> " (Not Found)"
        52 -> " (Gone. The resource requested is no longer available and will not be available again)"
        53 -> " (Proxy request refused. The server does not accept proxy requests)"
        59 -> " (Bad Request)"
        else -> ""
    }

    GeminiError(
        modifier = modifier,
        headerText = "Permanent failure. The server cannot process this request.",
        statusCode = statusCode,
        statusCodeDetail = detailedCodeDescription,
        statusLine = statusLine
    )
}
