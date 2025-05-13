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

package me.theentropyshard.growser

class History {
    private val urls: ArrayDeque<String> = ArrayDeque()

    fun visit(url: String) {
        urls.add(url)
    }

    fun pop(currentUrl: String): String {
        if (urls.size == 0) {
            return currentUrl
        }

        val last = urls.removeLast()

        if (last == currentUrl && urls.size > 0) {
            return urls.removeLast()
        }

        return last
    }
}