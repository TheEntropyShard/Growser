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

package me.theentropyshard.growser.gemini;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class StreamUtils {
    private static final int BUFFER_SIZE = 8192;
    private static final int EOF = -1;

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        StreamUtils.copy(inputStream, outputStream, StreamUtils.BUFFER_SIZE);
    }

    public static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        for (int numRead; (numRead = inputStream.read(buffer)) != StreamUtils.EOF; ) {
            outputStream.write(buffer, 0, numRead);
        }
        outputStream.flush();
    }

    public static ByteArrayOutputStream readToBAOS(InputStream inputStream) throws IOException {
        return StreamUtils.readToBAOS(inputStream, StreamUtils.BUFFER_SIZE);
    }

    public static ByteArrayOutputStream readToBAOS(InputStream inputStream, int bufferSize) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        StreamUtils.copy(inputStream, outputStream, bufferSize);

        return outputStream;
    }

    public static byte[] readToByteArray(InputStream inputStream) throws IOException {
        return StreamUtils.readToByteArray(inputStream, StreamUtils.BUFFER_SIZE);
    }

    public static byte[] readToByteArray(InputStream inputStream, int bufferSize) throws IOException {
        Objects.requireNonNull(inputStream);

        if (bufferSize < 1) {
            throw new IllegalArgumentException("bufferSize cannot be less than 1");
        }

        return StreamUtils.readToBAOS(inputStream, bufferSize).toByteArray();
    }

    public static String readToString(InputStream inputStream) throws IOException {
        return StreamUtils.readToString(inputStream, StandardCharsets.UTF_8);
    }

    public static String readToString(InputStream inputStream, Charset charset) throws IOException {
        Objects.requireNonNull(inputStream);
        Objects.requireNonNull(charset);

        return new String(StreamUtils.readToBAOS(inputStream).toByteArray(), charset);
    }

    public static byte[] readUntilDelimiter(InputStream inputStream, byte[] delimiter) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int readByte;
        int matchIndex = 0;

        int delimiterLength = delimiter.length;

        while ((readByte = inputStream.read()) != -1) {
            if (readByte == delimiter[matchIndex]) {
                matchIndex++;

                if (matchIndex == delimiterLength) {
                    break;
                }
            } else {
                if (matchIndex > 0) {
                    for (int i = 0; i < matchIndex; i++) {
                        baos.write(delimiter[i]);
                    }

                    matchIndex = 0;
                }

                baos.write(readByte);
            }
        }

        return baos.toByteArray();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException ignored) {

        }
    }

    private StreamUtils() {
        throw new UnsupportedOperationException();
    }
}