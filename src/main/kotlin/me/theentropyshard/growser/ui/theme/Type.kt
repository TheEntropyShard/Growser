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

package me.theentropyshard.growser.ui.theme

import androidx.compose.material3.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val jetbrainsMonoFamily = FontFamily(
    Font("font/jetbrainsmononl/jetbrainsmononl_bold.ttf", FontWeight.Bold),
    Font("font/jetbrainsmononl/jetbrainsmononl_italic.ttf", FontWeight.Normal, FontStyle.Italic),
    Font("font/jetbrainsmononl/jetbrainsmononl_light.ttf", FontWeight.Light),
    Font("font/jetbrainsmononl/jetbrainsmononl_medium.ttf", FontWeight.Medium),
    Font("font/jetbrainsmononl/jetbrainsmononl_regular.ttf", FontWeight.Normal),
)

val robotoFamily = FontFamily(
    Font("font/roboto/roboto_regular.ttf", FontWeight.Normal),
    Font("font/notocoloremoji/notocoloremoji_regular.ttf", FontWeight.Normal),
    Font("font/roboto/roboto_black.ttf", FontWeight.Black),
    Font("font/roboto/roboto_blackitalic.ttf", FontWeight.Black, FontStyle.Italic),
    Font("font/roboto/roboto_bold.ttf", FontWeight.Bold),
    Font("font/roboto/roboto_bolditalic.ttf", FontWeight.Bold, FontStyle.Italic),
    Font("font/roboto/roboto_extrabold.ttf", FontWeight.ExtraBold),
    Font("font/roboto/roboto_extrabolditalic.ttf", FontWeight.ExtraBold, FontStyle.Italic),
    Font("font/roboto/roboto_extralight.ttf", FontWeight.ExtraLight),
    Font("font/roboto/roboto_extralightitalic.ttf", FontWeight.ExtraLight, FontStyle.Italic),
    Font("font/roboto/roboto_italic.ttf", FontWeight.Normal, FontStyle.Italic),
    Font("font/roboto/roboto_light.ttf", FontWeight.Light),
    Font("font/roboto/roboto_lightitalic.ttf", FontWeight.Light, FontStyle.Italic),
    Font("font/roboto/roboto_medium.ttf", FontWeight.Medium),
    Font("font/roboto/roboto_mediumitalic.ttf", FontWeight.Medium, FontStyle.Italic),
    Font("font/roboto/roboto_semibold.ttf", FontWeight.SemiBold),
    Font("font/roboto/roboto_semibolditalic.ttf", FontWeight.SemiBold, FontStyle.Italic),
    Font("font/roboto/roboto_thin.ttf", FontWeight.Thin),
    Font("font/roboto/roboto_thinitalic.ttf", FontWeight.Thin, FontStyle.Italic),
)
