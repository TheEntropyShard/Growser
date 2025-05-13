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

package me.theentropyshard.growser.gemini.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class GeminiRequest {
    public static final int DEFAULT_PORT = 1965;

    private final URI rawUri;

    private final String uri;
    private final String host;
    private final int port;

    public GeminiRequest(String uri) {
        this(URI.create(uri));
    }

    public GeminiRequest(URI uri) {
        if (uri.getScheme() == null || !uri.getScheme().equals("gemini")) {
            throw new IllegalArgumentException("URI scheme for Gemini must be 'gemini://'");
        }

        if (uri.getUserInfo() != null) {
            throw new IllegalArgumentException("User info must not be present in Gemini URI");
        }

        this.rawUri = uri;

        this.uri = uri.toASCIIString();
        this.host = uri.getHost();
        this.port = uri.getPort() == -1 ? GeminiRequest.DEFAULT_PORT : uri.getPort();
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write((this.uri + "\r\n").getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    public URI getRawUri() {
        return this.rawUri;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }
}
