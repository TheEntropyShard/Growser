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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.HWND
import me.theentropyshard.growser.ui.Growser
import me.theentropyshard.growser.ui.components.titlebar.TitleBar
import me.theentropyshard.growser.ui.theme.GrowserTheme
import me.theentropyshard.growser.ui.windows.CustomWindowProcedure
import java.awt.Dimension
import kotlin.system.exitProcess

fun main() = application {
    val windowState = rememberWindowState()

    Window(
        state = windowState,
        title = "Growser",
        undecorated = true,
        onCloseRequest = ::exitApplication
    ) {
        window.minimumSize = Dimension(620, 90)

        val onMinimizeClick: () -> Unit = if (System.getProperty("os.name").lowercase().contains("windows")) {
            val windowHandle = remember(this.window) {
                val windowPointer = this.window.windowHandle.let(::Pointer)
                HWND(windowPointer)
            }

            remember(windowHandle) { CustomWindowProcedure(windowHandle) };

            {
                User32.INSTANCE.CloseWindow(windowHandle)
            }
        } else {
            {
                windowState.isMinimized = true
            }
        }

        GrowserTheme {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TitleBar(
                    state = windowState,
                    onMinimizeClick = onMinimizeClick,
                    onFullscreenClick = {

                    },
                    onCloseClick = {
                        exitProcess(0)
                    }
                )

                Growser()
            }
        }
    }
}
