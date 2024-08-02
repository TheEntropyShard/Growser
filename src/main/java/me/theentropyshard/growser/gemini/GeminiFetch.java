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

package me.theentropyshard.growser.gemini;

import me.theentropyshard.growser.gemini.client.GeminiClient;
import me.theentropyshard.growser.gemini.client.GeminiRequest;
import me.theentropyshard.growser.gemini.client.GeminiResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GeminiFetch {
    private static final GeminiClient CLIENT = new GeminiClient();

    public static String fetchWebPage(String url) throws IOException {
        url = url.endsWith("/") ? url : url + "/";

        try (GeminiResponse response = GeminiFetch.CLIENT.send(new GeminiRequest(url))) {
            return response.readToString();
        }
    }

    public static BufferedImage fetchImage(String url) throws IOException {
        try (GeminiResponse response = GeminiFetch.CLIENT.send(new GeminiRequest(url))) {
            return ImageIO.read(new BufferedInputStream(response.getInputStream()));
        }
    }
}
