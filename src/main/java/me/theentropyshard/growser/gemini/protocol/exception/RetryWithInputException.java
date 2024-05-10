/*
 * Growser - https://github.com/TheEntropyShard/Growser
 * Copyright (C) 2023-2024 TheEntropyShard
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

package me.theentropyshard.growser.gemini.protocol.exception;

import java.io.IOException;
import java.net.URL;

public class RetryWithInputException extends IOException {
    private final URL url;
    private final boolean hide;
    private final String prompt;

    public RetryWithInputException(URL url, boolean hide, String prompt) {
        super("Retry with input");

        this.url = url;
        this.hide = hide;
        this.prompt = prompt;
    }

    public URL getURL() {
        return this.url;
    }

    public boolean getHide() {
        return this.hide;
    }

    public String getPrompt() {
        return this.prompt;
    }
}