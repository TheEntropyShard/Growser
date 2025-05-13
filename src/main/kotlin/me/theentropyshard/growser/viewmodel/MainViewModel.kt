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

package me.theentropyshard.growser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.theentropyshard.growser.History
import me.theentropyshard.growser.gemini.client.GeminiClient
import me.theentropyshard.growser.gemini.client.GeminiRequest
import me.theentropyshard.growser.gemini.gemtext.GemtextParser
import me.theentropyshard.growser.gemini.gemtext.document.GemtextPage
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URI

enum class PageState {
    NotReady, Loading, Ready
}

class MainViewModel : ViewModel() {
    private var _pageState = MutableStateFlow(PageState.NotReady)
    val pageState = _pageState.asStateFlow()

    private var _currentUrl = MutableStateFlow("")
    val currentUrl = _currentUrl.asStateFlow()

    private var _document = MutableStateFlow(GemtextPage("", listOf()))
    val document = _document.asStateFlow()

    private var _statusCode = MutableStateFlow(0)
    val statusCode = _statusCode.asStateFlow()

    private var _statusLine = MutableStateFlow("")
    val statusLine = _statusLine.asStateFlow()

    private var _exception = MutableStateFlow("")
    val exception = _exception.asStateFlow()

    private val history = History()

    private val client = GeminiClient()

    fun loadPreviousPage() {
        this.loadPage(history.pop(this.currentUrl.value), false)
    }

    fun loadPage(url: String, addToHistory: Boolean = true) {
        var theUrl = url

        if (!url.startsWith("gemini://")) {
            theUrl = "gemini://$theUrl"
        }

        val path = URI(theUrl).path

        if (path == null || path.trim().isEmpty()) {
            theUrl = "$theUrl/"
        }

        if (addToHistory) history.visit(theUrl)

        _currentUrl.value = theUrl

        _pageState.value = PageState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                client.send(GeminiRequest(theUrl)).use { response ->
                    _exception.value = ""

                    if (response.statusCode.toString().startsWith("3")) {
                        var redirectUrl = response.metaInfo

                        if (!redirectUrl.startsWith("gemini://")) {
                            redirectUrl = "gemini://${URI(url).host}$redirectUrl"
                        }

                        loadPage(redirectUrl)

                        return@launch
                    }

                    _statusCode.value = response.statusCode
                    _statusLine.value = response.metaInfo

                    val text = response.readToString()
                    val page = GemtextParser().parse(text)

                    _pageState.value = PageState.Ready

                    _document.value = page
                }
            } catch (e: Exception) {
                _pageState.value = PageState.Ready
                val writer = StringWriter()
                val stream = PrintWriter(writer)
                e.printStackTrace(stream)
                _exception.value = writer.toString()
            }
        }
    }

    fun loadRelativePage(url: String) {
        var theUrl = url

        theUrl = if (_currentUrl.value.endsWith("/") && url.startsWith("/")) {
            "${_currentUrl.value}${url.drop(1)}"
        } else {
            "${_currentUrl.value}$theUrl"
        }

        this.loadPage(theUrl)
    }

    fun refresh() {
        this.loadPage(_currentUrl.value, false)
    }
}