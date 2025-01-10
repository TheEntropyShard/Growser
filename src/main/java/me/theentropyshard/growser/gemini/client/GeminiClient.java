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

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Collections;

public class GeminiClient {
    private final SSLContext context;

    public GeminiClient() {
        this.context = this.initSSLContext();
    }

    public GeminiResponse send(GeminiRequest request) throws IOException {
        @SuppressWarnings("resource")
        SSLSocket socket = this.createSSLSocket(this.context, request.getHost(), request.getPort());

        request.writeTo(socket.getOutputStream());

        return GeminiResponse.readFrom(socket.getInputStream());
    }

    private SSLContext initSSLContext() {
        try {
            TrustManager[] trustManagers = new TrustManager[]{
                new GeminiTrustManager()
            };

            SSLContext context = SSLContext.getInstance("SSL");
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

        return socket;
    }
}
