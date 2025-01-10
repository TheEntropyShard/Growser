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

import me.theentropyshard.growser.utils.StreamUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GeminiResponse implements Closeable {
    private final int statusCode;
    private final String metaInfo;
    private final InputStream inputStream;

    public GeminiResponse(int statusCode, String metaInfo, InputStream inputStream) {
        this.statusCode = statusCode;
        this.metaInfo = metaInfo;
        this.inputStream = inputStream;
    }

    public static GeminiResponse readFrom(InputStream inputStream) throws IOException {
        byte[] rawCode = new byte[3];
        inputStream.read(rawCode);
        int code = (rawCode[0] - '0') * 10 + rawCode[1] - '0';

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8), 1024);

        return new GeminiResponse(code, reader.readLine(), inputStream);
    }

    public String readToString() throws IOException {
        return StreamUtils.readToString(this.inputStream);
    }

    public byte[] readToByteArray() throws IOException {
        return StreamUtils.readToByteArray(this.inputStream);
    }

    @Override
    public void close() throws IOException {
        if (this.inputStream != null) {
            this.inputStream.close();
        }
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getMetaInfo() {
        return this.metaInfo;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }
}
