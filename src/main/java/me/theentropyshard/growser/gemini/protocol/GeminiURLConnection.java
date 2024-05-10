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

package me.theentropyshard.growser.gemini.protocol;

import me.theentropyshard.growser.gemini.protocol.exception.ErrorResponseException;
import me.theentropyshard.growser.gemini.protocol.exception.RedirectedException;
import me.theentropyshard.growser.gemini.protocol.exception.RetryWithInputException;
import me.theentropyshard.growser.utils.StreamUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collections;

public class GeminiURLConnection extends URLConnection {
    public static final String CRLF = "\r\n";
    private SSLSocket socket = null;
    private String contentType = null;
    private InputStream inputStream = null;
    private byte[] content = null;
    private String meta = null;

    public GeminiURLConnection(URL url) {
        super(url);
    }

    private static int parseStatus(String line) {
        String[] args = line.split("\\s+", 2);

        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String parseMeta(String line) {
        String[] args = line.split("\\s+", 2);

        if (args.length > 1) {
            return args[1];
        } else {
            return "";
        }
    }

    @Override
    public void connect() throws IOException {
        if (this.socket != null && this.socket.isConnected()) {
            return;
        }

        try {
            this.initConnection();
            this.sendRequest();
            this.readHeader();
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        } catch (KeyManagementException e) {
            throw new IOException(e);
        }
    }

    private void initConnection() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {

                    }
                }
        };

        String host = this.getURL().getHost();

        int port = this.getURL().getPort();
        if (port == -1) {
            port = 1965;
        }

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());

        this.socket = (SSLSocket) sc.getSocketFactory().createSocket(host, port);

        SSLParameters params = new SSLParameters();
        params.setServerNames(Collections.singletonList(new SNIHostName(host)));
        this.socket.setSSLParameters(params);

        this.inputStream = this.socket.getInputStream();
    }

    private void sendRequest() throws IOException {
        OutputStream outputStream = this.socket.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        String url = this.getURL().toString();
        if (!url.endsWith("/")) {
            url += "/";
        }
        printStream.print(url);
        printStream.print(GeminiURLConnection.CRLF);
        printStream.flush();
    }

    private void readHeader() throws IOException {
        StringBuilder lineBuilder = new StringBuilder();

        char c;
        do {
            c = (char) this.inputStream.read();

            if (c == '\n') {
                break;
            }

            if (c != '\r') {
                lineBuilder.append(c);
            }
        } while (c != 65535);

        String line = lineBuilder.toString();

        int status = GeminiURLConnection.parseStatus(line);
        this.meta = GeminiURLConnection.parseMeta(line);

        if (status >= 20 && status < 30) {
            // Nothing to do -- input stream is now positioned to read data
        } else {
            this.socket.close();
            this.socket = null;

            if (status < 10) {
                throw new ErrorResponseException(this.getURL(), status, "Invalid status code in response");
            }

            if (status >= 10 && status < 20) {
                throw new RetryWithInputException(this.getURL(), status == 11, this.meta);
            } else if (status >= 30 && status < 40) {
                throw new RedirectedException(new URL(this.meta));
            } else if (status >= 40) {
                throw new ErrorResponseException(this.getURL(), status, this.meta);
            }
        }
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public Object getContent() throws IOException {
        if (this.content != null) {
            return this.content;
        }

        try {
            this.connect();

            this.contentType = this.meta;
            this.content = StreamUtils.readToByteArray(this.inputStream);

            this.socket.close();
            this.socket = null;

            return this.content;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        this.connect();

        return this.inputStream;
    }
}