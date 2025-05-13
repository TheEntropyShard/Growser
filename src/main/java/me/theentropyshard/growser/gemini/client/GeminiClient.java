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

import me.theentropyshard.growser.gemini.StreamUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Collections;

public class GeminiClient {
    private final RedirectHandler redirectHandler;

    private final SSLContext context;

    private int redirectAttempt = 1;

    public GeminiClient() {
        this(new LimitRedirectHandler());
    }

    public GeminiClient(RedirectHandler redirectHandler) {
        this.redirectHandler = redirectHandler;

        this.context = this.initSSLContext();
    }

    public GeminiResponse send(GeminiRequest request) throws IOException {
        SSLSocket socket = this.createSSLSocket(this.context, request.getHost(), request.getPort());

        request.writeTo(socket.getOutputStream());

        InputStream inputStream = socket.getInputStream();

        int firstCodeDigit = ((char) inputStream.read()) - '0';
        int secondCodeDigit = ((char) inputStream.read()) - '0';
        int code = firstCodeDigit * 10 + secondCodeDigit;

        //noinspection ResultOfMethodCallIgnored
        inputStream.read(); // Skip the space

        byte[] crlf = "\r\n".getBytes(StandardCharsets.US_ASCII);

        String metaInfo = new String(StreamUtils.readUntilDelimiter(inputStream, crlf), StandardCharsets.UTF_8);

        if (firstCodeDigit == 3 && this.redirectHandler.redirect(metaInfo, this.redirectAttempt)) {
            this.redirectAttempt++;

            StreamUtils.closeQuietly(socket);

            if (metaInfo.startsWith("gemini://")) {
                return this.send(new GeminiRequest(metaInfo));
            } else {
                return this.send(new GeminiRequest(request.getRawUri().resolve(metaInfo)));
            }
        } else {
            this.redirectAttempt = 0;
        }

        return new GeminiResponse(code, metaInfo, inputStream);
    }

    private SSLContext initSSLContext() {
        try {
            TrustManager[] trustManagers = new TrustManager[]{
                new GeminiTrustManager()
            };

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());

            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SSLSocket createSSLSocket(SSLContext context, String host, int port) throws IOException {
        SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(host, port);

        SSLParameters params = new SSLParameters();
        params.setServerNames(Collections.singletonList(new SNIHostName(host)));
        socket.setSSLParameters(params);
        socket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});

        return socket;
    }
}
